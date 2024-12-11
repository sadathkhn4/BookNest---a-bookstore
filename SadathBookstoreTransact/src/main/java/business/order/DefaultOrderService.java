package business.order;

import api.ApiException;
import business.BookstoreDbException;
import business.JdbcUtils;
import business.book.Book;
import business.book.BookDao;
import business.cart.ShoppingCart;
import business.cart.ShoppingCartItem;
import business.customer.Customer;
import business.customer.CustomerDao;
import business.customer.CustomerForm;

import java.sql.Connection;
import java.util.Date;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultOrderService implements OrderService {

	private BookDao bookDao;
	private OrderDao orderDao;
	private LineItemDao lineItemDao;
	private CustomerDao customerDao;

	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setLineItemDao(LineItemDao lineItemDao) {
		this.lineItemDao = lineItemDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	public OrderDetails getOrderDetails(long orderId) {

		Order order = orderDao.findByOrderId(orderId);
		Customer customer = customerDao.findByCustomerId(order.customerId());
		List<LineItem> lineItems = lineItemDao.findByOrderId(orderId);
		List<Book> books = lineItems
				.stream()
				.map(lineItem -> bookDao.findByBookId(lineItem.bookId()))
				.toList();
		return new OrderDetails(order, customer, lineItems, books);
	}

	@Override
    public long placeOrder(CustomerForm customerForm, ShoppingCart cart) {

		validateCustomer(customerForm);
		validateCart(cart);try (Connection connection = JdbcUtils.getConnection()) {
			Date ccExpDate = getCardExpirationDate(
					customerForm.getCcExpiryMonth(),
					customerForm.getCcExpiryYear());
			return performPlaceOrderTransaction(
					customerForm.getName(),
					customerForm.getAddress(),
					customerForm.getPhone(),
					customerForm.getEmail(),
					customerForm.getCcNumber(),
					ccExpDate, cart, connection);
		} catch (SQLException e) {
			throw new BookstoreDbException("Error during close connection for customer order", e);
		}
	}

	private Date getCardExpirationDate(String monthString, String yearString) {
		//return new Date(1 - 2 -2025);
		// TODO Implement this correctly
		YearMonth expiry = YearMonth.of(Integer.parseInt(yearString), Integer.parseInt(monthString));
		return Date.from(expiry.atEndOfMonth().atTime(23,59,59).toInstant(ZoneOffset.UTC));
	}
	private long performPlaceOrderTransaction(
			String name, String address, String phone,
			String email, String ccNumber, Date date,
			ShoppingCart cart, Connection connection) {
		try {
			connection.setAutoCommit(false);
			long customerId = customerDao.create(
					connection, name, address, phone, email,
					ccNumber, date);
			long customerOrderId = orderDao.create(
					connection,
					cart.getComputedSubtotal() + cart.getSurcharge(),
					generateConfirmationNumber(), customerId);
			for (ShoppingCartItem item : cart.getItems()) {
				lineItemDao.create(connection, customerOrderId,
						item.getBookId(), item.getQuantity());
			}
			connection.commit();
			return customerOrderId;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new BookstoreDbException("Failed to roll back transaction", e1);
			}
			return 0;
		}
	}

	private int generateConfirmationNumber() {
		return ThreadLocalRandom.current().nextInt(999999999);
	}

	private void validateCustomer(CustomerForm customerForm) {

		// validate name: non-null, non-empty
		String name = customerForm.getName();
		if (nameIsInvalid(name)) {
			throw new ApiException.ValidationFailure("name", "Invalid name field.");
		}

		// validate address: non-null, non-empty, within 4 and 45 char
		String address = customerForm.getAddress();
		if (addressIsInvalid(address)) {
			throw new ApiException.ValidationFailure("address", "Invalid address field.");
		}

		// validate phone: non-null, non-empty, after removing all spaces, dashes, and
		// parens from the string it should have exactly 10 digits
		String phone = customerForm.getPhone();
		if (phoneIsInvalid(phone)==1) {
			throw new ApiException.ValidationFailure("phone", "Missing phone field.");
		} else if (phoneIsInvalid(phone)==2) {
			throw new ApiException.ValidationFailure("phone", "Invalid phone field.");
		}

		// validate email: non-null, non-empty, should not contain spaces; should
		// contain a "@"; and the last character should not be "."
		String email = customerForm.getEmail();
		if (emailIsInvalid(email)==1) {
			throw new ApiException.ValidationFailure("email", "Missing email field.");
		}
		if (emailIsInvalid(email)==2) {
			throw new ApiException.ValidationFailure("email", "Invalid email field.");
		}

		// validate credit card: non-null, non-empty, after remove spaces and dash, the
		// number of characters should be between 14 and 16
		String ccNumber = customerForm.getCcNumber();
		ccNumber = ccNumber.replaceAll("[\\s-]", "");
		if (ccIsInvalid(ccNumber)) {
			throw new ApiException.ValidationFailure("ccNumber", "Invalid credit card number field.");
		}

		// validate expiration date: non-null, non-empty, the month and year should be
		// the current month and year or later
		String ccExpiryMonth = customerForm.getCcExpiryMonth();
		String ccExpiryYear = customerForm.getCcExpiryYear();
		if (expiryDateIsInvalid(ccExpiryMonth, ccExpiryYear)==1) {
			throw new ApiException.ValidationFailure("expiryDate" , "Please enter a valid expiration date.");
		} else if (expiryDateIsInvalid(ccExpiryMonth, ccExpiryYear)==2) {
			throw new ApiException.ValidationFailure("expiryDate" , "Please enter a valid expiration format.");
		}
	}

	private void validateCart(ShoppingCart cart) {
		// cart should contain at least 1 item
		if (cart.getItems() == null || cart.getItems().equals("") || cart.getItems().size() <= 0) {
			throw new ApiException.ValidationFailure("Cart is empty.");
		}

		cart.getItems().forEach(item -> {
			// each cart item has quantity of books between 1 and 99
			if (item.getQuantity() < 1 || item.getQuantity() > 99) {
				throw new ApiException.ValidationFailure("Invalid quantity.");
			}

			// each cart item's price should match the price for the item from the database
			Book databaseBook = bookDao.findByBookId(item.getBookId());
			if (item.getBookForm().getPrice() != databaseBook.getPrice()) {
				throw new ApiException.ValidationFailure("Book price does not match.");
			}

			// each cart item's category should match the category for the item from the
			// database.
			if (item.getBookForm().getCategoryId() != databaseBook.getCategoryId()) {
				throw new ApiException.ValidationFailure("Book category Id does not match.");
			}
		});
	}

	private boolean nameIsInvalid(String name) {
		if (name == null || name.equals("") || name.length() > 45 || name.length() < 4) {
			return true;
		}
		return false;
	}

	private boolean addressIsInvalid(String address) {
		if (address == null || address.equals("") || address.length() > 45 || address.length() < 4) {
			return true;
		}
		return false;
	}

	private int phoneIsInvalid(String phone) {
		if (phone == null || phone.isEmpty()) {
			return 1;
		}
		phone = phone.replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
		if (phone.length() != 10) {
			return 2;
		}
		return 0;
	}

	private int emailIsInvalid(String email) {
		if (email == null || email.isEmpty()) {
			return 1;
		}
		if (!email.contains("@") || email.contains(" ") || email.endsWith(".")) {
			return 2;
		}
		return 0;
	}

	private boolean ccIsInvalid(String cc) {
		if (cc == null || cc.isEmpty()) {
			return true;
		}
		cc = cc.replace("-", "").replace(" ", "");
		if (cc.length() < 14 || cc.length() > 16) {
			return true;
		}
		return false;
	}

	private int expiryDateIsInvalid(String ccExpiryMonth, String ccExpiryYear) {
		//try {
			YearMonth currentYearMonth = YearMonth.now();
			YearMonth expiryYearMonth = YearMonth.of(Integer.parseInt(ccExpiryYear), Integer.parseInt(ccExpiryMonth));
			if (expiryYearMonth.isBefore(currentYearMonth)) {
				//throw new ApiException.ValidationFailure("expiryDate", "Invalid credit card expiry date.");
				return 1;
			}
//			return false;
//		} catch (NumberFormatException | NullPointerException | DateTimeException ex) {
//			throw new ApiException.ValidationFailure("expiryDate", "Invalid credit card expiry format.");
//		}
			else if (ccExpiryMonth.isEmpty() || ccExpiryMonth == null || ccExpiryYear.isEmpty() || ccExpiryYear==null) {
				return 2;
			}
			return 0;
	}

}
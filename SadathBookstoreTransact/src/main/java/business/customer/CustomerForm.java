package business.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */

public class CustomerForm {

    private String name;

	private String address;

    private String phone;

	private String email;

    private String ccNumber;

    private String ccExpiryMonth;

    private String ccExpiryYear;

    @JsonCreator
	public CustomerForm(
		@JsonProperty("name") String name,
		@JsonProperty("address") String address,
		@JsonProperty("phone") String phone,
		@JsonProperty("email") String email,
		@JsonProperty("ccNumber") String ccNumber,
		@JsonProperty("ccExpiryMonth") String ccExpiryMonth,
		@JsonProperty("ccExpiryYear") String ccExpiryYear) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.ccNumber = ccNumber;
		this.ccExpiryMonth = ccExpiryMonth;
		this.ccExpiryYear = ccExpiryYear;
	}

	public String getName() {
        return name;
    }

	public String getAddress() {
		return address;
	}

    public String getPhone() {
        return phone;
    }

	public String getEmail() {
		return email;
	}

	public String getCcNumber() {
        return ccNumber;
    }

	public String getCcExpiryMonth() {
		return ccExpiryMonth;
	}

	public String getCcExpiryYear() {
		return ccExpiryYear;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public void setCcExpiryMonth(String ccExpiryMonth) {
		this.ccExpiryMonth = ccExpiryMonth;
	}

	public void setCcExpiryYear(String ccExpiryYear) {
		this.ccExpiryYear = ccExpiryYear;
	}
}

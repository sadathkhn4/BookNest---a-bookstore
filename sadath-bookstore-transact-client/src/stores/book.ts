import { defineStore } from "pinia";
import type { BookItem } from "@/types";
import {apiUrl} from "@/api";
import {useCategoryStore} from "@/stores/category";

export const useBookStore = defineStore("book", {
    state: () => ({
        bookList: [] as BookItem[],
    }),
    actions: {
        async fetchBooks(categoryName: string) {
            const categoryStore = useCategoryStore()

            const selectedCategoryName =
                categoryStore.categoryList?.find((category) => category.name === categoryName)?.name ||
                categoryName

            try {
                const response = await fetch(`${apiUrl}/categories/name/${selectedCategoryName}/books`)

                if (!response.ok) {
                    throw new Error(
                        `Failed to fetch books for category '${categoryName}': ${response.status} ${response.statusText}`
                )
                }

                this.bookList = await response.json()
            } catch (e) {
                console.error(`Error fetching books for category '${categoryName}':`, e)

                // Propagate the error further, allowing the caller to handle or report it
                throw e
            }
        }

    },
    // getters
});
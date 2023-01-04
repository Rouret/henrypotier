package fr.imt.henrypotier

import android.content.Context
import com.google.gson.Gson
import fr.imt.henrypotier.data.Book


class BasketService {

    companion object {
        private var sharedPreferencesName = "basket"
        private var valueName = "cart"
        private var PRIVATE_MODE = 0
        fun getAllBooksInBasket(context: Context): List<Book> {
            val serializedObjects =
                context.getSharedPreferences(sharedPreferencesName, PRIVATE_MODE)
                    .getString(valueName, "[]")
            return Gson().fromJson(serializedObjects, Array<Book>::class.java).toList()
        }

        fun addBooksToBasket(context: Context, book: Book) {
            getAllBooksInBasket(context).let {
                if (!it.any { b -> b.isbn == book.isbn }) {
                    book.isInBasket = true
                    val newListOfBooks = it.plus(book)
                    update(context, newListOfBooks)
                }
            }
        }

        fun removeBookToBasket(context: Context, book: Book) {
            getAllBooksInBasket(context).let {
                if (it.any { b -> b.isbn == book.isbn }) {
                    //it remove book to the list
                    val newListOfBooks = it.filter { b -> b.isbn != book.isbn }
                    update(context, newListOfBooks)
                }
            }
        }

        fun update(context: Context, newListOfBooks: List<Book>) {
            context.getSharedPreferences(sharedPreferencesName, PRIVATE_MODE).edit()
                .putString(valueName, Gson().toJson(newListOfBooks)).apply()
        }

        fun isBookIsInBasket(context: Context, bookToCheck: Book): Boolean {
            getAllBooksInBasket(context).let {
                return it.find { b -> b.isbn == bookToCheck.isbn } != null
            }
        }
    }


}
package fr.imt.henrypotier

import android.content.Context
import com.google.gson.Gson
import fr.imt.henrypotier.data.Book
import fr.imt.henrypotier.data.BasketBook
import fr.imt.henrypotier.data.CommercialOffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BasketService {

    companion object {
        private var sharedPreferencesName = "basket"
        private var valueName = "cart"
        private var PRIVATE_MODE = 0

        fun getAllBooksInBasket(context: Context): List<BasketBook> {
            val serializedObjects =
                context.getSharedPreferences(sharedPreferencesName, PRIVATE_MODE)
                    .getString(valueName, "[]")
            return Gson().fromJson(serializedObjects, Array<BasketBook>::class.java).toList()
        }

        fun addBooksToBasket(context: Context, book: Book) {
            getAllBooksInBasket(context).let {
                val cartBook = it.find { cartBook -> cartBook.isbn == book.isbn }
                if (cartBook != null) {
                    cartBook.quantity += 1
                    val cart = it.filter { aCartBook -> aCartBook.isbn != book.isbn }
                    saveBooksInBasket(context,  cart.plus(cartBook))
                } else {
                    saveBooksInBasket(context, it.plus(book.factoryCartBook()))
                }
            }
        }


        fun removeQuantityToBasketBook(context: Context, book: BasketBook) {
            getAllBooksInBasket(context).let {
                val cartBook = it.find { cartBook -> cartBook.isbn == book.isbn }
                if (cartBook != null) {
                    cartBook.quantity -= 1
                    val cart = it.filter { aCartBook -> aCartBook.isbn != book.isbn }
                    saveBooksInBasket(context,  cart.plus(cartBook))
                }
            }
        }

        fun removeBookToBasket(context: Context, book: Book) {
            getAllBooksInBasket(context).let {
                if (it.any { b -> b.isbn == book.isbn }) {
                    //it remove book to the list
                    val newListOfBooks = it.filter { b -> b.isbn != book.isbn }
                    saveBooksInBasket(context, newListOfBooks)
                }
            }
        }

        fun saveBooksInBasket(context: Context, newListOfBooks: List<BasketBook>) {
            context.getSharedPreferences(sharedPreferencesName, PRIVATE_MODE).edit()
                .putString(valueName, Gson().toJson(newListOfBooks)).apply()
        }

        fun getNbBookInBasket(context: Context, bookToCheck: Book): Int {
            getAllBooksInBasket(context).let {
               return it.find { b -> b.isbn == bookToCheck.isbn }?.quantity ?: 0
            }
        }

        fun getTotalPrice(context: Context): Double {
            var totalPrice = 0.0
            getAllBooksInBasket(context).forEach { book ->
                totalPrice += (book.price * book.quantity)
            }
            return totalPrice
        }

        private fun getISBNs(context: Context): List<String> {
            val listISBN = mutableListOf<String>()
            getAllBooksInBasket(context).forEach { book ->
                for (i in 1..book.quantity) {
                    listISBN.add(book.isbn)
                }
            }
            return listISBN
        }

        private fun getISBNsAsString(context: Context): String {
            return getISBNs(context).joinToString(",")
        }

        suspend fun getCommercialOffers(context: Context): List<CommercialOffer> {
            val retrofit = getRetrofit()
            val service = retrofit.create(BookService::class.java)
            val commercialOffersRequest = service.getCommercialOffers(getISBNsAsString(context))
            return commercialOffersRequest.offers
        }

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
                .addConverterFactory(GsonConverterFactory.create()).build()
        }

        fun removeAllBooks(context: Context) {
            context.getSharedPreferences(sharedPreferencesName, PRIVATE_MODE).edit()
                .putString(valueName, "[]").apply()
        }
    }


}
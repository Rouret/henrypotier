package fr.imt.henrypotier

import android.content.Context
import com.google.gson.Gson
import fr.imt.henrypotier.data.Book


class BasketService {

    companion object {
        var sharedPreferencesName = "basket"
        var valueName = "cart"
        private var PRIVATE_MODE = 0
        fun getAllBooksInBasket(context: Context): List<Book> {
            val serializedObjects = context.getSharedPreferences(sharedPreferencesName, PRIVATE_MODE)
                .getString(valueName, "[]")
            return Gson().fromJson(serializedObjects, Array<Book>::class.java).toList()
        }
        fun addBooksToBasket(context:Context, book: Book) {
            getAllBooksInBasket(context).let {
                if(!it.contains(book)){
                    var newListOfBooks = it.plus(book)
                    context.getSharedPreferences(sharedPreferencesName, PRIVATE_MODE)
                        .edit()
                        .putString(valueName, Gson().toJson(newListOfBooks))
                        .apply()
                }
            }
        }
    }


}
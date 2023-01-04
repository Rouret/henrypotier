package fr.imt.henrypotier.bookList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.imt.henrypotier.BookViewModel

class BasketViewModel(val dataSource: BookViewModel) : ViewModel() {

    val booksLiveData = dataSource.getBookList()

    /* If the name and description are present, create new Book and add it to the datasource */
    fun insertBook(flowerName: String?, flowerDescription: String?) {
        //if (flowerName == null || flowerDescription == null) {
        //    return
        //}
//
        //val image = dataSource.getRandomBookImageAsset()
        //val newBook = Book(
        //    Random.nextLong(),
        //    flowerName,
        //    image,
        //    flowerDescription
        //)
//
        //dataSource.addBook(newBook)
    }
}

class BasketViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BasketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return BasketViewModel(
                dataSource = BookViewModel.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
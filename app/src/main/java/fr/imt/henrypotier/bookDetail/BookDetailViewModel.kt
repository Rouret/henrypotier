package fr.imt.henrypotier.bookDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.imt.henrypotier.BookViewModel
import fr.imt.henrypotier.data.Book

class BookDetailViewModel(private val datasource: BookViewModel) : ViewModel() {

    /* Queries datasource to returns a book that corresponds to an id. */
    //fun getBookForId(id: Long) : Book? {
    //    //return datasource.getBookForId(id)
    //}

    /* Queries datasource to remove a book. */
    fun removeBook(book: Book) {
        //datasource.removeBook(book)
    }
}

class BookDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDetailViewModel(
                datasource = BookViewModel.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
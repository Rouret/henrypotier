package fr.imt.henrypotier.bookList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.imt.henrypotier.BookViewModel

class BooksListViewModel(val dataSource: BookViewModel) : ViewModel() {

    val booksLiveData = dataSource.getBookList()
}

class BooksListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BooksListViewModel(
                dataSource = BookViewModel.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
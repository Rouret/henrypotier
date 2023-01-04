package fr.imt.henrypotier.bookDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.imt.henrypotier.BookViewModel
import fr.imt.henrypotier.data.Book

class BookDetailViewModel(private val datasource: BookViewModel) : ViewModel() {

    fun getBookForId(id: String): Book? {
        return datasource.getBookForId(id)
    }
}

class BookDetailViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return BookDetailViewModel(
                datasource = BookViewModel.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
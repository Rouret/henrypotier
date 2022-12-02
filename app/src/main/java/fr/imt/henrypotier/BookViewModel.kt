package fr.imt.henrypotier;

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.imt.henrypotier.data.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookViewModel : ViewModel() {
    val state = MutableLiveData<BookState>()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun loadBooks() {
        val service: BookService = getRetrofit().create(BookService::class.java)

        state.postValue(BookState(emptyList(), true))

        viewModelScope.launch(context = Dispatchers.Main) {
            val books = withContext(Dispatchers.IO) {
                service.listBooks()
            }
            state.postValue(BookState(books, false))
        }
    }

    fun getBookList() : LiveData<BookState> {
        return state
    }

    companion object {
        private var INSTANCE: BookViewModel? = null

        fun getDataSource(): BookViewModel {
            return synchronized(BookViewModel::class) {
                val newInstance = INSTANCE ?: BookViewModel()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}
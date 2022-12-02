package fr.imt.henrypotier;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookViewModel : ViewModel() {
    val state = MutableLiveData<BookState>()

    fun getRetrofite(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun loadBooks() {
        val service: BookService = getRetrofite().create(BookService::class.java)

        state.postValue(BookState(emptyList(), true))

        viewModelScope.launch(context = Dispatchers.Main) {
            val books = withContext(Dispatchers.IO) {
                service.listBooks()
            }
            state.postValue(BookState(books, false))
        }
    }
}
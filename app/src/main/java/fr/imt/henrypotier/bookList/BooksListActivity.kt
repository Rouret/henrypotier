package fr.imt.henrypotier.bookList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.BookService
import fr.imt.henrypotier.R
import fr.imt.henrypotier.basket.BasketActivity
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.data.Book
import fr.imt.henrypotier.data.CommercialOffer
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BOOK_ID = "book id"

class BooksListActivity : AppCompatActivity() {

    private lateinit var booksAdapter: BooksAdapter

    private val booksListViewModel by viewModels<BooksListViewModel> {
        BooksListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        booksAdapter = BooksAdapter { book -> adapterOnClick(book) }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        BasketService.update(this, ArrayList<Book>())

        booksListViewModel.booksLiveData.observe(this) {
            it?.let {
                if(!it.isLoading && it.books.isNotEmpty() && booksAdapter.currentList != it.books){
                    var basket = BasketService.getAllBooksInBasket(this);
                    //filter the cart
                    var newCart = basket.filter { book -> !it.books.contains(book) }
                    if(newCart.size != basket.size){
                        BasketService.update(this, newCart)
                    }
                    it.books.map {
                        it.isInBasket = newCart.find { book -> book.isbn == it.isbn } != null
                    }
                    //filter the result
                    booksAdapter.submitList(it.books)
                }
            }
                booksAdapter.submitList(it.books)
            }


        booksListViewModel.dataSource.state.observe(this) { state ->
            Toast.makeText(
                this@BooksListActivity,
                "${state.books.size} books | isLoading ${state.isLoading}",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        booksListViewModel.dataSource.loadBooks()

        val basketButton: View = findViewById(R.id.basket_button)
        basketButton.setOnClickListener {
            basketButtonOnClick()
        }
    }

    override fun onResume() {
        super.onResume()
        booksListViewModel.dataSource.loadBooks()
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    // onClick on button basket go to basket
    private fun basketButtonOnClick() {
        val intent = Intent(this@BooksListActivity, BasketActivity::class.java)
        startActivity(intent)
    }

    /* Opens BookDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(book: Book) {
        val intent = Intent(this, BookDetailActivity()::class.java)
        intent.putExtra(BOOK_ID, book.isbn)
        startActivity(intent)
    }
}
package fr.imt.henrypotier.bookList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.R
import fr.imt.henrypotier.basket.BasketActivity
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.data.Book


const val BOOK_ID = "book id"

class BooksListActivity : AppCompatActivity() {

    private lateinit var booksAdapter: BooksAdapter

    private val booksListViewModel by viewModels<BooksListViewModel> {
        BooksListViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        booksAdapter = BooksAdapter { book -> adapterOnClick(book) }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        BasketService.saveBooksInBasket(this, ArrayList())

        //TODO lorsqu'on a la liste des livres on check ce qui sont déjà dans le panier et leur quantité
        booksListViewModel.booksLiveData.observe(this) { it ->
            if (!it.isLoading && it.books.isNotEmpty() && booksAdapter.currentList != it.books) {
                val basket = BasketService.getAllBooksInBasket(this)
                //filter the cart
                val newCart = basket.filter { book -> !it.books.contains(book) }
                if (newCart.size != basket.size) {
                    BasketService.saveBooksInBasket(this, newCart)
                }
                it.books.map {
                    it.isInBasket = newCart.find { book -> book.isbn == it.isbn } != null
                }
                //filter the result
                booksAdapter.submitList(it.books)
            }

            booksAdapter.submitList(it.books)
        }

        //TODO circular progress ?
        booksListViewModel.dataSource.state.observe(this) { state ->
            Toast.makeText(
                this@BooksListActivity,
                "${state.books.size} books | isLoading ${state.isLoading}",
                Toast.LENGTH_SHORT
            ).show()
        }

        booksListViewModel.dataSource.loadBooks()

        val basketButton: View = findViewById(R.id.basket_button)
        basketButton.setOnClickListener {
            basketButtonOnClick()
        }
    }

    override fun onResume() {
        super.onResume()
        //TODO changer dynmaiquement la valeur des livres
        booksListViewModel.dataSource.loadBooks()
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
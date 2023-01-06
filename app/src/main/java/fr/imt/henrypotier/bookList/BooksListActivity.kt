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
    private lateinit var booksAdapterLeft: BooksAdapter
    private lateinit var booksAdapterRight: BooksAdapter
    private val isPortrait: Boolean by lazy { resources.getBoolean(R.bool.is_portrait) }

    private val booksListViewModel by viewModels<BooksListViewModel> {
        BooksListViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        booksAdapter = BooksAdapter { book -> adapterOnClick(book) }
        booksAdapterLeft = BooksAdapter { book -> adapterOnClick(book) }
        booksAdapterRight = BooksAdapter { book -> adapterOnClick(book) }
        if(isPortrait) {
            val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
            recyclerView.adapter = booksAdapter
        } else {
            // put half of the books in the left recycler view
            val recyclerViewLeft: RecyclerView = findViewById(R.id.recycler_view_left)
            recyclerViewLeft.adapter = booksAdapterLeft
            // put the other half in the right recycler view
            val recyclerViewRight: RecyclerView = findViewById(R.id.recycler_view_right)
            recyclerViewRight.adapter = booksAdapterRight
        }

        BasketService.saveBooksInBasket(this, ArrayList())

        booksListViewModel.booksLiveData.observe(this) { it ->
            if (!it.isLoading && it.books.isNotEmpty() && booksAdapter.currentList != it.books) {
                val basket = BasketService.getAllBooksInBasket(this)
                //filter the cart
                val newCart = basket.filter { book -> !it.books.contains(book) }
                if (newCart.size != basket.size) {
                    BasketService.saveBooksInBasket(this, newCart)
                }

                if(!isPortrait) {
                    // split the list in two
                    // half arrondi au supeieur
                    val half = (it.books.size + 1) / 2
                    booksAdapterRight.submitList(it.books.subList(half, it.books.size))
                    booksAdapterLeft.submitList(it.books.subList(0, half))
                } else {
                    booksAdapter.submitList(it.books)
                }
            }
        }

        booksListViewModel.dataSource.state.observe(this) { state ->
            if(state.isLoading){
                Toast.makeText(
                    this@BooksListActivity,
                    "Chargement ...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        booksListViewModel.dataSource.loadBooks()

        val basketButton: View = findViewById(R.id.basket_button)
        basketButton.setOnClickListener {
            basketButtonOnClick()
        }
    }

    override fun onResume() {
        super.onResume()
        booksAdapter.notifyDataSetChanged()
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
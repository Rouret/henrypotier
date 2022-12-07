package fr.imt.henrypotier.basket

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.R
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.bookList.BOOK_ID
import fr.imt.henrypotier.bookList.BasketViewModel
import fr.imt.henrypotier.bookList.BasketViewModelFactory
import fr.imt.henrypotier.bookList.BooksAdapter
import fr.imt.henrypotier.data.Book
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class BasketActivity : AppCompatActivity() {
    private val newBookActivityRequestCode = 1
    private lateinit var realm: Realm
    private val basketViewModel by viewModels<BasketViewModel> {
        BasketViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        /* Instantiates headerAdapter and BooksAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val booksAdapter = BooksAdapter { book -> adapterOnClick(book) }
        val config = RealmConfiguration.Builder(schema = setOf())
            .build()
        this.realm = Realm.open(config)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        basketViewModel.booksLiveData.observe(this) {
            it?.let {
                booksAdapter.submitList(it.books)
            }
        }

        basketViewModel.dataSource.state.observe(this) { state ->
            Toast.makeText(
                this@BasketActivity,
                "${state.books.size} books | isLoading ${state.isLoading}",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        basketViewModel.dataSource.loadBooks();
    }

    override fun onDestroy() {
        super.onDestroy()
        this.realm.close();
    }

    /* Opens BookDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(book: Book) {
        val intent = Intent(this, BookDetailActivity()::class.java)
        intent.putExtra(BOOK_ID, book.isbn)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        //TODO POUR LE CART ?
        /*
        /* Inserts book into viewModel. */
        if (requestCode == newBookActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val bookTitle = data.getStringExtra()
                val bookSynopsis = data.getStringExtra("BOOK_SYNOPSIS")

                //BooksListViewModel.insertBook(bookTitle, bookSynopsis)
            }
        }

         */
    }
}
package fr.imt.henrypotier.basket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.R
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.bookList.BOOK_ID
import fr.imt.henrypotier.bookList.BooksAdapter
import fr.imt.henrypotier.data.Book

class BasketActivity : AppCompatActivity() {

    private lateinit var booksAdapter: BooksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        booksAdapter = BooksAdapter { book -> adapterOnClick(book) }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        BasketService.getAllBooksInBasket(this).let {
            booksAdapter.submitList(it)
        }
    }


    override fun onResume() {
        super.onResume()
        booksAdapter.notifyDataSetChanged();
    }
    /* Opens BookDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(book: Book) {
        val intent = Intent(this, BookDetailActivity()::class.java)
        intent.putExtra(BOOK_ID, book.isbn)
        startActivity(intent)
    }

}
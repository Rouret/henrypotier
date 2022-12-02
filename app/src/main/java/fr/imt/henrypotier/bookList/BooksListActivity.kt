package fr.imt.henrypotier.bookList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.R
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.data.Book

const val BOOK_ID = "book id"

class BooksListActivity : AppCompatActivity() {
    private val newBookActivityRequestCode = 1
    private val booksListViewModel by viewModels<BooksListViewModel> {
        BooksListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        /* Instantiates headerAdapter and BooksAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val booksAdapter = BooksAdapter { book -> adapterOnClick(book) }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        booksListViewModel.booksLiveData.observe(this) {
            it?.let {
                booksAdapter.submitList(it.books)
            }
        }

        //val fab: View = findViewById(R.id.fab)
        //fab.setOnClickListener {
        //    fabOnClick()
        //}
    }

    /* Opens BookDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(book: Book) {
        val intent = Intent(this, BookDetailActivity()::class.java)
        intent.putExtra(BOOK_ID, book.isbn)
        startActivity(intent)
    }

    /* Adds book to bookList when FAB is clicked. */
    //private fun fabOnClick() {
    //    val intent = Intent(this, AddBookActivity::class.java)
    //    startActivityForResult(intent, newBookActivityRequestCode)
    //}

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        /* Inserts book into viewModel. */
        if (requestCode == newBookActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val bookTitle = data.getStringExtra("BOOK_TITLE") // TODO enlever les ""
                val bookSynopsis = data.getStringExtra("BOOK_SYNOPSIS") // TODO enlever les ""

                //BooksListViewModel.insertBook(bookTitle, bookSynopsis)
            }
        }
    }
}
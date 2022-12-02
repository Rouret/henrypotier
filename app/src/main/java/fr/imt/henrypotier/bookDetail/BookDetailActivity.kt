package fr.imt.henrypotier.bookDetail

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import fr.imt.henrypotier.R
import fr.imt.henrypotier.bookList.BOOK_ID

class BookDetailActivity : AppCompatActivity() {

    private val bookDetailViewModel by viewModels<BookDetailViewModel> {
        BookDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        var currentBookId: Long? = null

        /* Connect variables to UI elements. */
        val bookName: TextView = findViewById(R.id.book_detail_name)
        val bookImage: ImageView = findViewById(R.id.book_detail_image)
        val bookDescription: TextView = findViewById(R.id.book_detail_description)
        val removeBookButton: Button = findViewById(R.id.remove_button)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentBookId = bundle.getLong(BOOK_ID)
        }

        /* If currentBookId is not null, get corresponding book and set name, image and
        description */
        //currentBookId?.let {
        //    val currentBook = bookDetailViewModel.getBookForId(it)
        //    bookName.text = currentBook?.name
        //    bookImage.setImageResource(currentBook.image)
        //    bookDescription.text = currentBook?.description
//
        //    removeBookButton.setOnClickListener {
        //        if (currentBook != null) {
        //            bookDetailViewModel.removeBook(currentBook)
        //        }
        //        finish()
        //    }
        //}

    }
}
package fr.imt.henrypotier.bookDetail

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.R
import fr.imt.henrypotier.bookList.BOOK_ID
import fr.imt.henrypotier.data.Book

class BookDetailActivity : AppCompatActivity() {

    private val bookDetailViewModel by viewModels<BookDetailViewModel> {
        BookDetailViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        var currentBookId: String?

        //get the book id from the intent
        val intent = intent
        if (!intent.hasExtra(BOOK_ID)) {
            finish()
        }

        currentBookId = intent.getStringExtra(BOOK_ID)

        //get the book from the view model
        val book: Book? = currentBookId?.let { bookDetailViewModel.getBookForId(it) }


        /* Connect variables to UI elements. */
        val bookName: TextView = findViewById(R.id.book_detail_title)
        val bookImage: ImageView = findViewById(R.id.book_detail_image)
        val bookDescription: TextView = findViewById(R.id.book_detail_description)
        val bookPrice: TextView = findViewById(R.id.book_detail_price)
        val addToCartButton: Button = findViewById(R.id.book_detail_add_to_cart)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentBookId = bundle.getString(BOOK_ID)
        }

        //set the book data to the UI elements
        bookName.text = book?.title
        bookDescription.text = book?.synopsis?.joinToString(separator = "") { it }
        bookPrice.text = String.format(book?.price.toString() + "€")
        Glide.with(this).load(book?.cover).into(bookImage)
        addToCartButton.setOnClickListener {
            if (book != null) {
                BasketService.addBooksToBasket(context = this, book)
                Toast.makeText(
                    this@BookDetailActivity,
                    "Ajouté au panier",
                    Toast.LENGTH_SHORT
                ).show()
            };
        }





    }
}
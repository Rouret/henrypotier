package fr.imt.henrypotier.bookList

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.data.Book
import fr.imt.henrypotier.R

class BooksAdapter(private val onClick: (Book) -> Unit) :
    ListAdapter<Book, BooksAdapter.BookViewHolder>(BookDiffCallback) {

    /* ViewHolder for Book, takes in the inflated view and the onClick behavior. */
    class BookViewHolder(itemView: View, val onClick: (Book) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val bookImageView: ImageView = itemView.findViewById(R.id.book_image)
        private val bookTextView: TextView = itemView.findViewById(R.id.book_title)
        private val bookPriceView: TextView = itemView.findViewById(R.id.book_price)
        private val basketButton : Button = itemView.findViewById(R.id.item_basket_button)
        private var currentBook: Book? = null

        init {
            itemView.setOnClickListener {
                currentBook?.let {
                    onClick(it)
                }
            }
        }

        /* Bind book name and image. */
        fun bind(book: Book) {
            currentBook = book

            bookTextView.text = book.title
            val cover = book.cover
            val uri = Uri.parse(cover)
            Glide.with(itemView.context)
                .load(uri)
                .into(bookImageView)

            bookPriceView.text = book.price.toString() + "€" // TODO

            if(book.isInBasket){
                basketButton.text = "-"
                basketButton.setOnClickListener {
                    BasketService.removeBookToBasket(itemView.context, book)
                    book.isInBasket = false
                }
            } else {
                basketButton.text = "+"
                basketButton.setOnClickListener {
                    book.isInBasket = true
                    BasketService.addBooksToBasket(itemView.context, book)
                    bindingAdapter?.notifyDataSetChanged()
                }
            }

        }
    }

    /* Creates and inflates view and return BookViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view, onClick)
    }

    /* Gets current Book and uses it to bind view. */
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)

    }
}

object BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.isbn == newItem.isbn
    }
}
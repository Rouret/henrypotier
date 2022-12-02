package fr.imt.henrypotier.bookList

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

            bookPriceView.text = "$book.price + €" // TODO

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
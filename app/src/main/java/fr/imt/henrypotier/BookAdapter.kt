package fr.imt.henrypotier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
//import com.example.recyclersample.R
//import com.example.recyclersample.data.Book

class BookAdapter(private val onClick: (Book) -> Unit) :
    ListAdapter<Book, BooksAdapter.BookViewHolder>(BookDiffCallback) {

    /* ViewHolder for Book, takes in the inflated view and the onClick behavior. */
    class BookViewHolder(itemView: View, val onClick: (Book) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val bookTextView: TextView = itemView.findViewById(R.id.Book_text)
        private val bookImageView: ImageView = itemView.findViewById(R.id.Book_image)
        private var currentBook: Book? = null

        init {
            itemView.setOnClickListener {
                currentBook?.let {
                    onClick(it)
                }
            }
        }

        /* Bind Book name and image. */
        fun bind(Book: Book) {
            currentBook = Book

            BookTextView.text = Book.name
            if (Book.image != null) {
                BookImageView.setImageResource(Book.image)
            } else {
                BookImageView.setImageResource(R.drawable.rose)
            }
        }
    }

    /* Creates and inflates view and return BookViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.Book_item, parent, false)
        return BookViewHolder(view, onClick)
    }

    /* Gets current Book and uses it to bind view. */
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val Book = getItem(position)
        holder.bind(Book)

    }
}

object BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }
}
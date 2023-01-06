package fr.imt.henrypotier.bookList

import android.net.Uri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.R
import fr.imt.henrypotier.data.Book

class BooksAdapter(private val onClick: (Book) -> Unit) :
    ListAdapter<Book, BooksAdapter.BookViewHolder>(BookDiffCallback) {

    /* ViewHolder for Book, takes in the inflated view and the onClick behavior. */
    class BookViewHolder(itemView: View, val onClick: (Book) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val bookImageView: ImageView = itemView.findViewById(R.id.book_image)
        private val bookTextView: TextView = itemView.findViewById(R.id.book_title)
        private val bookPriceView: TextView = itemView.findViewById(R.id.book_price)
        private val basketButton: Button = itemView.findViewById(R.id.item_basket_button)
        private val nbInCartTextView : TextView = itemView.findViewById(R.id.nb_in_cart)
        private var currentBook: Book? = null
        //TODO Modifier pour avoir la ligne "x dans le panier" si le livre est déjà dans le panier
        init {
            itemView.setOnClickListener {
                currentBook?.let {
                    onClick(it)
                }
            }
        }

        /* Bind book name and image. */
        fun bind(book: Book) {
            //Si le livre est déjà dans le panier on affiche le bouton "dans le panier" avec sa quantité
            val nbInCart = BasketService.getNbBookInBasket( itemView.context,book)
            if(nbInCart > 0) {
                nbInCartTextView.visibility = View.VISIBLE
                nbInCartTextView.text = itemView.context.getString(R.string.nb_in_basket, nbInCart.toString())
            }
            else {
                nbInCartTextView.visibility = View.GONE
            }
            //Init des autres éléments
            bookTextView.text = book.title
            val cover = book.cover
            val uri = Uri.parse(cover)
            Glide.with(itemView.context).load(uri).into(bookImageView)
            bookPriceView.text = String.format(book.price.toString() + "€")
            //Ajout du bouton "dans le panier"
            basketButton.text = itemView.context.getString(R.string.basket_add)
            basketButton.setOnClickListener {
                BasketService.addBooksToBasket(itemView.context, book)
                bindingAdapter?.notifyDataSetChanged()
            }

            currentBook = book
        }
    }

    /* Creates and inflates view and return BookViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
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
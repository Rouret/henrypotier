package fr.imt.henrypotier.basket

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
import fr.imt.henrypotier.data.BasketBook


class BasketBooksAdapter(private val onClick: (BasketBook) -> Unit) :
    ListAdapter<BasketBook, BasketBooksAdapter.BasketBookViewHolder>(BasketBookDiffCallback) {

    /* ViewHolder for Book, takes in the inflated view and the onClick behavior. */
    class BasketBookViewHolder(itemView: View, val onClick: (BasketBook) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val bookImageView: ImageView = itemView.findViewById(R.id.book_image)
        private val bookTextView: TextView = itemView.findViewById(R.id.book_title)
        private val bookPriceView: TextView = itemView.findViewById(R.id.book_price)
        private val quantitiesTextView: TextView = itemView.findViewById(R.id.quantites)
        private val addQuantitiesButton: Button = itemView.findViewById(R.id.add_quantities)
        private val removeQuantitiesButton: Button = itemView.findViewById(R.id.remove_quantities)
        private var currentBook: BasketBook? = null

        init {
            itemView.setOnClickListener {
                currentBook?.let {
                    onClick(it)
                }
            }
        }

        /* Bind book name and image. */
        fun bind(book: BasketBook) {
            //Init des autres éléments
            bookTextView.text = book.title
            val cover = book.cover
            val uri = Uri.parse(cover)
            Glide.with(itemView.context).load(uri).into(bookImageView)
            bookPriceView.text = String.format(book.price.toString() + "€")
            currentBook = book
            quantitiesTextView.text = book.quantity.toString()

            addQuantitiesButton.setOnClickListener {
                BasketService.addBooksToBasket(itemView.context, book);
                book.quantity += 1
                quantitiesTextView.text = book.quantity.toString()
                bindingAdapter?.notifyDataSetChanged()
            }

            removeQuantitiesButton.setOnClickListener {
                if(book.quantity == 1) {
                    book.quantity = 0
                    BasketService.removeBookToBasket(itemView.context, book);
                    bindingAdapter?.notifyItemRemoved(adapterPosition)
                }else if(book.quantity == 0){
                    book.quantity = 1
                    BasketService.addBooksToBasket(itemView.context, book);
                }else{
                    BasketService.removeQuantityToBasketBook(itemView.context, book);
                    book.quantity -= 1
                }
                quantitiesTextView.text = book.quantity.toString()
                bindingAdapter?.notifyDataSetChanged()
            }
        }
    }

    /* Creates and inflates view and return BookViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketBooksAdapter.BasketBookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.basket_book_item, parent, false)
        return BasketBookViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: BasketBookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }
}

object BasketBookDiffCallback : DiffUtil.ItemCallback<BasketBook>() {
    override fun areItemsTheSame(oldItem: BasketBook, newItem: BasketBook): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BasketBook, newItem: BasketBook): Boolean {
        return oldItem.isbn == newItem.isbn
    }
}
package fr.imt.henrypotier.basket

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.R
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.bookList.BOOK_ID
import fr.imt.henrypotier.data.BasketBook
import fr.imt.henrypotier.data.CommercialOffer
import kotlinx.coroutines.runBlocking

class BasketActivity : AppCompatActivity() {

    private lateinit var booksAdapter: BasketBooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        booksAdapter = BasketBooksAdapter { book -> adapterOnClick(book) }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        BasketService.getAllBooksInBasket(this).let {
            booksAdapter.submitList(it)

            if (it.isNotEmpty()) {
                runBlocking {
                    calculateTotal()
                }
            }
        }
    }

    private suspend fun calculateTotal() {
        val totalBeforeDiscount = BasketService.getTotalPrice(this)
        val allOffers = BasketService.getCommercialOffers(this)
        val discount = CommercialOffer.bestOffer(allOffers, totalBeforeDiscount)
        val totalAfterDiscount = totalBeforeDiscount - discount!!.value

        setViewAfterCalculateTotal(totalBeforeDiscount, totalAfterDiscount, discount)
    }


    override fun onResume() {
        super.onResume()
        booksAdapter.notifyDataSetChanged()
    }

    /* Opens BookDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(book: BasketBook) {
        val intent = Intent(this, BookDetailActivity()::class.java)
        intent.putExtra(BOOK_ID, book.isbn)
        startActivity(intent)
    }

    private fun setViewAfterCalculateTotal(totalBeforeDiscount: Double, totalAfterDiscount: Double, discount: CommercialOffer) {
        val totalBeforeDiscountView: TextView = findViewById(R.id.basket_total_before_discount)
        totalBeforeDiscountView.text = String.format(
            getString(R.string.basket_total_before_discount), totalBeforeDiscount.toString()
        )
        val totalAfterDiscountView: TextView = findViewById(R.id.basket_total_after_discount)
        totalAfterDiscountView.text = String.format(
            getString(R.string.basket_total_after_discount), totalAfterDiscount.toString()
        )
        val discountView: TextView = findViewById(R.id.basket_discount)
        discountView.text =
            String.format(getString(R.string.basket_discount), discount.value.toString())

        val basketEmptyView: TextView = findViewById(R.id.basket_empty)
        basketEmptyView.text = ""
    }

}
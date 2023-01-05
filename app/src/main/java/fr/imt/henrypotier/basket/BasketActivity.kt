package fr.imt.henrypotier.basket

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.BookService
import fr.imt.henrypotier.R
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.bookList.BOOK_ID
import fr.imt.henrypotier.bookList.BooksAdapter
import fr.imt.henrypotier.data.Book
import fr.imt.henrypotier.data.CommercialOffer
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BasketActivity : AppCompatActivity() {

    private lateinit var booksAdapter: BooksAdapter
    private val listISBN = ArrayList<String>()
    private val listPrice = ArrayList<Int>()
    private val urlPromoStart = "https://henri-potier.techx.fr/books/"
    private val urlPromoEnd = "/commercialOffers"
    private var discount: CommercialOffer? = null
    private var totalBeforeDiscount: Double = 0.0
    private var totalAfterDiscount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        booksAdapter = BooksAdapter { book -> adapterOnClick(book) }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        BasketService.getAllBooksInBasket(this).let {
            booksAdapter.submitList(it)

            println("Liste de livres")
            println(it)
            for (book in it) {
                listISBN.add(book.isbn)
                listPrice.add(book.price)
            }

            testAffichage()
            if (it.isNotEmpty()) {
                runBlocking {
                    calculateTotal()
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun testAffichage() {
        println("Liste d'ISBN")
        println(listISBN)
        println("Liste de prix")
        println(listPrice)
        val urlPromo = urlPromoStart + listISBN.joinToString(separator = ",") + urlPromoEnd
        println("URL promo")
        println(urlPromo)
    }

    private suspend fun calculateTotal() {
        println("Calcul du total")
        // total in listprice
        for (price in listPrice) {
            totalBeforeDiscount += price
        }

        val retrofit = getRetrofit()
        val service = retrofit.create(BookService::class.java)
        println("Liste d'ISBN AAAAAA")
        println(listISBN)
        val commercialOffersRequest = service.getCommercialOffers(listISBN.joinToString(","))
        println("CommercialOffersRequest")
        println(commercialOffersRequest)
        val allOffers = commercialOffersRequest.offers
        println("All offers")
        println(allOffers)
        discount = CommercialOffer.bestOffer(allOffers, totalBeforeDiscount)
        println("Réduction")
        println(discount)
        println("totalBeforeDiscount")
        println(totalBeforeDiscount)
        totalAfterDiscount = totalBeforeDiscount - discount!!.value
        println("totalAfterDiscount")
        println(totalAfterDiscount)

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
            String.format(getString(R.string.basket_discount), discount!!.value.toString())

        val basketEmptyView: TextView = findViewById(R.id.basket_empty)
        basketEmptyView.text = ""
    }


    override fun onResume() {
        super.onResume()
        booksAdapter.notifyDataSetChanged()
    }

    /* Opens BookDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(book: Book) {
        val intent = Intent(this, BookDetailActivity()::class.java)
        intent.putExtra(BOOK_ID, book.isbn)
        startActivity(intent)
    }

}
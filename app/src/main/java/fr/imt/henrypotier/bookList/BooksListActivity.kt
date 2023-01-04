package fr.imt.henrypotier.bookList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.BasketService
import fr.imt.henrypotier.R
import fr.imt.henrypotier.basket.BasketActivity
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.data.Book
import fr.imt.henrypotier.data.CommercialOffer
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BOOK_ID = "book id"

class BooksListActivity : AppCompatActivity() {

    private lateinit var booksAdapter: BooksAdapter

    private val booksListViewModel by viewModels<BooksListViewModel> {
        BooksListViewModelFactory(this)
    }
    private var percentagePromo = Int.MAX_VALUE;
    private var minusPromo = Int.MAX_VALUE;
    private var slicePromo = Int.MAX_VALUE;
    private val listISBN = ArrayList<String>()
    private val listPrice = ArrayList<Int>()
    private val urlPromoStart = "https://henri-potier.techx.fr/books/"
    private val urlPromoEnd = "/commercialOffers"
    private var commercialOffers = ArrayList<CommercialOffer>()
    private var discount: CommercialOffer? = null
    private var total: Double = 0.0

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        booksAdapter = BooksAdapter { book -> adapterOnClick(book) }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        BasketService.update(this, ArrayList<Book>())

        booksListViewModel.booksLiveData.observe(this) {
            it?.let {
                if(!it.isLoading && it.books.isNotEmpty() && booksAdapter.currentList != it.books){
                    var basket = BasketService.getAllBooksInBasket(this);
                    //filter the cart
                    var newCart = basket.filter { book -> !it.books.contains(book) }
                    if(newCart.size != basket.size){
                        BasketService.update(this, newCart)
                    }
                    it.books.map {
                        it.isInBasket = newCart.find { book -> book.isbn == it.isbn } != null
                    }
                    //filter the result
                    booksAdapter.submitList(it.books)
                }
            }
                booksAdapter.submitList(it.books)
            }
        }

        booksListViewModel.dataSource.state.observe(this) { state ->
            Toast.makeText(
                this@BooksListActivity,
                "${state.books.size} books | isLoading ${state.isLoading}",
                Toast.LENGTH_SHORT
            )
                .show()

            println("Liste de livres")
            println(state.books)
            for (book in state.books) {
                listISBN.add(book.isbn)
                listPrice.add(book.price)
            }
            // keep only 3 first books
            if (listISBN.size > 3) {
                listISBN.subList(3, listISBN.size).clear()
                listPrice.subList(3, listPrice.size).clear()
            }

            testAffichage()
            runBlocking {
                calculateTotal()
            }
        }

        booksListViewModel.dataSource.loadBooks()

        val basketButton: View = findViewById(R.id.basket_button)
        basketButton.setOnClickListener {
            basketButtonOnClick()
        }
    }

    override fun onResume() {
        super.onResume()
        booksListViewModel.dataSource.loadBooks()
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
        // total in listprice
        for (price in listPrice) {
            total += price
        }

        if (listISBN.size >= 3) {
            val retrofit = getRetrofit()
            val service = retrofit.create(BookService::class.java)
            println("Liste d'ISBN AAAAAA")
            println(listISBN)
            val commercialOffersRequest =
                service.getCommercialOffers(listISBN.joinToString(","))
            println("CommercialOffersRequest")
            println(commercialOffersRequest)
                    val allOffers = commercialOffersRequest.offers
                    println("All offers")
                    println(allOffers)
                    discount = CommercialOffer.bestOffer(allOffers, total)
                    println("Réduction")
                    println(discount)
                    println("Total")
                    println(total)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    // onClick on buttton basket go to basket
    fun basketButtonOnClick() {
        val intent = Intent(this@BooksListActivity, BasketActivity::class.java)
        startActivity(intent)
    }

    /* Opens BookDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(book: Book) {
        val intent = Intent(this, BookDetailActivity()::class.java)
        intent.putExtra(BOOK_ID, book.isbn)
        startActivity(intent)
    }
}
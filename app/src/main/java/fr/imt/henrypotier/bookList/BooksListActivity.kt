package fr.imt.henrypotier.bookList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import fr.imt.henrypotier.BookService
import fr.imt.henrypotier.BookState
import fr.imt.henrypotier.R
import fr.imt.henrypotier.basket.BasketActivity
import fr.imt.henrypotier.bookDetail.BookDetailActivity
import fr.imt.henrypotier.data.Book
import fr.imt.henrypotier.data.CommercialOffer
import fr.imt.henrypotier.data.CommercialOfferType
import fr.imt.henrypotier.data.CommercialOffers
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BOOK_ID = "book id"

class BooksListActivity : AppCompatActivity() {
    private val newBookActivityRequestCode = 1
    private lateinit var realm: Realm
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

        /* Instantiates headerAdapter and BooksAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val booksAdapter = BooksAdapter { book -> adapterOnClick(book) }
        val config = RealmConfiguration.Builder(schema = setOf())
            .build()
        this.realm = Realm.open(config)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

        booksListViewModel.booksLiveData.observe(this) {
            it?.let {
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
        this.realm.close();
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

    /* Adds book to bookList when FAB is clicked. */
    //private fun fabOnClick() {
    //    val intent = Intent(this, AddBookActivity::class.java)
    //    startActivityForResult(intent, newBookActivityRequestCode)
    //}

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        //TODO POUR LE CART ?
        /*
        /* Inserts book into viewModel. */
        if (requestCode == newBookActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val bookTitle = data.getStringExtra()
                val bookSynopsis = data.getStringExtra("BOOK_SYNOPSIS")

                //BooksListViewModel.insertBook(bookTitle, bookSynopsis)
            }
        }

         */
    }
}
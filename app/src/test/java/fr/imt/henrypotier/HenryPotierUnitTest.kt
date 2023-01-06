package fr.imt.henrypotier

import fr.imt.henrypotier.data.CommercialOffer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HenryPotierUnitTest {
    @Test
    fun check_bookService_get_7_books() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val bookService = retrofit.create(BookService::class.java)
        val books = runBlocking { bookService.listBooks() }

        assertEquals(7, books.size)
    }

    @Test
    fun check_bookService_does_not_get_8_books() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val bookService = retrofit.create(BookService::class.java)
        val books = runBlocking { bookService.listBooks() }

        assertNotEquals(8, books.size)
    }

    @Test
    fun check_commercialOffersService_get_1_offer() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val bookService = retrofit.create(BookService::class.java)
        val commercialOffers = runBlocking { bookService.getCommercialOffers("c8fabf68-8374-48fe-a7ea-a00ccd07afff") }

        assertEquals(1, commercialOffers.offers.size)
    }

    @Test
    fun check_commercialOffersService_does_not_get_2_offers() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val bookService = retrofit.create(BookService::class.java)
        val commercialOffers = runBlocking { bookService.getCommercialOffers("c8fabf68-8374-48fe-a7ea-a00ccd07afff") }

        assertNotEquals(2, commercialOffers.offers.size)
    }

    @Test
    fun check_commercialOffersService_get_3_offers() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val bookService = retrofit.create(BookService::class.java)
        val commercialOffers = runBlocking { bookService.getCommercialOffers("c8fabf68-8374-48fe-a7ea-a00ccd07afff,c8fabf68-8374-48fe-a7ea-a00ccd07afff") }

        assertEquals(3, commercialOffers.offers.size)
    }

    @Test
    fun check_commercialOffersService_does_not_get_4_offers() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val bookService = retrofit.create(BookService::class.java)
        val commercialOffers = runBlocking { bookService.getCommercialOffers("c8fabf68-8374-48fe-a7ea-a00ccd07afff,c8fabf68-8374-48fe-a7ea-a00ccd07afff") }

        assertNotEquals(4, commercialOffers.offers.size)
    }

    @Test
    fun check_bestOfferCalcul() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://henri-potier.techx.fr")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val bookService = retrofit.create(BookService::class.java)
        val commercialOffers = runBlocking { bookService.getCommercialOffers("c8fabf68-8374-48fe-a7ea-a00ccd07afff,c8fabf68-8374-48fe-a7ea-a00ccd07afff") }
        val commercialOffersList = commercialOffers.offers
        val bestOffer = CommercialOffer.bestOffer(commercialOffersList, 100.0)

        assertEquals(15, bestOffer?.value)
    }
}
package fr.imt.henrypotier

import fr.imt.henrypotier.data.Book
import fr.imt.henrypotier.data.CommercialOffers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BookService {
    @GET("/books")
    suspend fun listBooks(): List<Book>

    @GET("/books/{isbns}/commercialOffers")
    suspend fun getCommercialOffers(@Path("isbns") isbns: String): CommercialOffers
}
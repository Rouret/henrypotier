package fr.imt.henrypotier

import fr.imt.henrypotier.data.Book
import retrofit2.http.GET

interface BookService {
    @GET("/books")
    suspend fun listBooks(): List<Book>
}
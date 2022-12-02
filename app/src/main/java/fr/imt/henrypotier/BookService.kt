package fr.imt.henrypotier

import retrofit2.http.GET

interface BookService {
    @GET("/books")
    suspend fun listBooks(): List<Book>
}
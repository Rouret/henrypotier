package fr.imt.henrypotier

import fr.imt.henrypotier.data.Book

data class BookState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean
)
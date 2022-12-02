package fr.imt.henrypotier

data class BookState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean
)
package fr.imt.henrypotier.data

open class Book {
    var isbn: String = ""
    var title: String = ""
    var price: Int = 0
    var cover: String = ""
    var synopsis: List<String> = listOf()
    var isInBasket: Boolean = false

    override fun toString(): String {
        return "Book(isbn='$isbn', title='$title', price=$price, cover='$cover', synopsis=$synopsis)"
    }

    fun factoryCartBook(): CartBook {
        val cartBook = CartBook()
        cartBook.isbn = this.isbn
        cartBook.title = this.title
        cartBook.price = this.price
        cartBook.cover = this.cover
        cartBook.synopsis = this.synopsis
        cartBook.isInBasket = this.isInBasket
        return cartBook
    }

}
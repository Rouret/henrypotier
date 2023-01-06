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

    fun factoryCartBook(): BasketBook {
        val basketBook = BasketBook()
        basketBook.isbn = this.isbn
        basketBook.title = this.title
        basketBook.price = this.price
        basketBook.cover = this.cover
        basketBook.synopsis = this.synopsis
        basketBook.isInBasket = true
        return basketBook
    }

}
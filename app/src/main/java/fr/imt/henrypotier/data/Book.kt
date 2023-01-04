package fr.imt.henrypotier.data

class Book {
    var isbn: String = ""
    var title: String = ""
    var price: Int = 0
    var cover: String = ""
    var synopsis: List<String> = listOf()
    var isInBasket : Boolean = false

    override fun toString(): String {
        return "Book(isbn='$isbn', title='$title', price=$price, cover='$cover', synopsis=$synopsis)"
    }
}
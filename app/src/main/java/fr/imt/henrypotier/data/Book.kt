package fr.imt.henrypotier.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Book() {
    var isbn: String = ""
    var title: String = ""
    var price: Int = 0
    var cover: String = ""
    var synopsis: List<String> = listOf()

    override fun toString(): String {
        return "Book(isbn='$isbn', title='$title', price=$price, cover='$cover', synopsis=$synopsis)"
    }
}
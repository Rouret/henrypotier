package fr.imt.henrypotier.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Book() : RealmObject {
    @PrimaryKey
    var isbn: String = ""
    var title: String = ""
    var price: String = ""
    var cover: String = ""
    constructor(isbn: String, title: String, price: String, cover: String) : this() {
        this.isbn = isbn
        this.title = title
        this.price = price
        this.cover = cover
    }
}
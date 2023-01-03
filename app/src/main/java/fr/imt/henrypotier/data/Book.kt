package fr.imt.henrypotier.data


open class Book(
    var isbn: String = "",
    var title: String = "",
    var price: Int = 0,
    var cover: String = "",
    var synopsis: List<String> = listOf()
)
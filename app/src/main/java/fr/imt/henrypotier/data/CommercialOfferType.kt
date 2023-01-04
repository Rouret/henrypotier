package fr.imt.henrypotier.data

import com.google.gson.annotations.SerializedName

enum class CommercialOfferType {
    @SerializedName("percentage")
    PERCENTAGE,
    @SerializedName("minus")
    MINUS,
    @SerializedName("slice")
    SLICE,
    @SerializedName("none")
    NONE;
}
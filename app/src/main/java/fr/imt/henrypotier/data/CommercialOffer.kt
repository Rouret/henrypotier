package fr.imt.henrypotier.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommercialOffer(
    val type: CommercialOfferType, val value: Int, val sliceValue: Int = 0
) : Parcelable {

    fun calculateDiscount(total: Double): Double {
        return when (type) {
            CommercialOfferType.PERCENTAGE -> total * ((100.0 - value.toDouble()) / 100.0)
            CommercialOfferType.MINUS -> total - value
            CommercialOfferType.SLICE -> total - (total / sliceValue).toInt() * value
        }
    }

    override fun toString(): String {
        return when (type) {
            CommercialOfferType.PERCENTAGE -> "$value% reduction"
            CommercialOfferType.MINUS -> "Reduction of $value€"
            CommercialOfferType.SLICE -> "Pay $value€ less every $sliceValue€"
        }
    }

    companion object {
        fun bestOffer(offers: List<CommercialOffer>?, total: Double): CommercialOffer? {
            return if (offers != null) {
                offers.minByOrNull { offer -> offer.calculateDiscount(total = total) }
            } else {
                CommercialOffer(CommercialOfferType.MINUS, 0, 0)
            }

        }
    }
}
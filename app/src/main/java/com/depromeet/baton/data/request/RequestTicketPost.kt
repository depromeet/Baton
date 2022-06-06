package com.depromeet.baton.data.request

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class RequestTicketPost(
    val location: String,
    val address: String,
    val price: Int,
    val expiryDate: String?=null,
    val type: String,
    val tradeType: String,
    val transferFee: String,
    val canNego: Boolean,
    val hasShower: Boolean,
    val hasLocker: Boolean,
    val hasClothes: Boolean,
    val hasGx: Boolean,
    val canResell: Boolean,
    val canRefund: Boolean,
    val description: String,
    val isMembership: Boolean,
    val isHolding: Boolean,
    val remainingNumber: Int?=null,
    val latitude: Float,
    val longitude: Float,
    val tags: List<String>?=null,
) {
    fun toRequestBody(): HashMap<String, RequestBody?> {

        return hashMapOf(
            "location" to createPartFromString(location),
            "address" to createPartFromString(address),
            "price" to createPartFromString(price.toString()),
            "expiryDate" to createPartFromString(expiryDate),
            "type" to createPartFromString(type),
            "tradeType" to createPartFromString(tradeType),
            "transferFee" to createPartFromString(transferFee),
            "canNego" to createPartFromString(canNego.toString()),
            "hasShower" to createPartFromString(hasShower.toString()),
            "hasLocker" to createPartFromString(hasLocker.toString()),
            "hasClothes" to createPartFromString(hasClothes.toString()),
            "hasGx" to createPartFromString(hasGx.toString()),
            "canResell" to createPartFromString(canResell.toString()),
            "canRefund" to createPartFromString(canRefund.toString()),
            "description" to createPartFromString(description),
            "isMembership" to createPartFromString(isMembership.toString()),
            "isHolding" to createPartFromString(isHolding.toString()),
            "remainingNumber" to createPartFromString(remainingNumber.toString()),
            "latitude" to createPartFromString(latitude.toString()),
            "longitude" to createPartFromString(longitude.toString()),
            "tags" to createPartFromString(tags?.map { createPartFromString(it) }.toString()),
            )
    }
}

fun createPartFromString(stringData: String?): RequestBody? {
    return stringData?.toRequestBody("text/plain".toMediaTypeOrNull())
}

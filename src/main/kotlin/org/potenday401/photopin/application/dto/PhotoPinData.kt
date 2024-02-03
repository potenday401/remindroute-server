package org.potenday401.photopin.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhotoPinData(
    val photoPinId: String,
    val memberId: String,
    val tagIds: List<String>,
    val photoUrl: String,
    val photoDateTime: Long,
    val latLng: LatLngData,
    val locality: String,
    val subLocality: String,
    val createdAt: Long,
    val modifiedAt: Long
) {
}
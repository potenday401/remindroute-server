package org.potenday401.photopin.application.dto

import PhotoPinTable.subLocality
import kotlinx.serialization.Serializable

@Serializable
data class PhotoPinCreationData(
    val photoPinId: String,
    val memberId: String,
    val tagIds: List<String>,
    val photoDateTime: Long,
    val photoFileBase64Payload: String,
    val photoFileExt: String,
    val latLng: LatLngData,
    val locality: String,
    val subLocality: String
) {
}
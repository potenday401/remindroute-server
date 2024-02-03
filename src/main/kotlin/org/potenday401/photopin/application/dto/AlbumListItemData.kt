package org.potenday401.photopin.application.dto

import kotlinx.serialization.Serializable


@Serializable
data class AlbumListItemData(
    val photoPinId:String,
    val photoUrl: String,
    val latLng: LatLngData,
    val createdAt: Long,
) {
}
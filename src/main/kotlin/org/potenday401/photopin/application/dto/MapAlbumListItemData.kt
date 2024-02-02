package org.potenday401.photopin.application.dto

import kotlinx.serialization.Serializable


@Serializable
data class MapAlbumListItemData(
    val photoPinId:String,
    val photoUrl: String,
    val latLng: LatLngData,
    val createdAt: Long,
) {
}
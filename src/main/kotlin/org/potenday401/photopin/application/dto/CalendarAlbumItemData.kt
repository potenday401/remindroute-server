package org.potenday401.photopin.application.dto

import kotlinx.serialization.Serializable


@Serializable
data class CalendarAlbumItemData(
    val photoPinId:String,
    val photoUrl: String,
    val date: Int,
    val createdAt: Long,

    ) {
}
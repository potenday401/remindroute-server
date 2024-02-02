package org.potenday401.photopin.application.dto

import kotlinx.serialization.Serializable


@Serializable
data class TagAlbumListItemData(
    val thumbnailPhotoUrl: String,
    val tagId: String,
    val tagName: String,
    val tagCount: Long,
) {
}
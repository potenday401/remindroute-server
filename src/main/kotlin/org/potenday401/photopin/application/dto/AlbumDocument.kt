package org.potenday401.photopin.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumDocument(
    val listItems: List<AlbumListItemData>,
)
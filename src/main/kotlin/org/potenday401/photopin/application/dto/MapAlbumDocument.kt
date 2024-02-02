package org.potenday401.photopin.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapAlbumDocument(
    val listItems: List<MapAlbumListItemData>,
)
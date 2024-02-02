package org.potenday401.photopin.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class CalendarAlbumDocument(
    val year: Int,
    val month: Int,
    val dayOfMonthToItem: Map<Int, CalendarAlbumItemData>
)
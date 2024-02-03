package org.potenday401.photopin.domain.model

import java.time.LocalDateTime

class PhotoPin(
    val id: String,
    val memberId: String,
    var tagIds:List<String>,
    var photoUrl: String,
    var photoDateTime: LocalDateTime,
    var latLng: LatLng,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val modifiedAt: LocalDateTime = LocalDateTime.now()
) {

    fun changeContent(tagIds: List<String>, photoUrl: String, photoDateTime: LocalDateTime, latLng:LatLng) {
        this.tagIds = tagIds
        this.photoUrl = photoUrl
        this.photoDateTime = photoDateTime
        this.latLng = latLng
    }
}
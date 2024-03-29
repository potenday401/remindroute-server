package org.potenday401.photopin.domain.model

import java.time.LocalDateTime

class PhotoPin(
    val id: String,
    val memberId: String,
    var tagIds: List<String>,
    var photoUrl: String,
    var photoDateTime: LocalDateTime,
    var latLng: LatLng,
    var locality: String,
    var subLocality: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val modifiedAt: LocalDateTime = LocalDateTime.now()
) {

    fun changeContent(
        tagIds: List<String>,
        photoUrl: String,
        photoDateTime: LocalDateTime,
        latLng: LatLng,
        locality: String,
        subLocality: String
    ) {
        this.tagIds = tagIds
        this.photoUrl = photoUrl
        this.photoDateTime = photoDateTime
        this.latLng = latLng
        this.locality = locality
        this.subLocality = subLocality
    }
}
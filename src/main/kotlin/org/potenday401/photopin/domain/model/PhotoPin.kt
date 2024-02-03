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
}
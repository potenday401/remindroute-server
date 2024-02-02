package org.potenday401.photopin.domain.model

import java.time.LocalDateTime

class PhotoPin(
    val id: String,
    val memberId: String,
    val tagIds:List<String>,
    val photoUrl: String,
    val photoDateTime: LocalDateTime,
    val latLng: LatLng,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val modifiedAt: LocalDateTime = LocalDateTime.now()
) {
}
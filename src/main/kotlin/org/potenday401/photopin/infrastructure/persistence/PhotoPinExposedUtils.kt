package org.potenday401.photopin.infrastructure.persistence

import org.jetbrains.exposed.sql.ResultRow
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.photopin.domain.model.PhotoPin

fun ResultRow.toPhotoPin(tagIds: List<String>): PhotoPin {
    return PhotoPin(
        id = this[PhotoPinTable.id],
        memberId = this[PhotoPinTable.memberId],
        tagIds = tagIds,
        photoUrl = this[PhotoPinTable.photoUrl],
        photoDateTime = this[PhotoPinTable.photoDateTime],
        latLng = LatLng(
            latitude = this[PhotoPinTable.latitude],
            longitude = this[PhotoPinTable.longitude]
        ),
        locality = this[PhotoPinTable.locality],
        subLocality = this[PhotoPinTable.subLocality],
        createdAt = this[PhotoPinTable.createdAt],
        modifiedAt = this[PhotoPinTable.modifiedAt]
    )
}
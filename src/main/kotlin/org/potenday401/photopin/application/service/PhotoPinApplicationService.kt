package org.potenday401.photopin.application.service


import org.potenday401.photopin.application.dto.LatLngData
import org.potenday401.photopin.application.dto.PhotoPinCreationData
import org.potenday401.photopin.application.dto.PhotoPinData
import org.potenday401.photopin.application.service.PhotoPinDataConverter.toPhotoPinData
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.photopin.domain.model.PhotoPin
import org.potenday401.photopin.domain.model.PhotoPinRepository
import org.potenday401.util.toEpochMilli
import org.potenday401.util.toLocalDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class PhotoPinApplicationService(private val photoPinRepository: PhotoPinRepository) {

    fun getPhotoPinById(id: String): PhotoPinData? {
        val photoPin = photoPinRepository.findById(id)
        if (photoPin != null) {
            return toPhotoPinData(photoPin)
        } else {
            return null
        }
    }

    fun getAllPhotoPins(): List<PhotoPinData> {
        return photoPinRepository.findAll().map { toPhotoPinData(it) }
    }


    fun createPhotoPin(photoPinCreationData: PhotoPinCreationData) {
        // TODO: save file to S3 then get url
        // TODO: add tagId validation
        val photoUrl: String = ""
        val latLng =
            LatLng(photoPinCreationData.latLng.latitude, photoPinCreationData.latLng.longitude)

        val photoPin = PhotoPin(
            photoPinCreationData.photoPinId,
            photoPinCreationData.memberId,
            photoPinCreationData.tagIds,
            photoUrl,
            photoPinCreationData.photoDateTime.toLocalDateTime(),
            latLng
        )
        photoPinRepository.create(photoPin)
    }
}

object PhotoPinDataConverter {
    fun toPhotoPinData(photoPin: PhotoPin): PhotoPinData {
        return PhotoPinData(
            photoPinId = photoPin.id,
            memberId = photoPin.memberId,
            tagIds = photoPin.tagIds,
            photoUrl = photoPin.photoUrl,
            photoDateTime = photoPin.photoDateTime.toEpochMilli(),
            latLng = toLatLngData(photoPin.latLng),
            createdAt = photoPin.createdAt.toEpochMilli(),
            modifiedAt = photoPin.modifiedAt.toEpochMilli()
        )
    }

    fun toLatLngData(latLng: LatLng): LatLngData {
        return LatLngData(
            latitude = latLng.latitude,
            longitude = latLng.longitude
        )
    }
}


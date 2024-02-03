package org.potenday401.photopin.application.service


import org.potenday401.common.domain.model.File
import org.potenday401.common.domain.model.FileStorageService
import org.potenday401.photopin.application.dto.LatLngData
import org.potenday401.photopin.application.dto.PhotoPinCreationData
import org.potenday401.photopin.application.dto.PhotoPinData
import org.potenday401.photopin.application.dto.PhotoPinMutationData
import org.potenday401.photopin.application.service.PhotoPinDataConverter.toPhotoPinData
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.photopin.domain.model.PhotoPin
import org.potenday401.photopin.domain.model.PhotoPinRepository
import org.potenday401.util.toEpochMilli
import org.potenday401.util.toLocalDateTime
import java.util.*

class PhotoPinApplicationService(
    private val photoPinRepository: PhotoPinRepository,
    private val fileStorageService: FileStorageService
) {

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
        // TODO: add tagId validation

        val file = createFile(
            photoPinCreationData.photoFileBase64Payload,
            photoPinCreationData.photoFileExt
        )
        val dir = "images/${photoPinCreationData.memberId}"
        val photoUrl = fileStorageService.storeFile(dir, file).toExternalForm()
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

    fun updatePhotoPin(photoPinMutationData: PhotoPinMutationData) {
        // TODO: add tagId validation

        val photoPin = photoPinRepository.findById(photoPinMutationData.photoPinId)
            ?: throw Exception("photo pin not found")

        val latLng =
            LatLng(photoPinMutationData.latLng.latitude, photoPinMutationData.latLng.longitude)
        var photoUrl = photoPin.photoUrl

        if (photoPinMutationData.photoFileBase64Payload != null && photoPinMutationData.photoFileExt != null) {
            val file = createFile(
                photoPinMutationData.photoFileBase64Payload,
                photoPinMutationData.photoFileExt
            )
            val dir = "images/${photoPin.memberId}"
            photoUrl = fileStorageService.storeFile(dir, file).toExternalForm()
        }

        photoPin.photoUrl = photoUrl
        photoPin.tagIds = photoPinMutationData.tagIds
        photoPin.latLng = latLng

        photoPinRepository.update(photoPin)
    }

    private fun createFile(base64Payload: String, ext: String): File {
        val fileByte: ByteArray = Base64.getDecoder().decode(base64Payload)
        return File(fileByte, ext)
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


package org.potenday401.photopin.application.service


import PhotoPinTable.locality
import org.potenday401.common.domain.model.File
import org.potenday401.common.domain.model.FileStorageService
import org.potenday401.photopin.application.dto.LatLngData
import org.potenday401.photopin.application.dto.PhotoPinCreationData
import org.potenday401.photopin.application.dto.PhotoPinData
import org.potenday401.photopin.application.dto.PhotoPinContentMutationData
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

    fun getAllPhotoPins(memberId: String): List<PhotoPinData> {
        return photoPinRepository.findAll(memberId).map { toPhotoPinData(it) }
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
            latLng,
            photoPinCreationData.locality,
            photoPinCreationData.subLocality
        )
        photoPinRepository.create(photoPin)
    }

    fun changePhotoPinContent(photoPinContentMutationData: PhotoPinContentMutationData) {
        // TODO: add tagId validation

        val photoPin = photoPinRepository.findById(photoPinContentMutationData.photoPinId)
            ?: throw Exception("photo pin not found")

        var photoUrl = photoPin.photoUrl
        if (photoPinContentMutationData.photoFileBase64Payload != null && photoPinContentMutationData.photoFileExt != null) {
            val file = createFile(
                photoPinContentMutationData.photoFileBase64Payload,
                photoPinContentMutationData.photoFileExt
            )
            val dir = "images/${photoPin.memberId}"
            photoUrl = fileStorageService.storeFile(dir, file).toExternalForm()
        }

        val latLng =
            LatLng(
                photoPinContentMutationData.latLng.latitude,
                photoPinContentMutationData.latLng.longitude
            )

        photoPin.changeContent(
            photoPinContentMutationData.tagIds,
            photoUrl,
            photoPinContentMutationData.photoDateTime.toLocalDateTime(),
            latLng,
            photoPinContentMutationData.locality,
            photoPinContentMutationData.subLocality
        )
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
            locality = photoPin.locality,
            subLocality = photoPin.subLocality,
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


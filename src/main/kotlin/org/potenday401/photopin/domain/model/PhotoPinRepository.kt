package org.potenday401.photopin.domain.model


interface PhotoPinRepository {
    fun findById(id: String): PhotoPin?
    fun findAllByTagId(tagId: String): List<PhotoPin>
    fun findAll(memberId: String): List<PhotoPin>
    fun create(photoPin: PhotoPin)
}

package org.potenday401.photopin.domain.model


interface PhotoPinRepository {
    fun findById(id: String): PhotoPin?
    fun findAllByTagId(tagId: String): List<PhotoPin>
    fun findAll(): List<PhotoPin>
    fun create(photoPin: PhotoPin)
    fun update(photoPin: PhotoPin)
}

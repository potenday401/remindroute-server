package org.potenday401.photopin.domain.model


interface PhotoPinRepository {
    fun findById(id: String): PhotoPin?
    // fun findAllByNameIn(names: List<String>): List<Tag>
    fun findAll(): List<PhotoPin>
    fun create(photoPin: PhotoPin)
}

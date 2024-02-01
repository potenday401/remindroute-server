package org.potenday401.photopin.infrastructure.persistence

import ExposedPhotoPinRepository
import PhotoPinTable
import PhotoPinTagIdsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.photopin.domain.model.PhotoPin
import java.time.LocalDateTime
import java.util.*

class ExposedPhotoPinRepositoryTest {

    val repository = ExposedPhotoPinRepository()

    @Before
    fun setup() {
        // 각각의 테스트마다 새로운 DB 사용
        val uniqueId = UUID.randomUUID().toString()
        Database.connect("jdbc:h2:mem:test-$uniqueId;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(PhotoPinTable)
            SchemaUtils.create(PhotoPinTagIdsTable)

            val photoPin1 = PhotoPin(
                "test-id-1",
                "test-member-id-1",
                mutableListOf("test-tag-id-1", "test-tag-id-2"),
                "url",
                LocalDateTime.now(),
                LatLng(5.0, 6.0)
            )

            PhotoPinTable.insert {
                it[id] = photoPin1.id
                it[memberId] = photoPin1.memberId
                it[photoUrl] = photoPin1.photoUrl
                it[photoDateTime] = photoPin1.photoDateTime
                it[latitude] = photoPin1.latLng.latitude
                it[longitude] = photoPin1.latLng.longitude
                it[createdAt] = photoPin1.createdAt
                it[modifiedAt] = photoPin1.modifiedAt
            }

            photoPin1.tagIds.forEach { tagId ->
                PhotoPinTagIdsTable.insert {
                    it[photoPinId] = photoPin1.id
                    it[this.tagId] = tagId
                }
            }

            val photoPin2 = PhotoPin(
                "test-id-2",
                "test-member-id-2",
                mutableListOf("test-tag-id-3", "test-tag-id-4"),
                "url",
                LocalDateTime.now(),
                LatLng(5.0, 6.0)
            )

            PhotoPinTable.insert {
                it[id] = photoPin2.id
                it[memberId] = photoPin2.memberId
                it[photoUrl] = photoPin2.photoUrl
                it[photoDateTime] = photoPin2.photoDateTime
                it[latitude] = photoPin2.latLng.latitude
                it[longitude] = photoPin2.latLng.longitude
                it[createdAt] = photoPin2.createdAt
                it[modifiedAt] = photoPin2.modifiedAt
            }

            photoPin2.tagIds.forEach { tagId ->
                PhotoPinTagIdsTable.insert {
                    it[photoPinId] = photoPin2.id
                    it[this.tagId] = tagId
                }
            }

        }
    }

    @Test
    fun testFindById() {
        val foundPhotoPin = repository.findById("test-id-1")

        assertNotNull(foundPhotoPin)
        assertEquals("test-id-1", foundPhotoPin?.id)
        assertEquals("test-member-id-1", foundPhotoPin?.memberId)
        assertEquals(5.0, foundPhotoPin?.latLng?.latitude)
        assertEquals(6.0, foundPhotoPin?.latLng?.longitude)
        assertEquals("test-tag-id-1", foundPhotoPin?.tagIds?.get(0) ?: "")
        assertEquals("test-tag-id-2", foundPhotoPin?.tagIds?.get(1) ?: "")
    }

    @Test
    fun testCreate() {
        val photoPin = PhotoPin(
            "test-id-3",
            "test-member-id-3",
            mutableListOf("test-tag-id-1", "test-tag-id-2"),
            "url",
            LocalDateTime.now(),
            LatLng(5.0, 6.0)
        )

        transaction {
            repository.create(photoPin)
            val result = repository.findById(photoPin.id)
            assertEquals("test-id-3", result?.id)
            assertEquals("test-member-id-3", result?.memberId)
            assertEquals(5.0, result?.latLng?.latitude)
            assertEquals(6.0, result?.latLng?.longitude)
            assertEquals("test-tag-id-1", result?.tagIds?.get(0) ?: "")
            assertEquals("test-tag-id-2", result?.tagIds?.get(1) ?: "")
        }
    }

    @Test
    fun testFindAll() {
        val photoPins = repository.findAll()

        assertTrue(photoPins.stream().anyMatch { photoPin ->
            photoPin.id == "test-id-1" && photoPin.memberId == "test-member-id-1"
        })

        assertTrue(photoPins.stream().anyMatch { photoPin ->
            photoPin.id == "test-id-2" && photoPin.memberId == "test-member-id-2"
        })

    }

}
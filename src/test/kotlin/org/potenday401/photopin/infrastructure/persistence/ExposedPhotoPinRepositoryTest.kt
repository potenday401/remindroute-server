package org.potenday401.photopin.infrastructure.persistence

import ExposedPhotoPinRepository
import PhotoPinTable
import PhotoPinTable.photoUrl
import PhotoPinTagIdsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.potenday401.photopin.domain.model.*
import org.potenday401.util.toLocalDateTime
import java.time.LocalDateTime
import java.util.*

class ExposedPhotoPinRepositoryTest {

    private val repository = ExposedPhotoPinRepository()

    @Before
    fun setup() {
        // 각각의 테스트마다 새로운 DB 사용
        val uniqueId = UUID.randomUUID().toString()
        Database.connect("jdbc:h2:mem:test-$uniqueId;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(PhotoPinTable)
            SchemaUtils.create(PhotoPinTagIdsTable)

            PhotoPinTable.insert {
                it[id] = mockPhotoPin1.id
                it[memberId] = mockPhotoPin1.memberId
                it[photoUrl] = mockPhotoPin1.photoUrl
                it[photoDateTime] = mockPhotoPin1.photoDateTime
                it[latitude] = mockPhotoPin1.latLng.latitude
                it[longitude] = mockPhotoPin1.latLng.longitude
                it[createdAt] = mockPhotoPin1.createdAt
                it[modifiedAt] = mockPhotoPin1.modifiedAt
            }

            mockPhotoPin1.tagIds.forEach { tagId ->
                PhotoPinTagIdsTable.insert {
                    it[photoPinId] = mockPhotoPin1.id
                    it[this.tagId] = tagId
                }
            }

            PhotoPinTable.insert {
                it[id] = mockPhotoPin2.id
                it[memberId] = mockPhotoPin2.memberId
                it[photoUrl] = mockPhotoPin2.photoUrl
                it[photoDateTime] = mockPhotoPin2.photoDateTime
                it[latitude] = mockPhotoPin2.latLng.latitude
                it[longitude] = mockPhotoPin2.latLng.longitude
                it[createdAt] = mockPhotoPin2.createdAt
                it[modifiedAt] = mockPhotoPin2.modifiedAt
            }

        }
    }

    @Test
    fun testFindById() {
        val foundPhotoPin = repository.findById(mockPhotoPin1.id)

        assertNotNull(foundPhotoPin)
        assertEquals(mockPhotoPin1.id, foundPhotoPin?.id)
        assertEquals(mockPhotoPin1.memberId, foundPhotoPin?.memberId)
        assertEquals(mockPhotoPin1.latLng.latitude, foundPhotoPin?.latLng?.latitude)
        assertEquals(mockPhotoPin1.latLng.longitude, foundPhotoPin?.latLng?.longitude)
        assertEquals(mockPhotoPin1.tagIds[0], foundPhotoPin?.tagIds?.get(0) ?: "")
    }

    @Test
    fun testUpdate() {
        transaction {
            // given
            val foundPhotoPin = repository.findById(mockPhotoPin1.id) ?: throw Exception("not-found")

            val newLatLng =  LatLng(1.0,2.0)
            val newTagIds = mutableListOf("test-tag-id-4","test-tag-id-5")

            foundPhotoPin.tagIds = newTagIds
            foundPhotoPin.latLng = newLatLng

            foundPhotoPin.changeContent(newTagIds, "newPhotoUrl", LocalDateTime.of(2024,1,12,23,55), newLatLng)

            // when
            repository.update(foundPhotoPin)
            val foundAgainPhotoPin = repository.findById(mockPhotoPin1.id) ?: throw Exception("not-found")

            // then
            assertEquals(foundAgainPhotoPin.id, foundPhotoPin.id)
            assertEquals(foundAgainPhotoPin.memberId, foundPhotoPin.memberId)
            assertEquals(foundAgainPhotoPin.latLng.latitude.toString(), newLatLng.latitude.toString())
            assertEquals(foundAgainPhotoPin.latLng.longitude.toString(), newLatLng.longitude.toString())
            assertEquals(foundAgainPhotoPin.tagIds[0], newTagIds[0])
            assertEquals(foundAgainPhotoPin.tagIds[1], newTagIds[1])
            assertEquals(foundAgainPhotoPin.photoUrl, "newPhotoUrl")
        }
    }

    @Test
    fun testCreate() {
        transaction {
            repository.create(mockPhotoPin5)
            val result = repository.findById(mockPhotoPin5.id)
            assertEquals(mockPhotoPin5.id, result?.id)
            assertEquals(mockPhotoPin5.memberId, result?.memberId)
            assertEquals(mockPhotoPin5.latLng.latitude, result?.latLng?.latitude)
            assertEquals(mockPhotoPin5.latLng.longitude, result?.latLng?.longitude)
            assertEquals(mockPhotoPin5.tagIds[0], result?.tagIds?.get(0) ?: "")
            assertEquals(mockPhotoPin5.tagIds[1], result?.tagIds?.get(1) ?: "")
        }
    }

    @Test
    fun testFindAll() {
        val photoPins = repository.findAll(mockPhotoPin1.memberId)

        assertTrue(photoPins.stream().anyMatch { photoPin ->
            photoPin.id == mockPhotoPin1.id && photoPin.memberId == mockPhotoPin1.memberId
        })

        assertTrue(photoPins.stream().anyMatch { photoPin ->
            photoPin.id == mockPhotoPin2.id && photoPin.memberId == mockPhotoPin2.memberId
        })

    }

}
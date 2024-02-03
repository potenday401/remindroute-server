package org.potenday401.photopin.infrastructure.persistence

import PhotoPinTable
import PhotoPinTagIdsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.potenday401.photopin.domain.model.*
import org.potenday401.tag.domain.model.*
import org.potenday401.tag.infrastructure.persistence.ExposedTagRepository
import org.potenday401.tag.infrastructure.persistence.TagTable
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class PhotoPinQueriesTest {

    private val queries = PhotoPinQueries(ExposedTagRepository())

    @Before
    fun setup() {
        // 각각의 테스트마다 새로운 DB 사용
        val uniqueId = UUID.randomUUID().toString()
        Database.connect("jdbc:h2:mem:test-$uniqueId;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(TagTable, PhotoPinTable, PhotoPinTagIdsTable)

            val mockTag1 = createMockTag1()
            val mockTag2 = createMockTag2()
            val mockTag3 = createMockTag3()
            val mockTag4 = createMockTag4()

            insertTag(mockTag1)
            insertTag(mockTag2)
            insertTag(mockTag3)
            insertTag(mockTag4)
            // insertTag(mockTag5)

            val mockPhotoPin1 = createMockPhotoPin1()
            val mockPhotoPin2 = createMockPhotoPin2()
            val mockPhotoPin3 = createMockPhotoPin3()
            val mockPhotoPin4 = createMockPhotoPin4()
            val mockPhotoPin5 = createMockPhotoPin5()
            val mockPhotoPin6 = createMockPhotoPin6()

            insertPhotoPin(mockPhotoPin1)
            insertPhotoPin(mockPhotoPin2)
            insertPhotoPin(mockPhotoPin3)
            insertPhotoPin(mockPhotoPin4)
            insertPhotoPin(mockPhotoPin5)
            insertPhotoPin(mockPhotoPin6)
        }
    }

    @Test
    fun getTagAlbumDocuments() {
        val tagAlbumDocument = queries.getTagAlbumDocument("test-member-id-1")

        Assert.assertEquals(4, tagAlbumDocument.listItems.size)
        Assert.assertEquals(tagAlbumDocument.listItems[0].tagCount, 3)
        Assert.assertEquals(tagAlbumDocument.listItems[1].tagCount, 2)
        Assert.assertEquals(tagAlbumDocument.listItems[2].tagCount, 1)
    }

    @Test
    fun getTagAlbumDocumentOrderByCreatedAtDesc() {
        val mockTag2 = createMockTag2()

        val tagAlbumDocument = queries.getTagAlbumDocumentOrderByCreatedAtDesc("test-member-id-1")

        Assert.assertEquals(4, tagAlbumDocument.listItems.size)
        Assert.assertEquals(mockTag2.id, tagAlbumDocument.listItems[0].tagId)
    }

    @Test
    fun getMapAlbumDocument() {
        val mockPhotoPin2 = createMockPhotoPin2()
        val mapAlbumDocument =
            queries.getMapAlbumDocument("test-member-id-1", LatLng(1.0, -1.0), LatLng(4.0, 6.0))

        Assert.assertEquals(1, mapAlbumDocument.listItems.size)
        Assert.assertEquals(mockPhotoPin2.id, mapAlbumDocument.listItems[0].photoPinId)
    }

    @Test
    fun getCalendarAlbumDocument() {
        val mockPhotoPin3 = createMockPhotoPin3()
        val calendarAlbumDocument =
            queries.getCalendarAlbumDocument("test-member-id-1", YearMonth.of(2023, 12))

        Assert.assertEquals(2, calendarAlbumDocument.dayOfMonthToItem.size)
        Assert.assertEquals(
            mockPhotoPin3.id,
            calendarAlbumDocument.dayOfMonthToItem[1]?.photoPinId ?: ""
        )
        Assert.assertEquals(null, calendarAlbumDocument.dayOfMonthToItem[2])
    }

    @Test
    fun getAlbumDocumentOfDate() {
        val mockPhotoPin4 = createMockPhotoPin4()
        val albumDocument =
            queries.getAlbumDocumentOfDate("test-member-id-1", LocalDate.of(2024, 2, 1))

        Assert.assertEquals(1, albumDocument.listItems.size)
        Assert.assertEquals(mockPhotoPin4.id, albumDocument.listItems[0].photoPinId)
    }

    @Test
    fun getAlbumDocumentOfTag() {
        val mockTag2 = createMockTag2()
        val mockPhotoPin4 = createMockPhotoPin4()
        val albumDocument = queries.getAlbumDocumentOfTag("test-member-id-1", mockTag2.id)

        Assert.assertEquals(2, albumDocument.listItems.size)
        Assert.assertEquals(mockPhotoPin4.id, albumDocument.listItems[0].photoPinId)
    }


    companion object {
        fun insertTag(tag: Tag) {
            TagTable.insert {
                it[id] = tag.id
                it[memberId] = tag.memberId
                it[name] = tag.name
                it[createdAt] = tag.createdAt
                it[modifiedAt] = tag.modifiedAt
            }
        }

        fun insertPhotoPin(photoPin: PhotoPin) {
            PhotoPinTable.insert {
                it[id] = photoPin.id
                it[memberId] = photoPin.memberId
                it[photoUrl] = photoPin.photoUrl
                it[photoDateTime] = photoPin.photoDateTime
                it[latitude] = photoPin.latLng.latitude
                it[longitude] = photoPin.latLng.longitude
                it[locality] = photoPin.locality
                it[subLocality] = photoPin.subLocality
                it[createdAt] = photoPin.createdAt
                it[modifiedAt] = photoPin.modifiedAt
            }

            photoPin.tagIds.forEach { tagId ->
                PhotoPinTagIdsTable.insert {
                    it[photoPinId] = photoPin.id
                    it[this.tagId] = tagId
                }
            }
        }
    }


}
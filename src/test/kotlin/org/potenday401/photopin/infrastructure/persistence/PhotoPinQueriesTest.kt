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
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.tag.infrastructure.persistence.ExposedTagRepository
import org.potenday401.tag.infrastructure.persistence.TagTable
import java.time.LocalDateTime
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

            repeat(4) { index ->
                val tagId = "tagId$index"
                val tagName = "tagName$index"

                TagTable.insert {
                    it[id] = tagId
                    it[memberId] = "test-member-id"
                    it[name] = tagName
                    it[createdAt] = LocalDateTime.now()
                    it[modifiedAt] = LocalDateTime.now()
                }
            }
            var repeatCount = 5
            repeat(repeatCount) { photoIndex ->
                val photoPinId = "photoPinId_$photoIndex"
                var photoPinDateTime = LocalDateTime.now().minusDays(1)
                var photoPinLatitude = 37.7749
                var photoPinLongitude = -122.4194
                if(photoIndex == 4) {
                    photoPinDateTime = LocalDateTime.now()
                    photoPinLatitude = 3.0
                    photoPinLongitude = 5.0
                }

                PhotoPinTable.insert {
                    it[id] = photoPinId
                    it[memberId] = "test-member-id"
                    it[photoUrl] = "http://example.com/photo$photoIndex.jpg"
                    it[photoDateTime] = LocalDateTime.now().minusDays(photoIndex.toLong())
                    it[latitude] = photoPinLatitude
                    it[longitude] = photoPinLongitude
                    it[createdAt] = photoPinDateTime
                    it[modifiedAt] = photoPinDateTime
                }

                if (photoIndex == 0 || photoIndex == 1 || photoIndex == 2) {
                    PhotoPinTagIdsTable.insert {
                        it[this.photoPinId] = photoPinId
                        it[this.tagId] = "tagId0"
                    }

                }

                if (photoIndex == 2 || photoIndex == 3) {
                    PhotoPinTagIdsTable.insert {
                        it[this.photoPinId] = photoPinId
                        it[this.tagId] = "tagId1"
                    }
                }

                if (photoIndex == 4) {
                    PhotoPinTagIdsTable.insert {
                        it[this.photoPinId] = photoPinId
                        it[this.tagId] = "tagId2"
                    }
                }
            }
        }
    }

    @Test
    fun getTagAlbumDocuments() {
        val tagAlbumDocument = queries.getTagAlbumDocument("test-member-id")

        Assert.assertEquals(4, tagAlbumDocument.listItems.size)
        Assert.assertEquals(tagAlbumDocument.listItems[0].tagCount, 3)
        Assert.assertEquals(tagAlbumDocument.listItems[1].tagCount, 2)
        Assert.assertEquals(tagAlbumDocument.listItems[2].tagCount, 1)
    }

    @Test
    fun getTagAlbumDocumentOrderByCreatedAtDesc() {
        val tagAlbumDocument = queries.getTagAlbumDocumentOrderByCreatedAtDesc("test-member-id")

        Assert.assertEquals(4, tagAlbumDocument.listItems.size)
        Assert.assertEquals(tagAlbumDocument.listItems[0].tagId, "tagId2")
    }

    @Test
    fun getMapAlbumDocument() {
        val mapAlbumDocument = queries.getMapAlbumDocument("test-member-id", LatLng(1.0,-1.0), LatLng(4.0, 6.0))

        Assert.assertEquals(1, mapAlbumDocument.listItems.size)
        Assert.assertEquals(mapAlbumDocument.listItems[0].photoPinId, "photoPinId_4")
    }
}
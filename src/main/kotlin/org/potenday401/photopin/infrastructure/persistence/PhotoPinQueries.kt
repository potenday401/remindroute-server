package org.potenday401.photopin.infrastructure.persistence

import PhotoPinTable
import PhotoPinTable.latitude
import PhotoPinTable.longitude
import PhotoPinTagIdsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.photopin.application.dto.TagAlbumDocument
import org.potenday401.photopin.application.dto.TagAlbumListItemData
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.photopin.domain.model.PhotoPin
import org.potenday401.tag.domain.model.Tag
import org.potenday401.tag.domain.model.TagRepository
import java.time.LocalDateTime
import kotlin.streams.toList

class PhotoPinQueries(
    private val tagRepository: TagRepository,
) {
    fun getTagAlbumDocument(memberId: String): TagAlbumDocument {
        // TODO: Fix this to use join query for performance
        val tags: List<Tag> = tagRepository.findAllByMemberId(memberId)
        val tagIdToCount: Map<String, Long> = getTagCount(memberId)

        val tagAlbumListItems = tags.stream().map { tag ->
            val photoPin: PhotoPin? = getLatestPhotoPinOfTag(tagId = tag.id)

            TagAlbumListItemData(
                thumbnailPhotoUrl = photoPin?.photoUrl ?: "",
                tagId = tag.id,
                tagName = tag.name,
                tagCount = tagIdToCount.get(tag.id) ?: 0
            )
        }.toList()

        return TagAlbumDocument(tagAlbumListItems)
    }

    fun getTagAlbumDocumentOrderByCreatedAtDesc(memberId: String): TagAlbumDocument {
        // TODO: Fix this to use join query for performance
        val tags: List<Tag> = tagRepository.findAllByMemberId(memberId)
        val tagIdToCount: Map<String, Long> = getTagCount(memberId)

        val tagAlbumListItems = tags.stream().map { tag ->
            val photoPin: PhotoPin? = getLatestPhotoPinOfTag(tagId = tag.id)

            TempDataForSorting(
                thumbnailPhotoUrl = photoPin?.photoUrl ?: "",
                photoPinCreatedDateTime = photoPin?.createdAt ?: LocalDateTime.MIN,
                tagId = tag.id,
                tagName = tag.name,
                tagCount = tagIdToCount.get(tag.id) ?: 0
            )
        }.sorted { i1, i2 -> i2.photoPinCreatedDateTime.compareTo(i1.photoPinCreatedDateTime)}
            .map { data ->
                TagAlbumListItemData(
                    thumbnailPhotoUrl = data.thumbnailPhotoUrl,
                    tagId = data.tagId,
                    tagName = data.tagName,
                    tagCount = data.tagCount
                )
            }.toList()

        return TagAlbumDocument(tagAlbumListItems)

    }

    private data class TempDataForSorting(
        val thumbnailPhotoUrl: String,
        val photoPinCreatedDateTime: LocalDateTime,
        val tagId: String,
        val tagName: String,
        val tagCount: Long,
    ) {
    }


    private fun getTagCount(memberId: String): MutableMap<String, Long> {
        val result: MutableMap<String, Long> = mutableMapOf()
        transaction {
            PhotoPinTagIdsTable
                .leftJoin(PhotoPinTable, { photoPinId }, { id })
                .slice(
                    PhotoPinTagIdsTable.tagId,
                    PhotoPinTagIdsTable.tagId.count().alias("count")
                )
                .select { (PhotoPinTable.memberId eq memberId) }
                .groupBy(PhotoPinTagIdsTable.tagId)
                .orderBy(PhotoPinTagIdsTable.tagId.count() to SortOrder.DESC)
                .map {
                    result.put(
                        it[PhotoPinTagIdsTable.tagId].toString(),
                        it[PhotoPinTagIdsTable.tagId.count()]
                    )

                }

        }
        return result
    }

    private fun getLatestPhotoPinOfTag(tagId: String): PhotoPin? {
        return transaction {

            PhotoPinTable.join(
                PhotoPinTagIdsTable,
                JoinType.LEFT,
                additionalConstraint = { PhotoPinTable.id eq PhotoPinTagIdsTable.photoPinId })
                .select { PhotoPinTagIdsTable.tagId eq tagId }
                .orderBy(PhotoPinTable.createdAt to SortOrder.DESC)
                .limit(1)
                .groupBy { it[PhotoPinTable.id] }
                .map { (_, rows) ->
                    val firstRow = rows.first()
                    PhotoPin(
                        id = firstRow[PhotoPinTable.id],
                        memberId = firstRow[PhotoPinTable.memberId],
                        tagIds = rows.mapNotNull { it[PhotoPinTagIdsTable.tagId] }.distinct(),
                        photoUrl = firstRow[PhotoPinTable.photoUrl],
                        photoDateTime = firstRow[PhotoPinTable.photoDateTime],
                        latLng = LatLng(
                            latitude = firstRow[latitude],
                            longitude = firstRow[longitude]
                        ),
                        createdAt = firstRow[PhotoPinTable.createdAt],
                        modifiedAt = firstRow[PhotoPinTable.modifiedAt]
                    )
                }
                .singleOrNull()

        }
    }


}

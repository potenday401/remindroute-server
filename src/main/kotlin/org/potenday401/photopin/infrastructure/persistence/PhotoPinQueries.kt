package org.potenday401.photopin.infrastructure.persistence

import PhotoPinTable
import PhotoPinTable.latitude
import PhotoPinTable.longitude
import PhotoPinTagIdsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.photopin.application.dto.*
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.photopin.domain.model.PhotoPin
import org.potenday401.tag.domain.model.Tag
import org.potenday401.tag.domain.model.TagRepository
import org.potenday401.util.toEpochMilli
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import kotlin.streams.toList


private data class TempDataForSorting(
    val thumbnailPhotoUrl: String,
    val photoPinCreatedDateTime: LocalDateTime,
    val tagId: String,
    val tagName: String,
    val tagCount: Long,
) {
}

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
        }.sorted { i1, i2 -> i2.photoPinCreatedDateTime.compareTo(i1.photoPinCreatedDateTime) }
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

    fun getMapAlbumDocument(memberId: String, start: LatLng, end: LatLng): MapAlbumDocument {
        // TODO: Fix this to use join query for performance
        val photoPins = transaction {
            PhotoPinTable.join(
                PhotoPinTagIdsTable,
                JoinType.LEFT,
                additionalConstraint = { PhotoPinTable.id eq PhotoPinTagIdsTable.photoPinId })
                .select {
                    (PhotoPinTable.memberId eq memberId)
                    (PhotoPinTable.latitude greaterEq start.latitude) and
                            (PhotoPinTable.latitude lessEq end.latitude) and
                            (PhotoPinTable.longitude greaterEq start.longitude) and
                            (PhotoPinTable.longitude lessEq end.longitude)
                }
                .groupBy { it[PhotoPinTable.id] }
                .map { (_, rows) ->
                    val firstRow = rows.first()
                    MapAlbumListItemData(
                        photoPinId = firstRow[PhotoPinTable.id],
                        photoUrl = firstRow[PhotoPinTable.photoUrl],
                        latLng = LatLngData(
                            latitude = firstRow[latitude],
                            longitude = firstRow[longitude]
                        ),
                        createdAt = firstRow[PhotoPinTable.createdAt].toEpochMilli(),
                    )
                }
        }
        return MapAlbumDocument(photoPins)
    }

    fun getCalendarAlbumDocument(memberId: String, yearMonth: YearMonth): CalendarAlbumDocument {
        // TODO: Fix this to use join query for performance
        val startOfMonth = yearMonth.atDay(1).atStartOfDay()
        val endOfMonth =
            yearMonth.atEndOfMonth().atTime(23, 59, 59)

        val photoPins = transaction {
            PhotoPinTable.join(
                PhotoPinTagIdsTable,
                JoinType.LEFT,
                additionalConstraint = { PhotoPinTable.id eq PhotoPinTagIdsTable.photoPinId })
                .select { (PhotoPinTable.memberId eq memberId) and (PhotoPinTable.createdAt greaterEq startOfMonth) and (PhotoPinTable.createdAt lessEq endOfMonth) }

                .groupBy { it[PhotoPinTable.id] }
                .map { (_, rows) ->
                    val firstRow = rows.first()

                    CalendarAlbumItemData(
                        photoPinId = firstRow[PhotoPinTable.id],
                        photoUrl = firstRow[PhotoPinTable.photoUrl],
                        date = firstRow[PhotoPinTable.createdAt].dayOfMonth,
                        createdAt = firstRow[PhotoPinTable.createdAt].toEpochMilli()
                    )
                }
        }

        val dayOfMonthToItem: MutableMap<Int, CalendarAlbumItemData> = mutableMapOf()
        val sortedPhotoPins = photoPins.sortedBy { it.createdAt }
        for (sortedPhotoPin in sortedPhotoPins) {
            dayOfMonthToItem.put(sortedPhotoPin.date, sortedPhotoPin)
        }

        return CalendarAlbumDocument(yearMonth.year, yearMonth.monthValue, dayOfMonthToItem)
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
                    firstRow.toPhotoPin(rows.mapNotNull { it[PhotoPinTagIdsTable.tagId] }.distinct())
                }
                .singleOrNull()

        }
    }
}

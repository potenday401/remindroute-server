package org.potenday401.photopin.domain.model

import org.potenday401.tag.domain.model.mockTag1
import org.potenday401.tag.domain.model.mockTag2
import org.potenday401.tag.domain.model.mockTag3
import org.potenday401.tag.domain.model.mockTag4
import java.time.LocalDateTime

val mockPhotoPin1 = PhotoPin(
    "test-id-1",
    "test-member-id-1",
    mutableListOf(mockTag1.id),
    "url",
    LocalDateTime.of(2024, 1, 23, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2024, 1, 23, 0, 0),
)

val mockPhotoPin2 = PhotoPin(
    "test-id-2",
    "test-member-id-1",
    mutableListOf(mockTag1.id),
    "url",
    LocalDateTime.of(2023, 12, 23, 0, 0),
    LatLng(3.0, 4.0),
    LocalDateTime.of(2023, 12, 23, 0, 0),
)

val mockPhotoPin3 = PhotoPin(
    "test-id-3",
    "test-member-id-1",
    mutableListOf(mockTag1.id),
    "url",
    LocalDateTime.of(2023, 12, 1, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2023, 12, 1, 0, 0),
)

val mockPhotoPin4 = PhotoPin(
    "test-id-4",
    "test-member-id-1",
    mutableListOf(mockTag2.id),
    "url",
    LocalDateTime.of(2024, 2, 1, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2024, 2, 1, 0, 0),

)

val mockPhotoPin5 = PhotoPin(
    "test-id-5",
    "test-member-id-1",
    mutableListOf(mockTag2.id, mockTag3.id),
    "url",
    LocalDateTime.of(2024, 1, 14, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2024, 1, 14, 0, 0),
)

val mockPhotoPin6 = PhotoPin(
    "test-id-6",
    "test-member-id-1",
    mutableListOf(mockTag4.id),
    "url",
    LocalDateTime.of(2024, 1, 12, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2024, 1, 12, 0, 0),
)
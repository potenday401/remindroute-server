package org.potenday401.photopin.domain.model

import org.potenday401.tag.domain.model.createMockTag1
import org.potenday401.tag.domain.model.createMockTag2
import org.potenday401.tag.domain.model.createMockTag3
import org.potenday401.tag.domain.model.createMockTag4
import java.time.LocalDateTime

fun createMockPhotoPin1() = PhotoPin(
    "test-id-1",
    "test-member-id-1",
    mutableListOf(createMockTag1().id),
    "url",
    LocalDateTime.of(2024, 1, 23, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2024, 1, 23, 0, 0),
)

fun createMockPhotoPin2() = PhotoPin(
    "test-id-2",
    "test-member-id-1",
    mutableListOf(createMockTag1().id),
    "url",
    LocalDateTime.of(2023, 12, 23, 0, 0),
    LatLng(3.0, 4.0),
    LocalDateTime.of(2023, 12, 23, 0, 0),
)

fun createMockPhotoPin3() = PhotoPin(
    "test-id-3",
    "test-member-id-1",
    mutableListOf(createMockTag1().id),
    "url",
    LocalDateTime.of(2023, 12, 1, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2023, 12, 1, 0, 0),
)

fun createMockPhotoPin4() = PhotoPin(
    "test-id-4",
    "test-member-id-1",
    mutableListOf(createMockTag2().id),
    "url",
    LocalDateTime.of(2024, 2, 1, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2024, 2, 1, 0, 0),

)

fun createMockPhotoPin5() = PhotoPin(
    "test-id-5",
    "test-member-id-1",
    mutableListOf(createMockTag2().id, createMockTag3().id),
    "url",
    LocalDateTime.of(2024, 1, 14, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2024, 1, 14, 0, 0),
)

fun createMockPhotoPin6() = PhotoPin(
    "test-id-6",
    "test-member-id-1",
    mutableListOf(createMockTag4().id),
    "url",
    LocalDateTime.of(2024, 1, 12, 0, 0),
    LatLng(37.7749, -122.4194),
    LocalDateTime.of(2024, 1, 12, 0, 0),
)
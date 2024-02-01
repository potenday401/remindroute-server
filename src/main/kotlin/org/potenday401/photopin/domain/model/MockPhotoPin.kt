package org.potenday401.photopin.domain.model

import java.time.LocalDateTime

val mockPhotoPin1 = PhotoPin(
    "test-id-1",
    "test-member-id-1",
    mutableListOf("test-tag-id-1", "test-tag-id-2"),
    "url",
    LocalDateTime.now(),
    LatLng(5.0, 6.0)
)

val mockPhotoPin2 = PhotoPin(
    "test-id-2",
    "test-member-id-2",
    mutableListOf("test-tag-id-2", "test-tag-id-3"),
    "url",
    LocalDateTime.now(),
    LatLng(5.0, 6.0)
)

val mockPhotoPin3 = PhotoPin(
    "test-id-3",
    "test-member-id-3",
    mutableListOf("test-tag-id-1", "test-tag-id-3"),
    "url",
    LocalDateTime.now(),
    LatLng(5.0, 6.0)
)
package org.potenday401.tag.domain.model

import java.time.LocalDateTime
import java.time.ZonedDateTime


public class Tag(
    val id: String,
    val memberId: String,
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val modifiedAt: LocalDateTime = LocalDateTime.now()) {
}
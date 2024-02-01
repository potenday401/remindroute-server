package org.potenday401.member.domain.model

import java.time.LocalDateTime

class Member(val id: Int?, val email: String, val nickname: String, val password: String, val createdAt: LocalDateTime = LocalDateTime.now(), val modifiedAt: LocalDateTime = LocalDateTime.now()) {
    constructor(email: String, nickname: String, password: String, createdAt: LocalDateTime = LocalDateTime.now(), modifiedAt: LocalDateTime = LocalDateTime.now())
            : this(null, email, nickname, password, createdAt, modifiedAt)
}
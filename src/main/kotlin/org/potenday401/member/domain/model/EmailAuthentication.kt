package org.potenday401.member.domain.model

import java.time.LocalDateTime

class EmailAuthentication(var id: Int?, val email: String, val authCode: String, val createdAt: LocalDateTime = LocalDateTime.now(), val authenticatedAt: LocalDateTime?) {
    constructor(email: String, authCode: String) : this(null, email, authCode, LocalDateTime.now(), null)

}
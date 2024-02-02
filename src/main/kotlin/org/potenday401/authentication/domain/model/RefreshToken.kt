package org.potenday401.authentication.domain.model

import java.time.LocalDateTime

class RefreshToken (val id: Int?, val memberId: Int, val refreshToken: String, val expiresAt: LocalDateTime) {
    constructor(memberId: Int, refreshToken: String, expiresAt: LocalDateTime)
            : this(null, memberId, refreshToken, expiresAt)
}
package org.potenday401.authentication.domain.model

import java.time.LocalDateTime

interface RefreshTokenRepository {

    fun findByRefreshToken(refreshToken: String): RefreshToken?
    fun create(refreshToken: RefreshToken)
    fun update(oldRefreshToken: String, newRefreshToken: String, newExpiresAt: LocalDateTime)
    fun deleteByMemberId(memberId: Int)
}
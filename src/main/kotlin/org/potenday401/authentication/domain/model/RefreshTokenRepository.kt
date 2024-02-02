package org.potenday401.authentication.domain.model

interface RefreshTokenRepository {

    fun create(refreshToken: RefreshToken)
}
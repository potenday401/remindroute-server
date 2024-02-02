package org.potenday401.authentication.application.service

import org.potenday401.authentication.application.dto.TokenPair
import java.time.LocalDateTime
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import org.potenday401.authentication.application.service.exception.InvalidTokenException
import org.potenday401.authentication.domain.model.RefreshToken
import org.potenday401.authentication.infrastructure.persistence.ExposedRefreshTokenRepository
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

class AuthenticationApplicationService(private val refreshTokenRepository: ExposedRefreshTokenRepository) {
    companion object {
        private val config = ConfigFactory.load()
        val issuer: String = config.getString("jwt.issuer")
        val accessLifetime = config.getLong("jwt.access.lifetime")
        val refreshLifetime = config.getLong("jwt.refresh.lifetime")
        val sign: Algorithm = Algorithm.HMAC256(config.getString("jwt.access.secret"))
    }
    fun generateTokenPair(memberId: Int, isUpdate: Boolean = false): TokenPair {
        val now = Instant.now()
        val accessTokenExpiresAt = now.plus(accessLifetime, ChronoUnit.MINUTES)

        val accessToken = JWT.create()
            .withSubject(memberId.toString())
            .withExpiresAt(accessTokenExpiresAt)
            .withIssuer(issuer)
            .sign(sign)

        val refreshToken = UUID.randomUUID().toString()
        val refreshTokenExpiresAt = now.plus(refreshLifetime, ChronoUnit.DAYS)
        val localDateTimeExpiresAt = LocalDateTime.ofInstant(refreshTokenExpiresAt, ZoneId.systemDefault());

        if(!isUpdate) {
            refreshTokenRepository.create(RefreshToken(memberId, refreshToken, localDateTimeExpiresAt))
        }
        return TokenPair(accessToken, refreshToken)
    }

    fun getRefreshToken(refreshToken: String): RefreshToken {
        return refreshTokenRepository.findByRefreshToken(refreshToken) ?: throw InvalidTokenException()
    }

    fun updateRefreshToken(oldRefreshToken: String, newRefreshToken: String, newExpiresAt: LocalDateTime) {
        return refreshTokenRepository.update(oldRefreshToken, newRefreshToken, newExpiresAt)
    }

    fun deleteRefreshTokenByMemberId(memberId: Int) {
        refreshTokenRepository.deleteByMemberId(memberId)
    }
}
package org.potenday401.member.domain.model

import java.time.LocalDateTime

interface EmailAuthenticationRepository {

    fun findById(id: Int): EmailAuthentication?
    fun findByEmail(email: String): List<EmailAuthentication>?

    fun findLatestByEmail(email: String): EmailAuthentication?

    fun create(emailAuthentication: EmailAuthentication)

    fun updateAuthenticatedAt(id: Int, authenticatedAt: LocalDateTime)

}
package org.potenday401.member.infrastructure.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.member.domain.model.EmailAuthentication
import org.potenday401.member.domain.model.EmailAuthenticationRepository
import java.time.LocalDateTime


object EmailAuthenticationTable : Table("email_authentication") {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 256)
    val authCode = varchar("auth_code", 16)
    val createdAt = datetime("created_at")
    val authenticatedAt = datetime("authenticated_at")
    override val primaryKey = PrimaryKey(id)
}
class ExposedEmailAuthenticationRepository : EmailAuthenticationRepository {
    override fun findById(id: Int): EmailAuthentication? {
        TODO("Not yet implemented")
    }

    override fun findByEmail(email: String): List<EmailAuthentication> {
        return transaction {
            EmailAuthenticationTable.select { EmailAuthenticationTable.email eq email }
                .map {row -> EmailAuthentication(
                    id = row[EmailAuthenticationTable.id],
                    email = row[EmailAuthenticationTable.email],
                    authCode = row[EmailAuthenticationTable.authCode],
                    createdAt = row[EmailAuthenticationTable.createdAt],
                    authenticatedAt = row[EmailAuthenticationTable.authenticatedAt]
                )
            }
        }
    }

    override fun findLatestByEmail(email: String): EmailAuthentication? {
        return transaction {
            val result = EmailAuthenticationTable.select { EmailAuthenticationTable.email eq email }
                .orderBy(EmailAuthenticationTable.createdAt to SortOrder.DESC)
                .limit(1)
                .firstOrNull()?.let {
                    EmailAuthentication(
                        id = it[EmailAuthenticationTable.id],
                        email = it[EmailAuthenticationTable.email],
                        authCode = it[EmailAuthenticationTable.authCode],
                        createdAt = it[EmailAuthenticationTable.createdAt],
                        authenticatedAt = it[EmailAuthenticationTable.authenticatedAt]
                    )
                }
            return@transaction result
        }
    }

    override fun create(emailAuthentication: EmailAuthentication) {
        transaction {
            EmailAuthenticationTable.insert {
                it[email] = emailAuthentication.email
                it[authCode] = emailAuthentication.authCode
                it[createdAt] = emailAuthentication.createdAt
            }
        }
    }

    override fun updateAuthenticatedAt(id: Int, authenticatedAt: LocalDateTime) {
        EmailAuthenticationTable.update ({ EmailAuthenticationTable.id eq id }) {
            it[EmailAuthenticationTable.authenticatedAt] = authenticatedAt
        }
    }


}


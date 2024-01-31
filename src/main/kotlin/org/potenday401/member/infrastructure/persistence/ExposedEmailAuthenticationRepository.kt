package org.potenday401.member.infrastructure.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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

    override fun findByEmail(email: String): EmailAuthentication? {
        TODO("Not yet implemented")
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


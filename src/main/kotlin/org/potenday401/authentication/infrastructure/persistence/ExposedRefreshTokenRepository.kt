package org.potenday401.authentication.infrastructure.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.authentication.domain.model.RefreshToken
import org.potenday401.authentication.domain.model.RefreshTokenRepository
import org.potenday401.member.infrastructure.persistence.MemberTable


object RefreshTokenTable : Table("refresh_token") {
    val id = integer("id").autoIncrement()
    val memberId = integer("member_id") references MemberTable.id
    val refreshToken = varchar("refresh_token", 300)
    val expiresAt = datetime("expires_at")
    override val primaryKey = PrimaryKey(id)
}
class ExposedRefreshTokenRepository : RefreshTokenRepository {
    override fun create(refreshToken: RefreshToken) {
        transaction {
            RefreshTokenTable.insert {
                it[RefreshTokenTable.memberId] = refreshToken.memberId
                it[RefreshTokenTable.refreshToken] = refreshToken.refreshToken
                it[RefreshTokenTable.expiresAt] = refreshToken.expiresAt
            }
        }
    }
}
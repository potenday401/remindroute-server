package org.potenday401.authentication.infrastructure.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.authentication.domain.model.RefreshToken
import org.potenday401.authentication.domain.model.RefreshTokenRepository
import org.potenday401.member.infrastructure.persistence.MemberTable
import java.time.LocalDateTime


object RefreshTokenTable : Table("refresh_token") {
    val id = integer("id").autoIncrement()
    val memberId = integer("member_id") references MemberTable.id
    val refreshToken = varchar("refresh_token", 300)
    val expiresAt = datetime("expires_at")
    override val primaryKey = PrimaryKey(id)
}

class ExposedRefreshTokenRepository : RefreshTokenRepository {
    override fun findByRefreshToken(refreshToken: String): RefreshToken? {
        return transaction {
            RefreshTokenTable.select { RefreshTokenTable.refreshToken eq refreshToken }
                .firstOrNull()?.let {
                    RefreshToken(
                        id = it[RefreshTokenTable.id],
                        memberId = it[RefreshTokenTable.memberId],
                        refreshToken = it[RefreshTokenTable.refreshToken],
                        expiresAt = it[RefreshTokenTable.expiresAt]
                    )
                }
        }
    }

    override fun create(refreshToken: RefreshToken) {
        transaction {
            RefreshTokenTable.insert {
                it[memberId] = refreshToken.memberId
                it[RefreshTokenTable.refreshToken] = refreshToken.refreshToken
                it[expiresAt] = refreshToken.expiresAt
            }
        }
    }

    override fun update(oldRefreshToken: String, newRefreshToken: String, newExpiresAt: LocalDateTime) {
        transaction {
            RefreshTokenTable.update({ RefreshTokenTable.refreshToken eq oldRefreshToken }) {
                it[refreshToken] = newRefreshToken
                it[expiresAt] = newExpiresAt
            }
        }
    }

    override fun deleteByMemberId(memberId: Int) {
        transaction {
            RefreshTokenTable.deleteWhere { RefreshTokenTable.memberId eq memberId }
        }
    }
}
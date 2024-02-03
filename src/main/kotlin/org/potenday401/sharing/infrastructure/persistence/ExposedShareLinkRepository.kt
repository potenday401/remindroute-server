package org.potenday401.sharing.infrastructure.persistence

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.sharing.domain.model.ShareLink
import org.potenday401.sharing.domain.model.ShareLinkRepository


object ShareLinkTable : Table("share_link") {
    val id = integer("id").autoIncrement()
    val memberId = varchar("member_id", 64)
    val ogKey = varchar("og_key", 64)
    val ogUrl = varchar("og_url",300)
    val deepLink = varchar("deep_link",300)
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}
class ExposedShareLinkRepository : ShareLinkRepository {
    override fun create(shareLink: ShareLink) {
        return transaction {
            ShareLinkTable.insert {
                it[memberId] = shareLink.memberId
                it[ogKey] = shareLink.ogKey
                it[ogUrl] = shareLink.ogUrl
                it[deepLink] = shareLink.deepLink
                it[createdAt] = shareLink.createdAt
            }
        }
    }

    override fun findByOgKey(ogKey: String): ShareLink? {
        return transaction {
            val result = ShareLinkTable.select(ShareLinkTable.ogKey eq ogKey )
                .firstOrNull()?.let {
                    ShareLink(
                        id = it[ShareLinkTable.id],
                        memberId = it[ShareLinkTable.memberId],
                        ogKey = it[ShareLinkTable.ogKey],
                        ogUrl = it[ShareLinkTable.ogUrl],
                        deepLink = it[ShareLinkTable.deepLink],
                        createdAt = it[ShareLinkTable.createdAt]
                    )
                }
            return@transaction result
        }
    }
}
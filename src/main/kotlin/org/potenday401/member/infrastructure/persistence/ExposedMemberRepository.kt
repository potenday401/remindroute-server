package org.potenday401.member.infrastructure.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.member.domain.model.Member
import org.potenday401.member.domain.model.MemberRepository

object MemberTable : Table("member") {
    val email = varchar("email", 256)
    val nickname = varchar("nickname", 128)
    val password = varchar("password", 128)
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")

    override val primaryKey = PrimaryKey(email)
}
class ExposedMemberRepository : MemberRepository {

    override fun findByEmail(email: String): Member? {
        TODO("Not yet implemented")
    }

    override fun create(member: Member) {
        transaction {
            MemberTable.insert {
                it[email] = member.email
                it[nickname] = member.nickname
                it[password] = member.password
                it[createdAt] = member.createdAt
                it[modifiedAt] = member.modifiedAt
            }
        }
    }
}
package org.potenday401.member.infrastructure.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.member.domain.model.Member
import org.potenday401.member.domain.model.MemberRepository

object MemberTable : Table("member") {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 256)
    val nickname = varchar("nickname", 128)
    val password = varchar("password", 128)
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")

    override val primaryKey = PrimaryKey(id)
}

class ExposedMemberRepository : MemberRepository {
    override fun findById(id: Int): Member? {
        return findMemberByCondition { MemberTable.id eq id }
    }

    override fun findByEmail(email: String): Member? {
        return findMemberByCondition { MemberTable.email eq email }
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

    override fun update(member: Member) {
        TODO("비밀번호 재설정. 닉네임 재설정(?)")
    }

    private fun findMemberByCondition(condition: (SqlExpressionBuilder.() -> Op<Boolean>)): Member? {
        return transaction {
            val result = MemberTable.select(condition)
                .firstOrNull()?.let {
                    Member(
                        id = it[MemberTable.id],
                        email = it[MemberTable.email],
                        createdAt = it[MemberTable.createdAt],
                        modifiedAt = it[MemberTable.modifiedAt],
                        nickname = it[MemberTable.nickname],
                        password = it[MemberTable.password]
                    )
                }
            return@transaction result
        }
    }

}
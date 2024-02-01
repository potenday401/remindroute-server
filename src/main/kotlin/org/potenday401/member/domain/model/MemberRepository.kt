package org.potenday401.member.domain.model

interface MemberRepository {
    fun findById(id: Int): Member?
    fun findByEmail(email: String): Member?
    fun create(member: Member)
}
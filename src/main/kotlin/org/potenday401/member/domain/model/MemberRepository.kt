package org.potenday401.member.domain.model

interface MemberRepository {

    fun findByEmail(email: String): Member?
    fun create(member: Member)
}
package org.potenday401.member.domain.model

import java.time.LocalDateTime

class Member(val email: String, val nickname: String, val password: String, val createdAt: LocalDateTime = LocalDateTime.now(), val modifiedAt: LocalDateTime = LocalDateTime.now()) {

}
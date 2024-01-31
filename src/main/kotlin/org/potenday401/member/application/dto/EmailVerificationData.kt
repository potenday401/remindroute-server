package org.potenday401.member.application.dto

import kotlinx.serialization.Serializable

@Serializable
class EmailVerificationData(val email:String, val authCode:String) {
}
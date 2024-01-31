package org.potenday401.member.application.dto

import kotlinx.serialization.Serializable


@Serializable
class MemberCreationData(val email:String, val password:String, val confirmPassword:String, val verifiedToken:String) {
}
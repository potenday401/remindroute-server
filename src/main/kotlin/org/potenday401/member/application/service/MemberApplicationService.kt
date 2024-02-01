package org.potenday401.member.application.service

import org.potenday401.member.application.dto.MemberCreationData
import org.potenday401.member.application.service.exception.*
import org.potenday401.member.domain.model.EmailAuthentication
import org.potenday401.member.domain.model.EmailAuthenticationRepository
import org.potenday401.member.domain.model.Member
import org.potenday401.member.domain.model.MemberRepository
import org.potenday401.util.EmailUtil
import org.potenday401.util.validator.EmailValidator
import org.potenday401.util.PasswordUtil
import java.lang.RuntimeException
import java.time.LocalDateTime

class MemberApplicationService(
    private val memberRepository: MemberRepository,
    private val emailAuthenticationRepository: EmailAuthenticationRepository
) {

    companion object {
        private const val VALIDITY_DURATION_MINUTES = 10
    }

    fun sendAuthCodeEmail(email: String) {

        if(!EmailValidator.isValid(email)) {
            throw InvalidEmailFormatException()
        }

        if(memberRepository.findByEmail(email) != null) {
            throw MemberAlreadyExistsException()
        }

        val authCode = PasswordUtil.generateRandom5DigitCode()
        val subject = "[서비스명] 서비스 가입을 위한 인증 코드"
        val message = "인증번호를 앱에 입력하세요.\n인증번호: $authCode"

        try {
            EmailUtil.sendEmail(subject, message, email)
        } catch (ex: RuntimeException) {
            throw EmailSendingFailedException()
        }

        val emailAuthentication = EmailAuthentication(email, authCode)
        emailAuthenticationRepository.create(emailAuthentication)
    }

    fun generateVerifiedTokenIfValid(email: String, authCode: String): String {
        val request = emailAuthenticationRepository.findLatestByEmail(email)
        if(request == null) {
            throw RequestNotFoundException()
        } else {
            if(request.authCode != authCode) {
                throw InvalidAuthCodeException()
            }
            if(isExpired(request)) {
                throw RequestExpiredException()
            }
        }
        emailAuthenticationRepository.updateAuthenticatedAt(request.id!!, LocalDateTime.now())
        return request.id.toString() // 토큰은 일단 ID값으로 한다.
    }

    fun createMember(memberCreationData: MemberCreationData) {
        val request = emailAuthenticationRepository.findById(memberCreationData.verifiedToken.toInt())
        if(isExpired(request!!)) {
            throw RequestExpiredException()
        }
        if(!PasswordUtil.arePasswordsMatching(memberCreationData.password, memberCreationData.confirmPassword)) {
            throw PasswordMismatchException()
        }
        if(memberRepository.findByEmail(request.email) != null) {
            throw MemberAlreadyExistsException()
        }
        val encodePassword = PasswordUtil.encodePassword(memberCreationData.password)
        val nickname = extractLocalPart(memberCreationData.email)
        val member = Member(memberCreationData.email, nickname, encodePassword)
        memberRepository.create(member)
    }

    fun resetPassword() {
        TODO("비밀번호 재설정. 인증 받고 -> 재설정 필요")
    }
    private fun extractLocalPart(email: String): String {
        val atIndex = email.indexOf('@')
        return email.substring(0, atIndex)
    }

    private fun isExpired(emailAuthentication: EmailAuthentication): Boolean {
        return if (emailAuthentication.authenticatedAt == null) {
            emailAuthentication.createdAt.plusMinutes(VALIDITY_DURATION_MINUTES.toLong()) < LocalDateTime.now()
        } else {
            emailAuthentication.authenticatedAt.plusMinutes(VALIDITY_DURATION_MINUTES.toLong()) < LocalDateTime.now()
        }
    }


}
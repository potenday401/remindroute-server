package org.potenday401.member.infrastructure.restapi

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.potenday401.member.application.dto.EmailVerificationData
import org.potenday401.member.application.dto.MemberCreationData
import org.potenday401.member.application.dto.PreSignupData
import org.potenday401.member.application.service.MemberApplicationService
import org.potenday401.member.application.service.exception.*

fun Route.memberRouting(memberAppService : MemberApplicationService) {
    route("/member") {
        post("pre-signup") {
            val preSignupData =  call.receive<PreSignupData>()
            try {
                memberAppService.sendAuthCodeEmail(preSignupData.email)
                call.respondText("Email sent successfully", status = HttpStatusCode.OK)
            } catch (e: InvalidEmailFormatException) {
                call.respondText("Invalid email format", status = HttpStatusCode.BadRequest)
            } catch(e: EmailSendingFailedException) {
                call.respondText("Failed to send email", status = HttpStatusCode.InternalServerError)
            } catch(e: MemberAlreadyExistsException) {
                call.respondText("User already exists", status = HttpStatusCode.Conflict)
            }
        }

        post("/email-verification") {
            try {
                val emailVerificationData =  call.receive<EmailVerificationData>()
                val email = emailVerificationData.email
                val authCode = emailVerificationData.authCode
                val verifiedToken = memberAppService.generateVerifiedTokenIfValid(email, authCode)
                call.respond(verifiedToken)
            } catch(e: RequestNotFoundException) {
                call.respondText("Request not found", status = HttpStatusCode.NotFound)
            } catch(e: InvalidAuthCodeException) {
                call.respondText("Invalid authentication code", status = HttpStatusCode.BadRequest)
            } catch(e: RequestExpiredException) {
                call.respondText("Request has expired", status = HttpStatusCode.BadRequest)
            }
        }

        post("/signup") {
            val memberCreationData =  call.receive<MemberCreationData>()
            try {
                memberAppService.createMember(memberCreationData)
                call.respondText("Registration successful", status = HttpStatusCode.Created)
            } catch(e : RequestExpiredException) {
                call.respondText("Request has expired", status = HttpStatusCode.BadRequest)
            } catch(e : PasswordMismatchException) {
                call.respondText("Passwords do not match", status = HttpStatusCode.BadRequest)
            }
        }


    }


}
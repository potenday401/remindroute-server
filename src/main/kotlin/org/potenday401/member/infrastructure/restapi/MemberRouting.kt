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
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route

import org.potenday401.member.application.dto.VerifiedTokenData

fun Route.memberRouting(memberAppService: MemberApplicationService) {
    route("/member", {
        tags = listOf("member operation")
    }) {
        post("pre-signup", {
            description = "pre-signup : send authentication code email"
            request {
                body<PreSignupData>()
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid email format"
                }
                HttpStatusCode.Conflict to {
                    description = "User already exists"
                }
                HttpStatusCode.InternalServerError to {
                    description = "Failed to send email due to a server error"
                }
            }
        })
        {
            val preSignupData = call.receive<PreSignupData>()
            try {
                memberAppService.sendAuthCodeEmail(preSignupData.email)
                call.respondText("Email sent successfully", status = HttpStatusCode.OK)
            } catch (e: InvalidEmailFormatException) {
                call.respondText("Invalid email format", status = HttpStatusCode.BadRequest)
            } catch (e: EmailSendingFailedException) {
                call.respondText("Failed to send email", status = HttpStatusCode.InternalServerError)
            } catch (e: MemberAlreadyExistsException) {
                call.respondText("User already exists", status = HttpStatusCode.Conflict)
            }
        }

        post("/email-verification", {
            description = "email-verification : verify the email authentication code and issue a verified token"
            request {
                body<EmailVerificationData>()
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<VerifiedTokenData>()
                }
                HttpStatusCode.NotFound to {
                    description = "Request not found"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid authentication code"
                }
                HttpStatusCode.BadRequest to {
                    description = "Request has expired"
                }
            }
        }){
            try {
                val emailVerificationData = call.receive<EmailVerificationData>()
                val email = emailVerificationData.email
                val authCode = emailVerificationData.authCode
                val verifiedToken = memberAppService.generateVerifiedTokenIfValid(email, authCode)
                call.respond(HttpStatusCode.OK, VerifiedTokenData(verifiedToken))
            } catch (e: RequestNotFoundException) {
                call.respondText("Request not found", status = HttpStatusCode.NotFound)
            } catch (e: InvalidAuthCodeException) {
                call.respondText("Invalid authentication code", status = HttpStatusCode.BadRequest)
            } catch (e: RequestExpiredException) {
                call.respondText("Request has expired", status = HttpStatusCode.BadRequest)
            }
        }

        post("/signup", {
            description = "sign up"
            request {
                body<MemberCreationData>()
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                }
                HttpStatusCode.BadRequest to {
                    description = "Request has expired or Passwords do not match"
                }
                HttpStatusCode.BadRequest to {
                    description = "Passwords do not match"
                }
                HttpStatusCode.BadRequest to {
                    description = "User already exists"
                }
            }
        }) {
            val memberCreationData = call.receive<MemberCreationData>()
            try {
                memberAppService.createMember(memberCreationData)
                call.respondText("Registration successful", status = HttpStatusCode.Created)
            } catch (e: RequestExpiredException) {
                call.respondText("Request has expired", status = HttpStatusCode.BadRequest)
            } catch (e: PasswordMismatchException) {
                call.respondText("Passwords do not match", status = HttpStatusCode.BadRequest)
            } catch (e: MemberAlreadyExistsException) {
                call.respondText("User already exists", status = HttpStatusCode.BadRequest)
            }
        }


    }


}
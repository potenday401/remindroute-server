package org.potenday401.authentication.infrastructure.restapi

import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.potenday401.authentication.application.dto.LoginMember
import org.potenday401.authentication.application.dto.RefreshTokenData
import org.potenday401.authentication.application.dto.TokenPair
import org.potenday401.authentication.application.service.AuthenticationApplicationService
import org.potenday401.authentication.application.service.exception.InvalidTokenException
import org.potenday401.member.application.service.MemberApplicationService
import org.potenday401.util.PasswordUtil
import java.lang.RuntimeException
import java.time.LocalDateTime

fun Route.AuthenticationRouting(
    authAppService: AuthenticationApplicationService,
    memberAppService: MemberApplicationService
) {
    route("/auth", {
        tags = listOf("authentication operation")
    }) {
        authenticate("auth-jwt") {
            get("/test", {
                securitySchemeName = "auth-jwt-scheme"
                description = "auth-test"
                response {
                    HttpStatusCode.OK to {
                        description = "authenticated user"
                    }
                }
            })  {
                call.respondText("authenticated user", status = HttpStatusCode.OK)
            }
        }

        post("/login", {
            description = "login"
            request {
                body<LoginMember>()
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<TokenPair>()
                }
                HttpStatusCode.BadRequest to {
                    description = "incorrect email or password."
                }
            }
        }) {
            val authMember = call.receive<LoginMember>()
            val member = memberAppService.getMemberByEmail(authMember.email)
            if (member != null && PasswordUtil.verifyPassword(authMember.password, member.password)) {
                authAppService.deleteRefreshTokenByMemberId(member.id!!)
                val tokenPair = authAppService.generateTokenPair(member.id!!)
                call.respond(HttpStatusCode.OK, tokenPair)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/refresh", {
            description = "refresh the token"
            request {
                body<RefreshTokenData>()
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<TokenPair>()
                }
                HttpStatusCode.Unauthorized to {
                    description = "refresh-token is not valid or has expired"
                }
            }
        }) {
            val oldRefreshTokenInRequest = call.receive<RefreshTokenData>().refreshToken
            try {
                val oldRefreshToken = authAppService.getRefreshToken(oldRefreshTokenInRequest)
                val now = LocalDateTime.now()
                if (oldRefreshToken.expiresAt > now) {
                    val tokenPair = authAppService.generateTokenPair(oldRefreshToken.memberId, true)
                    authAppService.updateRefreshToken(oldRefreshTokenInRequest, tokenPair.refreshToken, now)
                    call.respond(HttpStatusCode.OK, tokenPair)
                } else {
                    throw InvalidTokenException()
                }
            } catch (e: InvalidTokenException) {
                call.respondText("refresh-token is not valid or has expired", status = HttpStatusCode.Unauthorized)
            } catch (e: RuntimeException) {
                call.respondText("InternalServerError", status = HttpStatusCode.InternalServerError)
            }
        }
    }

}
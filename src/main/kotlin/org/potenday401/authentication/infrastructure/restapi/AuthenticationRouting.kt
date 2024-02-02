package org.potenday401.authentication.infrastructure.restapi

import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.potenday401.authentication.application.dto.LoginMember
import org.potenday401.member.application.service.MemberApplicationService
import org.potenday401.util.PasswordUtil

fun Route.AuthenticationRouting(memberAppService: MemberApplicationService) {
    route("/auth", {
        tags = listOf("authentication operation")
    }) {
        post("/api/login") {
            val authMember = call.receive<LoginMember>()
            val member = memberAppService.getMemberByEmail(authMember.email)
            if(member != null && PasswordUtil.verifyPassword(authMember.password, member.password)) {

            }

            if (user != null && Bcrypt.verify(authUser.password, user.password)) {
                val id = user.id

                val tokenPair = generateTokenPair(id)
                call.respond(tokenPair)
            } else
                call.respond(HttpStatusCode.Unauthorized)
        }
    }

}
package org.potenday401

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.potenday401.member.application.service.MemberApplicationService
import org.potenday401.member.infrastructure.persistence.ExposedEmailAuthenticationRepository
import org.potenday401.member.infrastructure.persistence.ExposedMemberRepository
import org.potenday401.plugins.*
import org.potenday401.tag.application.service.TagApplicationService
import org.potenday401.tag.infrastructure.persistence.ExposedTagRepository


fun main() {
    val applicationConfig = ConfigFactory.load()
    val port = applicationConfig.getInt("ktor.deployment.port")

    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val memberRepository = ExposedMemberRepository()
    val emailAuthenticationRepository = ExposedEmailAuthenticationRepository()
    val memberAppService = MemberApplicationService(memberRepository, emailAuthenticationRepository)

    val tagRepository = ExposedTagRepository()
    val tagAppService = TagApplicationService(tagRepository)

    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureTemplating()
    configureRouting(tagAppService, memberAppService)

}

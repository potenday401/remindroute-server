package org.potenday401

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.potenday401.plugins.*
import org.potenday401.tag.application.service.TagApplicationService
import org.potenday401.tag.domain.model.TagRepository
import org.potenday401.tag.infrastructure.persistence.ExposedTagRepository

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val tagRepository = ExposedTagRepository()
    val tagAppService = TagApplicationService(tagRepository)

    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureTemplating()
    configureRouting(tagAppService)
}

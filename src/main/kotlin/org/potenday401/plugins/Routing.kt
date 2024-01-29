package org.potenday401.plugins

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.potenday401.tag.infrastructure.restapi.tagRouting

fun Application.configureRouting() {
    install(Resources)
    routing {
        get("/hello") {
            call.respond("Hello")
        }

        tagRouting()
    }
}

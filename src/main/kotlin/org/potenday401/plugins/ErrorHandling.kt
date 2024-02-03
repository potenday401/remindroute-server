package org.potenday401.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import java.io.PrintWriter
import java.io.StringWriter

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val sw = StringWriter().apply {
                cause.printStackTrace(PrintWriter(this))
            }
            val text = "Exception: ${cause::class.simpleName}\nMessage: ${cause.message}\nStack Trace:\n$sw"

            call.respondText(text, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
}

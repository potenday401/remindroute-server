package org.potenday401.tag.infrastructure.restapi

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.potenday401.tag.application.dto.TagData

fun Route.tagRouting() {
    route("/tags") {
        get("{id}") {
            val id = call.parameters["id"]
            if(id.isNullOrEmpty()) {
                return@get call.respondText("id is required", status = HttpStatusCode.BadRequest)
            }

            val tagData = TagData("${id}", "name", 0, 0)
            call.respond(tagData)
        }
    }
}

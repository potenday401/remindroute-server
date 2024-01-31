package org.potenday401.tag.infrastructure.restapi

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.potenday401.tag.application.dto.TagCreationData
import org.potenday401.tag.application.service.TagApplicationService

fun Route.tagRouting(tagAppService: TagApplicationService) {
    route("/tags") {
        get("{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText("id is required", status = HttpStatusCode.BadRequest)
            }

            val tag = tagAppService.getTagById(id)
            if (tag == null) {
                return@get call.respondText("not found", status = HttpStatusCode.NotFound)
            }
            call.respond(tag)
        }

        get("/") {
            val tags = tagAppService.getAllTags()
            call.respond(tags)
        }

        get("/by-name") {
            val names = call.request.queryParameters.getAll("name")
            if (names.isNullOrEmpty()) {
                return@get call.respondText("names are required", status = HttpStatusCode.BadRequest)
            }

            val tags = tagAppService.getAllTagsByNameIn(names)
            call.respond(tags)
        }

        post("/") {
            val tagCreationData = call.receive<TagCreationData>()
            tagAppService.createTag(tagCreationData)
            call.respondText("Tag created successfully", status = HttpStatusCode.Created)
        }
    }
}

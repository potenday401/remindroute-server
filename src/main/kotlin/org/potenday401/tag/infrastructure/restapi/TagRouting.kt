package org.potenday401.tag.infrastructure.restapi

import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.potenday401.tag.application.dto.TagCreationData
import org.potenday401.tag.application.dto.TagData
import org.potenday401.tag.application.service.TagApplicationService

fun Route.tagRouting(tagAppService: TagApplicationService) {
    route("/tags", {
        tags = listOf("tags operation")
    }) {
        get("{id}", {
            description = "get tag by id"
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<TagData> { description = "tag data" }
                }
                HttpStatusCode.NotFound to {
                    description = "not found"
                }
                HttpStatusCode.InternalServerError to {
                    description = "exception"
                }
            }
        }) {
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

        get( {
            description = "get tags"
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<List<TagData>> { description = "tag data" }
                }
                HttpStatusCode.NotFound to {
                    description = "not found"
                }
                HttpStatusCode.InternalServerError to {
                    description = "exception"
                }
            }
        }) {
            val tags = tagAppService.getAllTags()
            call.respond(tags)
        }

        get("/by-name",{
            description = "get tags by name"
            request {
                queryParameter<String>("name") {
                    description = "tag name"
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<List<TagData>> { description = "tag data" }
                }
                HttpStatusCode.NotFound to {
                    description = "not found"
                }
                HttpStatusCode.InternalServerError to {
                    description = "exception"
                }
            }
        }) {
            val names = call.request.queryParameters.getAll("name")
            if (names.isNullOrEmpty()) {
                return@get call.respondText("names are required", status = HttpStatusCode.BadRequest)
            }

            val tags = tagAppService.getAllTagsByNameIn(names)
            call.respond(tags)
        }

        post( {
            description = "create tag"
            request {
                body<TagCreationData>()
            }
            response {
                HttpStatusCode.Created to {
                    description = "success"

                }
            }
        } ) {
            val tagCreationData = call.receive<TagCreationData>()
            tagAppService.createTag(tagCreationData)
            call.respondText("Tag created successfully", status = HttpStatusCode.Created)
        }
    }
}

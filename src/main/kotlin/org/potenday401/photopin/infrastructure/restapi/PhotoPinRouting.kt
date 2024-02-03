package org.potenday401.photopin.infrastructure.restapi

import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.patch
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.potenday401.photopin.application.dto.*

import org.potenday401.photopin.application.service.PhotoPinApplicationService
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.photopin.infrastructure.persistence.PhotoPinQueries

fun Route.photoPinRouting(photoPinAppService: PhotoPinApplicationService, photoPinQueries: PhotoPinQueries) {
    route("/photo-pins", {
        tags = listOf("photoPins operation")
    }) {
        get("{id}", {
            description = "get photoPin by id"
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<PhotoPinData> { description = "photoPin data" }
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

            val photoPin = photoPinAppService.getPhotoPinById(id)
            if (photoPin == null) {
                return@get call.respondText("not found", status = HttpStatusCode.NotFound)
            }
            call.respond(photoPin)
        }

        get({
            description = "get photoPins"
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<List<PhotoPinData>> { description = "photoPin data" }
                }
                HttpStatusCode.NotFound to {
                    description = "not found"
                }
                HttpStatusCode.InternalServerError to {
                    description = "exception"
                }
            }
        }) {
            val photoPins = photoPinAppService.getAllPhotoPins()
            call.respond(photoPins)
        }

        post({
            description = "create photoPin"
            request {
                body<PhotoPinCreationData>()
            }
            response {
                HttpStatusCode.Created to {
                    description = "success"

                }
            }
        }) {
            val photoPinCreationData = call.receive<PhotoPinCreationData>()
            photoPinAppService.createPhotoPin(photoPinCreationData)
            call.respondText("PhotoPin created successfully", status = HttpStatusCode.Created)
        }

        patch("{id}/content", {
            description = "patch photoPin by id"
            request {
                body<PhotoPinMutationData>()
            }
            response {
                HttpStatusCode.NoContent to {
                    description = "success"
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
                return@patch call.respondText("id is required", status = HttpStatusCode.BadRequest)
            }

            val photoPinMutationData = call.receive<PhotoPinMutationData>()
            photoPinAppService.updatePhotoPin(photoPinMutationData)

            call.respondText("not found", status = HttpStatusCode.NoContent)
        }
    }

    route("/tag-album", {
        tags = listOf("photoPins operation")
    }) {
        get({
            description = "get tagAlbum"
            request {
                queryParameter<String>("memberId") {
                    required = true
                }
                queryParameter<String>("sort") {
                    required = false
                    description = "sort=latest 로 설정하면 가장 최근에 작성된 photoPin 에 등록된 테그부터 차례로 정렬된 아이템을 되돌려 줍니다."
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<TagAlbumDocument> { description = "tagAlbumDocument data" }
                }
                HttpStatusCode.NotFound to {
                    description = "not found"
                }
                HttpStatusCode.InternalServerError to {
                    description = "exception"
                }
            }
        }) {
            val memberId = call.request.queryParameters["memberId"]
            if (memberId.isNullOrEmpty()) {
                return@get call.respondText("memberId is required", status = HttpStatusCode.BadRequest)
            }

            val sort = call.request.queryParameters["sort"]
            val tagAlbum = when (sort) {
                "latest" -> photoPinQueries.getTagAlbumDocumentOrderByCreatedAtDesc(memberId)
                null -> photoPinQueries.getTagAlbumDocument(memberId)
                else -> return@get call.respondText("not supported sort value. use 'latest'", status = HttpStatusCode.BadRequest)
            }

            call.respond(tagAlbum)
        }
    }

    route("/map-album", {
        tags = listOf("photoPins operation")
    }) {
        get({
            description = "startLatLng, endLatLng 범위의 모든 photoPin을 돌려줍니다."
            request {
                queryParameter<String>("memberId") {
                    required = true
                }
                queryParameter<String>("startLat") {
                    required = true
                    description = "Dobule 타입으로 변환 될 수 있는 숫자만 사용할 수 있습니다."
                }
                queryParameter<String>("startLng") {
                    required = true
                    description = "Dobule 타입으로 변환 될 수 있는 숫자만 사용할 수 있습니다."
                }
                queryParameter<String>("endLat") {
                    required = true
                    description = "Dobule 타입으로 변환 될 수 있는 숫자만 사용할 수 있습니다."
                }
                queryParameter<String>("endLng") {
                    required = true
                    description = "Dobule 타입으로 변환 될 수 있는 숫자만 사용할 수 있습니다."
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<MapAlbumDocument> { description = "tagAlbumDocument data" }
                }
                HttpStatusCode.NotFound to {
                    description = "not found"
                }
                HttpStatusCode.InternalServerError to {
                    description = "exception"
                }
            }
        }) {
            val memberId = call.request.queryParameters["memberId"]
            if (memberId.isNullOrEmpty()) {
                return@get call.respondText("memberId is required", status = HttpStatusCode.BadRequest)
            }

            val startLat = call.request.queryParameters["startLat"]?.toDoubleOrNull()
            if (startLat == null) {
                return@get call.respondText("startLat must be a valid number", status = HttpStatusCode.BadRequest)
            }

            val startLng = call.request.queryParameters["startLng"]?.toDoubleOrNull()
            if (startLng == null) {
                return@get call.respondText("startLng must be a valid number", status = HttpStatusCode.BadRequest)
            }

            val endLat = call.request.queryParameters["endLat"]?.toDoubleOrNull()
            if (endLat == null) {
                return@get call.respondText("endLat must be a valid number", status = HttpStatusCode.BadRequest)
            }

            val endLng = call.request.queryParameters["endLng"]?.toDoubleOrNull()
            if (endLng == null) {
                return@get call.respondText("endLng must be a valid number", status = HttpStatusCode.BadRequest)
            }

            val mapAlbum = photoPinQueries.getMapAlbumDocument(memberId, LatLng(startLat, startLng), LatLng(endLat, endLng))

            call.respond(mapAlbum)
        }
    }
}

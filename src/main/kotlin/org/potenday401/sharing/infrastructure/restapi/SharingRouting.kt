package org.potenday401.sharing.infrastructure.restapi

import org.potenday401.sharing.application.dto.ShareLinkCreationData
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.thymeleaf.*
import org.potenday401.sharing.application.dto.ShareUrlData
import org.potenday401.sharing.application.service.ShareLinkApplicationService

fun Route.SharingRouting(shareLinkAppService: ShareLinkApplicationService) {
    route("/share-link", {
        tags = listOf("sharing operation")
    }) {
        post ({
            description = "make share-link"
            request {
                body<ShareLinkCreationData>()
            }
            response {
                HttpStatusCode.OK to {
                    description = "success"
                    body<ShareUrlData>()
                }
            }
        }) {
            val shareLinkCreationData = call.receive<ShareLinkCreationData>()
            val imageUrl = shareLinkAppService.createOgImage(shareLinkCreationData)
            call.respond(HttpStatusCode.OK, ShareUrlData(shareLinkAppService.saveShareLinkMetadataAndGetUrl(shareLinkCreationData.memberId,imageUrl, shareLinkCreationData.deepLink)))
        }
    }

    route("/popin", {
        tags = listOf("share-link")
    }) {
        get ("/share/{ogkey}", {
            description = ""
            response {
                HttpStatusCode.OK to {
                    description = "Redirect to deep link on succes"
                }
                HttpStatusCode.NotFound to {
                    description = "can not find resource"
                }
            }
        }) {
            val ogKey = call.parameters["ogKey"]
            if(ogKey.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            }
            val shareLink = shareLinkAppService.findByOgKey(ogKey!!)
            if(shareLink != null) {
                call.respond(ThymeleafContent("toAppLink",
                    mapOf("here" to "${ShareLinkApplicationService.domain}/popin/share/$ogKey",
                        "ogImgUrl" to shareLink.ogUrl,
                        "deepLink" to shareLink.deepLink)))
            } else {
                call.respond(HttpStatusCode.NotFound)
            }

        }
    }

}
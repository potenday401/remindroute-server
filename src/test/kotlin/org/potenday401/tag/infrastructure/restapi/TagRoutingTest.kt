package org.potenday401.tag.infrastructure.restapi

import com.google.gson.Gson
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*
import org.potenday401.tag.application.dto.TagCreationData
import org.potenday401.tag.application.dto.TagData
import org.potenday401.tag.application.service.TagApplicationService
import java.nio.charset.Charset


class TagRoutingTest {

    private val tagAppService = mock<TagApplicationService>()

    @Test
    fun testGetById() = testApplication {
        application {
            install(ContentNegotiation) {
                gson { }
            }

            routing {
                tagRouting(tagAppService)
            }
        }

        val testTagId = "test-id"
        val testTag = TagData(testTagId, "test-member-id","test-name", 0L, 0L)
        `when`(tagAppService.getTagById(testTagId)).thenReturn(testTag)

        val response = client.get("/tags/" + testTagId)

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(Gson().toJson(testTag), response.bodyAsText(Charset.defaultCharset()))
    }

    @Test
    fun testCreate() = testApplication {
        application {
            install(ContentNegotiation) {
                gson { }
            }

            routing {
                tagRouting(tagAppService)
            }
        }

        val data = TagCreationData("test-id", "tag-member-id","test-name")

        val response = client.post("/tags") {
            contentType(ContentType.Application.Json)
            setBody(Gson().toJson(data))
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

}

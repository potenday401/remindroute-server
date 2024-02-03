package org.potenday401.photopin.infrastructure.restapi

import com.google.gson.Gson
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.potenday401.photopin.application.dto.LatLngData
import org.potenday401.photopin.application.dto.PhotoPinCreationData
import org.potenday401.photopin.application.service.PhotoPinApplicationService
import org.potenday401.photopin.application.service.PhotoPinDataConverter
import org.potenday401.photopin.domain.model.createMockPhotoPin1
import org.potenday401.photopin.infrastructure.persistence.PhotoPinQueries
import java.nio.charset.Charset


class PhotoPinRoutingTest {

    private val photoPinAppService = mock<PhotoPinApplicationService>()
    private val photoPinQueries = mock<PhotoPinQueries>()

    @Test
    fun testGetById() = testApplication {
        application {
            install(ContentNegotiation) {
                gson { }
            }

            routing {
                photoPinRouting(photoPinAppService, photoPinQueries)
            }
        }

        val mockPhotoPin1 = createMockPhotoPin1()
        val testPhotoPin = PhotoPinDataConverter.toPhotoPinData(mockPhotoPin1)
        `when`(photoPinAppService.getPhotoPinById(mockPhotoPin1.id)).thenReturn(testPhotoPin)

        val response = client.get("/photo-pins/" + mockPhotoPin1.id)

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(Gson().toJson(testPhotoPin), response.bodyAsText(Charset.defaultCharset()))
    }

    @Test
    fun testCreate() = testApplication {
        application {
            install(ContentNegotiation) {
                gson { }
            }

            routing {
                photoPinRouting(photoPinAppService, photoPinQueries)
            }
        }
        val mockPhotoPin1 = createMockPhotoPin1()
        val data = PhotoPinCreationData(
            mockPhotoPin1.id,
            mockPhotoPin1.memberId,
            mockPhotoPin1.tagIds,
            1L,
            "paylaod",
            "ext",
            LatLngData(6.0, 5.0),
            "seoul",
            "mapo"
        )

        val response = client.post("/photo-pins") {
            contentType(ContentType.Application.Json)
            setBody(Gson().toJson(data))
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

}

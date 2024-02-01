package org.potenday401.member.infrastructure.restapi


import io.ktor.serialization.gson.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import org.mockito.Mockito
import org.potenday401.member.application.service.MemberApplicationService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.junit.Test

class MemberRoutingTest {

    private val memberAppService = Mockito.mock<MemberApplicationService>()


    /**
     curl -X POST http://localhost:8080/member/pre-signup \
           -H "Content-Type: application/json" \
          -d '{"email": "the__dreamer@naver.com"}'
     */
    @Test
    fun testEmailVification() = testApplication {
        application {
            install(ContentNegotiation) {
                gson {  }
            }

            routing {
                memberRouting(memberAppService)
            }
        }


    }

}
package org.potenday401.plugins

import com.auth0.jwt.JWT
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.potenday401.authentication.application.service.AuthenticationApplicationService

fun Application.configureSecurity() {



    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT
                    .require(AuthenticationApplicationService.sign)
                    .withIssuer(AuthenticationApplicationService.issuer)
                    .build())

            validate { token ->
                if (token.payload.expiresAt.time > System.currentTimeMillis())
                    JWTPrincipal(token.payload)
                else null
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    authentication {
        oauth("auth-oauth-google") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
                )
            }
            client = HttpClient(Apache)
        }
    }
}

package org.potenday401.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.SwaggerUiSyntaxHighlight
import io.ktor.server.application.*

fun Application.configureSwagger() {
    install(SwaggerUI) {
        swagger {
            swaggerUrl = "docs"
            forwardRoot = true
            displayOperationId = true
            showTagFilterInput = true
            syntaxHighlight = SwaggerUiSyntaxHighlight.OBSIDIAN
        }
        info {
            title = "popin API"
            version = "latest"
            description = "popin api"
        }
    }
}
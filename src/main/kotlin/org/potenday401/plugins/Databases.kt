package org.potenday401.plugins

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val applicationConfig = ConfigFactory.load()
    val dbConfig = applicationConfig.getConfig("ktor.database")
    val url = dbConfig.getString("url")
    val driver = dbConfig.getString("driver")
    val user = dbConfig.getString("user")
    val password = dbConfig.getString("password")

    val database = Database.connect(
        url = url,
        user = user,
        driver = driver,
        password = password
    )
}

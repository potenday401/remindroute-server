package org.potenday401.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3310/remindroute",
        user = "root",
        driver = "com.mysql.cj.jdbc.Driver",
        password = "mysqlrootpw"
    )
}

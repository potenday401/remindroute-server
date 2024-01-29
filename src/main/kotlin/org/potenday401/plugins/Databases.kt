package org.potenday401.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3309/remoindroute",
        user = "root",
        driver = "com.mysql.cj.jdbc.Driver",
        password = "mysqlrootpw"
    )
}

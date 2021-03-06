package com.example

import api.access.UralsCSVAccess
import com.example.mods.ModGetAverage
import com.example.mods.ModGetData
import com.example.mods.ModMinMax
import com.example.statistics.StatTrack
import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import java.io.File
import java.lang.StringBuilder

private val raw_api = UralsCSVAccess(File("./resources/source.csv"))
private val api = StatTrack(raw_api)
private val mods = hashMapOf(
    "stat" to api,
    "date" to ModGetData(api),
    "get_avg" to ModGetAverage(api),
    "min_max" to ModMinMax(api)
)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging)
    routing {

        get("/") {
            val response = StringBuilder()
            for ((k, v) in mods) {
                response.append("/").append(k).append(" - ").append(v.doc()).append("\n\n")
            }
            call.respond(response.toString())
        }

        for ((k, v) in mods) {
            get(k) { v.perform(call) }
        }

        // Silently ignore requests for nonexistent mods
        get { }
    }
}


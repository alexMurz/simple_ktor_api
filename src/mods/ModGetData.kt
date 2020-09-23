package com.example.mods

import api.access.Access
import com.example.mods.Mod.Companion.err
import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import java.time.LocalDate

class ModGetData(private val access: Access) : Mod {
    override fun doc() = """
        Get value for given date
        Params
         - date=YYYY-MM-DD - Date
    """.trimIndent()

    override suspend fun perform(call: ApplicationCall) {
        val dateString: String = call.request.queryParameters["date"] ?: run {
            return call.respond(err("expecting date param with format YYYY-MM-DD"))
        }
        try {
            val date = LocalDate.parse(dateString)
            call.respond(access.getForDate(date)?.value.toString())
        } catch (_: Throwable) {
            call.respond(err("Unable to parse date"))
        }
    }

}
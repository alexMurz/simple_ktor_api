package com.example.mods

import access.Access
import com.example.api
import com.example.mods.Mod.Companion.err
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import java.time.LocalDate

class ModGetData(val access: Access) : Mod {
    override fun doc() = """
        Get value for given date
        Params
         - date=YYYY-MM-DD - Date
    """.trimIndent()

    override suspend fun perform(call: ApplicationCall) {
        val dateString: String = call.request.queryParameters["date"] ?: run {
            return call.respond(err("/date expecting date param with format YYYY-MM-DD"))
        }
        try {
            val date = LocalDate.parse(dateString)
            call.respond(api.getForDate(date)?.value.toString())
        } catch (_: Throwable) {
            call.respond(err("Unable to parse date"))
        }
    }

}
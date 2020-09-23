package com.example.mods

import api.access.Access
import com.example.mods.Mod.Companion.err
import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import java.time.LocalDate

class ModGetAverage(private val access: Access): Mod {
    override fun doc() = """
        Get average over given date range.
        Returns NaN if no data in range
        Params
         - lo - Lower bound YYYY-MM-DD, Inclusive
         - hi - Upper bound YYYY-MM-DD, Exclusive
         - optional: strict - Default: false. Then true will require that data date range in fully in bounds with given range
    """.trimIndent()

    override suspend fun perform(call: ApplicationCall) {
        val lo = call.request.queryParameters["lo"]
        val hi = call.request.queryParameters["hi"]

        if (lo == null || hi == null) {
            return call.respond(err(
                "expecting `lo` and `hi` params with format YYYY-MM-DD"
            ))
        }

        val loDate = LocalDate.parse(lo)
        val hiDate = LocalDate.parse(hi)
        val strict = try {
            call.request.queryParameters["strict"]?.toBoolean() ?: false
        } catch (_: Throwable) { false }
        call.respond(access.getAverageInRange(loDate, hiDate, strict).toString())
    }
}
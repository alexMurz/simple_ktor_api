package com.example.mods

import access.Access
import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import java.time.LocalDate

class ModMinMax(private val access: Access): Mod {
    override fun doc() = """
        Get minimum and maximum over given range, return as Json object { min=.., max=.. }
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
            return call.respond(
                Mod.err("expecting `lo` and `hi` params with format YYYY-MM-DD")
            )
        }

        val loDate = LocalDate.parse(lo)
        val hiDate = LocalDate.parse(hi)
        val strict = try {
            call.request.queryParameters["strict"]?.toBoolean() ?: false
        } catch (_: Throwable) { false }
        val result = access.getMinMaxInRange(loDate, hiDate, strict)?.let { (a, b) ->
            // a bit of hardcoded json
            "{\"min\"=\"$a\", \"max\"=\"$b\"}"
        }
        call.respond(result.toString())
    }
}
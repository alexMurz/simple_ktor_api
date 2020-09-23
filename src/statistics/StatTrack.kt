package com.example.statistics

import access.Access
import com.example.mods.Mod
import io.ktor.application.ApplicationCall
import io.ktor.response.respond
import java.time.LocalDate

class StatTrack(val access: Access): Mod, Access {
    override fun doc() = """
        Provide api usage statistics
        No arguments
    """.trimIndent()

    override suspend fun perform(call: ApplicationCall) {

        // Replace NaN with 0
        fun Float.escapeNan() = if (isNaN()) 0.0f else this
        fun Double.escapeNan() = if (isNaN()) 0.0 else this

        fun StringBuffer.logUsage(buff: ArrayList<Long>): StringBuffer {
            append(buff.size)
            append(" count. Taking ")
            append(String.format("%.3f", buff.sum()/1e6f))
            append(" ms total. ")
            append(String.format("%.3f", buff.average().escapeNan()/1e6f))
            append(" ms average.")
            return this
        }

        val buff = StringBuffer()
        buff.append("getAll     used ").logUsage(timing.all).append("\n")
        buff.append("getForDate used ").logUsage(timing.date).append("\n")
        buff.append("getAverage used ").logUsage(timing.avg).append("\n")
        buff.append("getMinMax  used ").logUsage(timing.minmax).append("\n")

        call.respond(buff.toString())
    }

    private val timing = object {
        val all = ArrayList<Long>()
        val date = ArrayList<Long>()
        val avg = ArrayList<Long>()
        val minmax = ArrayList<Long>()
    }

    private inline fun <T>measure(buff: ArrayList<Long>, f: () -> T): T {
        val start = System.nanoTime()
        val result = f()
        val time = System.nanoTime() - start
        buff.add(time)
        return result
    }


    override fun getAll() = measure(timing.all) { access.getAll() }

    override fun getForDate(date: LocalDate) = measure(timing.date) { access.getForDate(date) }

    override fun getAverageInRange(firstInclusive: LocalDate, lastExclusive: LocalDate, strict: Boolean) =
        measure(timing.avg) { access.getAverageInRange(firstInclusive, lastExclusive, strict) }

    override fun getMinMaxInRange(firstInclusive: LocalDate, lastExclusive: LocalDate, strict: Boolean)=
        measure(timing.minmax) { access.getMinMaxInRange(firstInclusive, lastExclusive, strict) }

}
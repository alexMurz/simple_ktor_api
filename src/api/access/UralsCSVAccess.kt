package access

import data.DataPoint
import data.DataPointRaw
import java.io.File
import java.time.LocalDate
import kotlin.math.max
import kotlin.math.min

class UralsCSVAccess(file: File): Access {
    companion object {
        // v inside of bounds of [a, b)
        private fun dateInside(a: LocalDate, b: LocalDate, v: LocalDate): Boolean {
            val lo = v.isEqual(a) || v.isAfter(a)
            val hi = v.isBefore(b)
            return lo && hi
        }

        /// Test if range B inside range A
        private fun dateRangeTest(a0: LocalDate, a1: LocalDate, b0: LocalDate, b1: LocalDate, strict: Boolean): Boolean {
            val c0 = dateInside(a0, a1, b0)
            val c1 = dateInside(a0, a1, b1)
            return if (strict) { c0 && c1 } else { c0 || c1 }
        }

        /// PreProcess CSV entry
        private fun String.process() = this
            .trim()
            .removeSurrounding("\"")

    }
    private val data = file.readLines()
        .drop(1) // Skip first line
        .map { str ->
            val c1 = str.indexOf(",")
            val c2 = str.indexOf(",", c1+1)

            val start = str.substring(0, c1).process()
            val end = str.substring(c1+1, c2).process()
            val value = str.substring(c2+1).process()

            DataPointRaw(start, end, value).take()
        }

    override fun getAll(): List<DataPoint> = data

    override fun getForDate(date: LocalDate): DataPoint? =
        data.find { dateInside(it.startDate, it.endDate, date) }

    override fun getAverageInRange(firstInclusive: LocalDate, lastExclusive: LocalDate, strict: Boolean) =
        data.filter { dateRangeTest(firstInclusive, lastExclusive, it.startDate, it.endDate, strict) }
            .map { it.value }
            .average().toFloat()

    override fun getMinMaxInRange(
        firstInclusive: LocalDate,
        lastExclusive: LocalDate,
        strict: Boolean
    )=
        data.filter { dateRangeTest(firstInclusive, lastExclusive, it.startDate, it.endDate, strict) }
            .let { list ->
                val first = list.firstOrNull() ?: return@let Pair(Float.NaN, Float.NaN)
                var min = first.value
                var max = first.value
                for (v in list) {
                    min = min(min, v.value)
                    max = max(max, v.value)
                }
                Pair(min, max)
            }

}
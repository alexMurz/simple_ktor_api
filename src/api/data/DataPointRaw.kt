package api.data

import java.time.LocalDate

data class DataPointRaw(
    val startDate: String,
    val endDate: String,
    val value: String
) {
    companion object {
        // Extrapolated, should check again
        val months = arrayOf(
            "янв", "фев",
            "мар", "апр", "май",
            "июн", "июл", "авг",
            "сен", "окт", "ноя",
            "дек"
        )
        fun parseDate(s: String): LocalDate {
            val parts = s.split(".")
            if (parts.size != 3) error("Expecting 3 part date \"DD.MMM.YY\", got $s")
            val d = parts[0].toInt()
            val m = run {
                val i = months.indexOf(parts[1])
                if (i == -1) error("Unknown month ${parts[1]}")
                i + 1
            }
            val y = parts[2].toInt()+2000
            return LocalDate.of(y, m, d)
        }
    }

    fun take(): DataPoint {
        val start = parseDate(startDate)
        val end = parseDate(endDate)
        val value = value.replace(',', '.').toFloat()
        return DataPoint(start, end, value)
    }
}
data class DataPoint(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val value: Float
)
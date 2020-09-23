package access

import data.DataPoint
import java.time.LocalDate

interface Access {

    /// Get all data points
    fun getAll(): List<DataPoint>

    /// Get `DataPoint` for Date
    fun getForDate(date: LocalDate): DataPoint?

    /// Get Average value in date range
    /// Can be null on error, or Nan if no data in range
    fun getAverageInRange(firstInclusive: LocalDate, lastExclusive: LocalDate, strict: Boolean = true): Float

    /// Get min and max in date range
    /// Can be null on error or Nan if no data in range
    fun getMinMaxInRange(firstInclusive: LocalDate, lastExclusive: LocalDate, strict: Boolean = true): Pair<Float, Float>
}

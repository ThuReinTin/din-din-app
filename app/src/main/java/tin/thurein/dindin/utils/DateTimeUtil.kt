package tin.thurein.dindin.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {
    private val SERVER_DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mmZ'Z'"

    val hh_mm_aa= "hh:mm aa"

    fun String.parseToDate(pattern: String = SERVER_DEFAULT_DATE_TIME_FORMAT) : Date? {
        val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
        return try {
            formatter.parse(this)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun Date.formatDate(pattern: String = SERVER_DEFAULT_DATE_TIME_FORMAT) : String? {
        val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
        return try {
            formatter.format(this)
        } catch (e: ParseException) {
            null
        }
    }
}
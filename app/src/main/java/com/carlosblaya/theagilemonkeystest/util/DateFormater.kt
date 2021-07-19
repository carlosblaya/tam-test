package com.carlosblaya.theagilemonkeystest.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormater {
    companion object {
        const val DATE_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val YEAR = "yyyy"
        fun formatDateFrom(stringDate: String?, formatFrom: String, formatTo: String): String {
            return try {
                val formatterTo = SimpleDateFormat(formatTo, Locale.getDefault())
                val formatterFrom = SimpleDateFormat(formatFrom, Locale.getDefault())
                formatterTo.format(formatterFrom.parse(stringDate))
            } catch (e: Exception) {
                ""
            }
        }
    }
}
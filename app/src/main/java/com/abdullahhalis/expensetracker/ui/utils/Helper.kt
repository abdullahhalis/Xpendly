package com.abdullahhalis.expensetracker.ui.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Double.toRupiah(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
    return "Rp ${formatter.format(this)}"
}

fun Long.toFormattedDate(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    val oneDay = 86_400_000L
    val twoDays = 2 * oneDay

    val dateFormat = SimpleDateFormat("dd MMM yyy", Locale.forLanguageTag("id-ID"))

    return when {
        diff < oneDay -> "Today"
        diff < twoDays -> "Yesterday"
        else -> dateFormat.format(Date(this))
    }
}

fun DateOption.toDateRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()
    val endDate = calendar.timeInMillis

    when (this) {
        DateOption.DAY -> {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
        DateOption.WEEK -> {
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
        DateOption.MONTH -> {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
        DateOption.YEAR -> {
            calendar.set(Calendar.DAY_OF_YEAR, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
        DateOption.All -> return Pair(0L, endDate)
    }
    return Pair(calendar.timeInMillis, endDate)
}
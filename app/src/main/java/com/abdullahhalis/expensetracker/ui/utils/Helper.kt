package com.abdullahhalis.expensetracker.ui.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
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
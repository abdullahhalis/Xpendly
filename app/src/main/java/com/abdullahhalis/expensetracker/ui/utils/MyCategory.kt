package com.abdullahhalis.expensetracker.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class MyCategory(
    val label: String,
    val icon: ImageVector,
    val color: Color
) {
    FOOD("Food", Icons.Rounded.Restaurant, Color(0xFFE07B39)),
    TRANSPORT("Transport", Icons.Rounded.DirectionsCar, Color(0xFF3D8BF8)),
    SHOPPING("Shopping", Icons.Rounded.ShoppingBag, Color(0xFF2ECC9A)),
    BILL("Bill", Icons.Rounded.Receipt, Color(0xFFE84C6A)),
    ENTERTAINMENT("Entertainment", Icons.Rounded.Movie, Color(0xFF9B59F5)),
    OTHER("Other", Icons.Rounded.MoreHoriz, Color(0xFFF5A623));

    companion object {
        fun getByLabel(label: String): MyCategory = when(label) {
            "Food" -> FOOD
            "Transport" -> TRANSPORT
            "Shopping" -> SHOPPING
            "Bill" -> BILL
            "Entertainment" -> ENTERTAINMENT
            else -> OTHER
        }
    }
}
package com.abdullahhalis.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abdullahhalis.expensetracker.ui.utils.DateOption

@Composable
fun DateDropDown(
    dateOption: DateOption,
    onOptionSelected: (DateOption) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    backGroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(backGroundColor)
                .clickable{ expanded = true}
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = dateOption.displayLabel,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "filter option",
                tint = textColor
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false},
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            DateOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.displayLabel,
                            color = if (option == dateOption)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
package com.shiva.myhealth.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shiva.myhealth.R

@Composable
fun ExpandableSectionTitle(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    title: String,
    icon: ImageVector,
    onAddClick: () -> Unit
) {

    val iconExpand = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown

    Row(modifier = modifier.padding(8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

        Icon(icon, contentDescription = "")
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Image(
            modifier = Modifier.size(32.dp),
            imageVector = iconExpand,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
            contentDescription = stringResource(id = R.string.app_name)
        )

        FilledIconButton(
            onClick = onAddClick,
            shape = CircleShape,
        )
        {
            Icon(Icons.Default.Add, "")
        }


    }
}

@Composable
fun ExpandableSection(
    modifier: Modifier = Modifier,
    title: Int,
    icon: ImageVector,
    onAddClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(
                    alpha = .50f
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
    ) {
        ExpandableSectionTitle(isExpanded = isExpanded, title = stringResource(title), icon = icon, onAddClick = onAddClick)

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = isExpanded
        ) {
            content()
        }
    }
}
package com.shiva.myhealth.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ActivityRings(
    comboProgress: Float,  // Progress for the red ring (0..1)
    caloriesProgress: Float,  // Progress for the green ring (0..1)
    sleepProgress: Float,  // Progress for the blue ring (0..1)
    componentSize: Int = 170,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Colors for each ring
        val colors = listOf(
            Color.Red,
            Color.Green,
            Color.Blue
        )
        val icons = listOf(
            Icons.Default.LocalFireDepartment,
            Icons.Default.LocalDining,
            Icons.Default.Bedtime,
        )
        Canvas(modifier = modifier.size(componentSize.dp)) {
            val size = size.minDimension
            val strokeWidth = size * 0.15f

            // Calculate radii for each ring
            val radii = listOf(
                size * 0.4f,
                size * 0.55f,
                size * 0.7f
            )


            // Progress for each ring
            val progresses = listOf(
                comboProgress,
                caloriesProgress,
                sleepProgress
            )

            // Draw the background rings
            for (i in radii.indices) {
                drawCircle(
                    color = colors[i].copy(alpha = 0.35f),
                    center = center,
                    radius = radii[i],
                    style = Stroke(width = strokeWidth,)
                )
            }

            // Draw the progress rings
            for (i in radii.indices) {
                drawArc(
                    brush = Brush.sweepGradient( // !!! that what
                        0f to Color.Transparent,
                        0.9f to colors[i],
                        0.91f to colors[i], // there was a problem with start of the gradient, maybe there way to solve it better
                        1f to colors[i]
                    ),
                    //color = colors[i],
                    startAngle = -90f,
                    sweepAngle = 360 * progresses[i],
                    useCenter = false,
                    topLeft = Offset(center.x - radii[i], center.y - radii[i]),
                    size = Size(radii[i] * 2, radii[i] * 2),
                    style = Stroke(width = strokeWidth,)
                )
            }
        }
        /*Column(
            modifier.size((componentSize * 0.65f).dp).background(
                androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer.copy(
                    0.5f
                ), shape = CircleShape
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOf(comboProgress, caloriesProgress, sleepProgress).forEachIndexed { i, prm ->
                Row {
                    Icon(icons[i], "", tint = colors[i])
                    Text((prm * 100).toInt().toString() + " %", color = colors[i])
                }
            }


        }*/

    }
}

@Preview(showBackground = true)
@Composable
fun ActivityRingsPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ActivityRings(
                comboProgress = 0.75f,
                caloriesProgress = 0.5f,
                sleepProgress = 0.9f,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
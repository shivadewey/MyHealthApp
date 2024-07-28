package com.shiva.myhealth.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shiva.myhealth.R
import com.shiva.myhealth.data.Product

@Composable
fun FoodSectionContent(
    modifier: Modifier = Modifier, products: SnapshotStateList<Product>, goalCalories: Int
) {

    val currentCalories = products.fold(0f) { a, b -> a + b.caloriesSummery }
    Row(
modifier.fillMaxWidth().padding(8.dp).height(60.dp),
horizontalArrangement = Arrangement.SpaceAround,
verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = " ${stringResource(R.string.products)}: ${products.size}",
        minLines = 2,
        modifier = modifier.padding(top = 20.dp),
        style = MaterialTheme.typography.bodyLarge,
    )

    VerticalDivider(thickness = 6.dp, color = Color.Transparent)
    //ProgressTextIndicator((eating.calories / maxCalories).toFloat(), {})
    Box(modifier, Alignment.Center) {
        val str: String
        val color: Color
        if (currentCalories > goalCalories) {
            str =
                "${stringResource(R.string.calories)} на ${currentCalories - goalCalories} больше "
            color = Color.Red.copy(0.75f)
        } else {
            str = "${stringResource(R.string.calories)}: $currentCalories"
            color = Color.Green.copy(0.75f)
        }
        LinearProgressIndicator(
            progress = currentCalories / goalCalories,
            color = color,
            strokeCap = StrokeCap.Round,
            modifier = modifier.padding(2.dp).height(30.dp).border(1.dp,Color.Black, RoundedCornerShape(16.dp))
        )

        Text(
            str,
            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
            minLines = 2,
            modifier = modifier.padding(4.dp).padding(top = 15.dp).fillMaxWidth()
                .align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
    }
}

@Composable
@Preview(showBackground = true)
fun FoodSectionContentPreview() {
    //FoodSectionContent(Modifier, Day(LocalDate.now()).breakfast.products, 10000)
}
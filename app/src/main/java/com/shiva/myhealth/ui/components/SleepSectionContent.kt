package com.shiva.myhealth.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.shiva.myhealth.data.Sleep

@Composable
fun SleepSectionContent(
    modifier: Modifier = Modifier, sleeps: SnapshotStateList<Sleep>, goalSleep: Float
) {
    //val currentSleep by remember { mutableFloatStateOf(sleeps.totalSleep) }
    val currentSleep = sleeps.fold(0f) { a, b -> a + b.hours }
    Row(
        modifier.fillMaxWidth().padding(8.dp).height(60.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = " ${stringResource(R.string.sleep_count)}: ${sleeps.size}",
            minLines = 2,
            modifier = Modifier.padding(top = 20.dp),
            style = MaterialTheme.typography.bodyLarge,
        )

        VerticalDivider(thickness = 6.dp, color = Color.Transparent)

        Box(modifier, Alignment.CenterEnd) {
            val str: String
            val color: Color
            if (currentSleep < goalSleep) {
                str =
                    "${stringResource(R.string.lack_of_sleep)}(часов): ${goalSleep - currentSleep} "
                color = Color.Blue.copy(0.75f)
            }
            else if(currentSleep > goalSleep){
                str =
                    "${stringResource(R.string.sleep_more)} (часов): ${currentSleep - goalSleep} "
                color = Color.Red.copy(0.75f)
            }
                else {
                str = "${stringResource(R.string.sleep_time)}: $currentSleep"
                color = Color.Blue.copy(0.75f)
            }
            LinearProgressIndicator(
                progress = currentSleep / goalSleep,
                color = color,
                strokeCap = StrokeCap.Round,
                modifier = modifier.padding(2.dp).height(30.dp).border(1.dp,Color.Black, RoundedCornerShape(16.dp))
            )

            Text(
                str,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                minLines = 2,
                modifier = modifier.padding(4.dp).padding(top = 15.dp).fillMaxHeight().align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun SleepSectionContentPreview() {
    //SleepSectionContent(Modifier, Day(LocalDate.now()), 8f)
}
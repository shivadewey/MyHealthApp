package com.shiva.myhealth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shiva.myhealth.data.Day
import java.util.stream.IntStream.range

@Composable
fun CalendarItem(
    onItemClick: () -> Unit,
    isSelected: Boolean ,
    day: Day,
    modifier: Modifier,){

    val sel: Color = if (isSelected){
        Color.Green
    }
    else Color.DarkGray

    Column(modifier.height(75.dp)
        .width(50.dp)
        .border(width = 2.dp, color = sel, shape = RoundedCornerShape(16.dp))
        .padding(3.dp)
        .clickable(onClick = { onItemClick()}),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(day.date.dayOfMonth.toString(), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(day.dayOfWeekToString()), style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)

        Row (modifier= modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            var j = day.foodCount
            for (i in range(0,3)){
                if (j>0)
                {
                    Box(modifier = modifier.size(7.dp)
                        .background(color = Color.Green, shape = CircleShape)
                        .padding(2.dp))
                    j--
                }
                else{
                    Box(modifier = modifier.size(7.dp)
                        .background(color = Color.DarkGray, shape = CircleShape)
                        .padding(2.dp))
                }
        }
           val sleep = if (day.bedTime.isNotEmpty()) Color.Blue else Color.DarkGray

                Box(modifier = modifier.size(7.dp)
                .background(color = sleep, shape = CircleShape)
                .padding(2.dp))
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewCalendarItem(){
    //CalendarItem( {},false, isSleep = true, modifier = Modifier)
}
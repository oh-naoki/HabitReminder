package component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WeeklyCalendar(
    modifier: Modifier = Modifier,
) {
    val daysOfWeek = listOf("月", "火", "水", "木", "金", "土", "日")
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        for (day in daysOfWeek) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = day,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
                if (day == "土") {
                    WeekDay(label = "6", isComplete = true)
                } else {
                    WeekDay(label = "6")
                }
            }
        }
    }
}

@Composable
private fun WeekDay(
    label: String,
    isComplete: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .then(
                if (isComplete) {
                    Modifier.background(Color.Blue, shape = CircleShape)
                } else Modifier
            )
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}


@Preview
@Composable
fun WeeklyCalendarPreview() {
    WeeklyCalendar()
}

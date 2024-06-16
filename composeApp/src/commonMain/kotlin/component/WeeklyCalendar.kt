package component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WeeklyCalendar(
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = 0)

    val daysOfWeek = listOf("月", "火", "水", "木", "金", "土", "日")
    val allWeeks = listOf(
        List(7) { "4" },
        List(7) { "5" },
        List(7) { "6" },
    )

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            scrollState.scrollToItem(0)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            state = scrollState,
        ) {
            items(allWeeks) { week ->
                OneWeek(
                    modifier = Modifier.width(this@BoxWithConstraints.maxWidth),
                    days = week
                )
            }
        }
    }
}

@Composable
private fun OneWeek(
    days: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        for (day in days) {
            WeekDay(
                dayOfWeek = DayOfWeek.MONDAY,
                label = day,
            )
        }
    }
}

@Composable
private fun WeekDay(
    dayOfWeek: DayOfWeek,
    label: String,
    isComplete: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically
        ),
    ) {
        Text(
            text = "月",
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp
            ),
        )
        Box(
            modifier = modifier
                .size(24.dp)
                .then(
                    if (isComplete) {
                        Modifier.background(HabitColor.Blue, shape = CircleShape)
                    } else Modifier
                )
        ) {
            Text(
                text = label,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ),
            )
        }
    }
}


@Preview
@Composable
fun WeeklyCalendarPreview() {
    WeeklyCalendar()
}

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import component.HabitReminderTheme
import component.HabitReminderTopAppBar
import component.WeeklyCalendar
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HabitReminderTheme {
        Scaffold(
            backgroundColor = Color.DarkGray,
            topBar = {
                HabitReminderTopAppBar(
                    title = "Habit Reminder",
                    action = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            tint = Color.White,
                            contentDescription = "Add",
                            modifier = Modifier.clickable { /* TODO: Add click */ }
                                .size(24.dp)
                        )
                    },
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                WeeklyCalendar()
            }
        }
    }
}

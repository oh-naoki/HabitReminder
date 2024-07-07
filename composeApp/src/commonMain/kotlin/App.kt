import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import component.HabitReminderTheme
import component.HabitReminderTopAppBar
import component.RemindItem
import component.WeeklyCalendar
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController =
        remember(factory) { factory.createPermissionsController() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val notificationManager = getNotificationManager()

    BindEffect(controller)
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            runCatching {
                controller.providePermission(Permission.REMOTE_NOTIFICATION)
                // TODO: リマインダーアイテム生成時に設定する
                notificationManager.scheduleNotification(
                    weekDay = 1,
                    hour = 8,
                    minute = 0
                )
            }.onFailure {
                controller.openAppSettings()
            }
        }
    }

    HabitReminderTheme {
        Scaffold(
            backgroundColor = Color.Black,
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
                modifier = Modifier
                    .padding(paddingValues)
                    .background(
                        color = Color(0xFF0A0A0A),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                WeeklyCalendar(
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(10) {
                        RemindItem(
                            isComplete = it % 2 == 0,
                            icon = Icons.Default.Email,
                            title = "Title $it"
                        )
                    }
                }
            }
        }
    }
}

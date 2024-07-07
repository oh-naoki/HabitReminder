import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.HabitReminderTheme
import ui.home.HomeScreen

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
        HomeScreen()
    }
}

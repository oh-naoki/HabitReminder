import androidx.compose.ui.window.ComposeUIViewController
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

fun MainViewController() = ComposeUIViewController {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Ios(
            showPushNotification = true
        )
    )
    App()
}

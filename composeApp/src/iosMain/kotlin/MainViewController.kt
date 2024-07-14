import androidx.compose.ui.window.ComposeUIViewController
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import di.KoinKMPStarter

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinKMPStarter().init()
    }
) {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Ios(
            showPushNotification = true
        )
    )
    App()
}

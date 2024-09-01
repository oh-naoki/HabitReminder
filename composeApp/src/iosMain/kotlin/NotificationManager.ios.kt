// shared/src/iosMain/kotlin/NotificationManagerImpl.kt
import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNCalendarNotificationTrigger.Companion.triggerWithDateMatchingComponents
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationActionOptionNone
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter

class IOSLocalNotificationManager : LocalNotificationManager {
    override fun requestAuthorization() {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorizationWithOptions(
            UNNotificationActionOptionNone
        ) { granted, error ->
            if (granted) {
                println("Authorization granted")
            } else {
                println("Authorization failed: ${error?.localizedDescription}")
            }
        }
    }

    override fun scheduleNotification(
        weekDay: Int,
        hour: Int,
        minute: Int,
        title: String,
        body: String
    ) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(body)
            setSound(UNNotificationSound.defaultSound)
        }

        val dateComponents = NSDateComponents().apply {
            this.weekday = weekDay.toLong()
            this.hour = hour.toLong()
            this.minute = minute.toLong()
        }

        val trigger = triggerWithDateMatchingComponents(dateComponents, repeats = true)
        val request =
            UNNotificationRequest.requestWithIdentifier("morningNotification", content, trigger)

        center.addNotificationRequest(request) { error ->
            if (error != null) {
                println("Error scheduling notification: ${error.localizedDescription}")
            }
        }
    }
}

actual fun getNotificationManager(): LocalNotificationManager = IOSLocalNotificationManager()

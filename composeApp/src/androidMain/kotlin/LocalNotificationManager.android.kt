import net.ohnaoki.habitreminder.AndroidLocalNotificationManager
import net.ohnaoki.habitreminder.AppContext

actual fun getNotificationManager(): LocalNotificationManager {
    return AndroidLocalNotificationManager(context = AppContext.get())
}

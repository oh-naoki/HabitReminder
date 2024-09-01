interface LocalNotificationManager {
    fun requestAuthorization()
    fun scheduleNotification(
        weekDay: Int,
        hour: Int,
        minute: Int,
        title: String,
        body: String
    )
}

expect fun getNotificationManager(): LocalNotificationManager

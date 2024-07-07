interface NotificationManager {
    fun requestAuthorization()
    fun scheduleNotification(weekDay: Int, hour: Int, minute: Int)
}

expect fun getNotificationManager(): NotificationManager

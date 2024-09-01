package net.ohnaoki.habitreminder

import LocalNotificationManager
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar
import java.util.TimeZone

class AndroidLocalNotificationManager(private val context: Context) : LocalNotificationManager {
    override fun requestAuthorization() {
        // Androidでは特別な権限リクエストは必要ないため、何もしない
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun scheduleNotification(
        weekDay: Int,
        hour: Int,
        minute: Int,
        title: String,
        body: String
    ) {

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "dailyNotificationChannel",
                "Daily Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, MyNotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("body", body)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendarDayOfWeek = (weekDay + 1) % 7 + 1
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).apply {
            set(Calendar.DAY_OF_WEEK, calendarDayOfWeek)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // 過去の時間を設定した場合は次の週に設定する
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            println("set next week")
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }
}

class MyNotificationReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: ""
        val body = intent.getStringExtra("body") ?: ""
        showNotification(
            context,
            title,
            body
        )
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(
        context: Context,
        title: String,
        body: String
    ) {
        val builder = NotificationCompat.Builder(context, "dailyNotificationChannel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("${title}の時間です")
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }
}

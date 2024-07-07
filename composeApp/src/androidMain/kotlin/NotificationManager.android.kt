import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import net.ohnaoki.habitreminder.AppContext
import net.ohnaoki.habitreminder.R
import java.util.Calendar

class AndroidNotificationManager(private val context: Context) : NotificationManager {
    override fun requestAuthorization() {
        // Androidでは特別な権限リクエストは必要ないため、何もしない
    }

    override fun scheduleNotification(weekDay: Int, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "WEEKLY_NOTIFICATION"
        }
        val pendingIntent =
            PendingIntent.getBroadcast(context, weekDay, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, weekDay)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // 過去の時間を設定した場合は次の週に設定する
        if (calendar.timeInMillis < System.currentTimeMillis()) {
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

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context, "dailyNotificationChannel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("おはようございます")
            .setContentText("今日は何を予定していますか？")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }
}

actual fun getNotificationManager(): NotificationManager =
    AndroidNotificationManager(context = AppContext.get())

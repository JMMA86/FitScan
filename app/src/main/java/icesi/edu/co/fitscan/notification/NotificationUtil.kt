package icesi.edu.co.fitscan.notification

import android.content.Context
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.work.*
import icesi.edu.co.fitscan.R
import java.util.concurrent.TimeUnit

object NotificationUtil {

    private const val CHANNEL_ID = "messages"
    private const val CHANNEL_NAME = "Messages"
    private var id = 0
    private const val INACTIVITY_WORK_NAME = "inactivity_notification"

    fun showNotification(context: Context, title: String, message: String) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        manager.notify(
            id++,
            NotificationCompat
                .Builder(context, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()
        )
    }

    // Programar notificación de inactividad
    fun scheduleInactivityNotification(context: Context, delayHours: Long = 7) {
        val workRequest = OneTimeWorkRequestBuilder<InactivityNotificationWorker>()
            .setInitialDelay(delayHours, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                INACTIVITY_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    // Cancelar notificación de inactividad (cuando el usuario abre la app)
    fun cancelInactivityNotification(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(INACTIVITY_WORK_NAME)
    }

}
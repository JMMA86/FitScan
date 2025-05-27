package icesi.edu.co.fitscan.notification

import android.content.Context
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import icesi.edu.co.fitscan.R

object NotificationUtil {

    private val CHANNEL_ID = "messages";
    private val CHANNEL_NAME = "Messages";
    private var id = 0;

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

}
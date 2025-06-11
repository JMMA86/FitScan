package icesi.edu.co.fitscan.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class InactivityNotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    override fun doWork(): Result {
        NotificationUtil.showNotification(
            applicationContext,
            "¿Y los gains? ☠️",
            "😴 ¡Te extrañamos! Vuelve a entrenar y retoma tu camino hacia tus metas 💪📈"
        )
        
        // Programar la siguiente notificación automáticamente
        NotificationUtil.scheduleInactivityNotification(applicationContext)
        
        return Result.success()
    }
}

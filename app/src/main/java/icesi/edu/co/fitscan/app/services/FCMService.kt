package icesi.edu.co.fitscan.app.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import icesi.edu.co.fitscan.notification.NotificationUtil

class FCMService : FirebaseMessagingService() {
    
    override fun onMessageReceived(message: RemoteMessage) {
        val dataMap = message.data
        val titulo = dataMap["titulo"] ?: "FitScan"
        val mensaje = dataMap["mensaje"] ?: "Tienes una nueva notificación"
        NotificationUtil.showNotification(this, titulo, mensaje)
    }
}
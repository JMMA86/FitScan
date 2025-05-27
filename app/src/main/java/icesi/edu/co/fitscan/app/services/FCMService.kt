package icesi.edu.co.fitscan.app.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import icesi.edu.co.fitscan.notification.NotificationUtil
import org.json.JSONObject

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val obj = JSONObject(message.data as Map<*, *>)
        val json = obj.toString()
        NotificationUtil.showNotification(this, "¡Hola!", obj.optString("message"))
    }
}
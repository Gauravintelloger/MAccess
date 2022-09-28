package technology.dubaileading.maccessemployee.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.ui.HomeActivity

class FirebaseNotification : FirebaseMessagingService() {
    var notificationManager: NotificationManager? = null

    override fun onMessageReceived(message: RemoteMessage) {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        super.onMessageReceived(message)
        notifyUser(message.notification?.title.toString(),
            message.notification?.body.toString()
        )
    }


    private fun notifyUser(title: String, messageBody: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // For android Oreo and above  notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fcm notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


    private fun sendNotification(remoteMessage: RemoteMessage) {
        var intent = Intent(this, HomeActivity::class.java)
        val type = remoteMessage.data["type"]
        val message = remoteMessage.data["message"]
        val title = remoteMessage.data["title"]
        val order_id = remoteMessage.data["order_id"]
        val user_id = remoteMessage.data["user_id"]
        val image = remoteMessage.data["image"]

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )
                .setSound(defaultSoundUri)
                .setColor(resources.getColor(R.color.blue))
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Bradma",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(
            System.currentTimeMillis().toInt() / 1000 /* ID of notification */,
            notificationBuilder.build()
        )
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
package com.depromeet.baton

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.depromeet.baton.presentation.sample.SampleActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.d("new Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.d("From: " + remoteMessage.data)

        val title = remoteMessage.notification?.title
        val message = remoteMessage.notification?.body

        if (!message.isNullOrEmpty()) {
            Timber.d("notice 타이틀: $title")
            Timber.d("notice 바디: $message")
            sendNotiNotification(remoteMessage)
        } else {
            Timber.e("notice 수신에러: data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
    }

    private fun sendNotiNotification(remoteMessage: RemoteMessage) {
        val uniId = remoteMessage.sentTime.toInt()
        val intent = Intent(this, SampleActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, uniId, intent, PendingIntent.FLAG_MUTABLE)

        val channelId = "노티피케이션 메시지"

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(remoteMessage.notification?.body.toString())
                .setContentText(remoteMessage.notification?.title.toString())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(uniId, notificationBuilder.build())
    }
}
package com.depromeet.baton

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.ask.view.MsgRcvActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber


class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.e("new Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Timber.e("From: " + remoteMessage.data)

        if (remoteMessage.data.isNotEmpty() ) {
            sendNotification(remoteMessage)
        } else {
            Timber.d("pushAlarm", "메시지를 수신하지 못했습니다.")
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val uniId = remoteMessage.sentTime.toInt()

        //todo 문의하기 Activity로 변경
        val intent = Intent(this, MsgRcvActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        for (key in remoteMessage.data.keys) {
            intent.putExtra(key, remoteMessage.data.getValue(key))
        }

        val pendingIntent = PendingIntent.getActivity(
            this, uniId, intent, PendingIntent.FLAG_MUTABLE
        )

        val channelId = "노티피케이션 메시지"

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
          //      .setSmallIcon(R.mipmap.ic_cardna_logo)
          //      .setLargeIcon((BitmapFactory.decodeResource(resources, R.drawable.img_logo)))
                .setContentTitle("바통")
                .setContentText(remoteMessage.data["body"].toString())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(null) //소리
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //잠금

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId, "Notice",
                    NotificationManager.IMPORTANCE_DEFAULT,
                )
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(uniId, notificationBuilder.build())
    }
}
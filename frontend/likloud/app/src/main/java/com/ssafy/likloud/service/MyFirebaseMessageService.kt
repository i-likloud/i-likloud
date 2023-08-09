package com.ssafy.likloud.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.ui.drawing.DrawingDetailFragmentArgs
import java.util.*

private const val TAG = "MyFirebaseMessageServic_싸피"

class MyFirebaseMessageService : FirebaseMessagingService() {

    // 새로운 토큰이 생성될 때 마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        // 새로운 토큰 수신 시 서버로 전송
        MainActivity.uploadToken(token)
    }

    // Foreground에서 Push Service를 받기 위해 Notification 설정
    // 메시지가 오면 호출되는 함수
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: ${remoteMessage.data}")
        var message: String? = null

//        remoteMessage.notification?.apply {
//            Log.d(TAG, "onMessageReceived: ssss")
        val navBuilder = NavDeepLinkBuilder(this@MyFirebaseMessageService)
//            val messageTitle = title 이런 식으로 title body 받을 수 있음
        val pendingIntent: PendingIntent
        val nickname = remoteMessage.data["sendNickname"]
        when (remoteMessage.data["historyType"]) {
            "GIFT" -> {
                message = "$nickname ${getString(R.string.body_gift)}"
                pendingIntent = navBuilder
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.drawingListFragment)
                    .createPendingIntent()
            }
            // LIKE 혹은 COMMENT 인 경우
            "LIKE" -> {
                message = "$nickname ${getString(R.string.body_like)}"
                pendingIntent =
                    createDrawingDetailPendingIntent(remoteMessage.data["drawingId"]!!.toInt())
            }

            else -> {
                message = "$nickname ${getString(R.string.body_comment)}"
                pendingIntent =
                    createDrawingDetailPendingIntent(remoteMessage.data["drawingId"]!!.toInt())
            }
        }

        // 알림 생성하고 nav args에 집어넣고 아이콘 지정
        val builder =
            NotificationCompat.Builder(this@MyFirebaseMessageService, MainActivity.channel_id)
                .setSmallIcon(R.drawable.logo_main)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setFullScreenIntent(pendingIntent, true)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(101, builder.build())
    }


    private fun createDrawingDetailPendingIntent(drawingId: Int): PendingIntent {
        val args = DrawingDetailFragmentArgs(drawingId)

        return NavDeepLinkBuilder(this@MyFirebaseMessageService)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.drawingDetailFragment)
            .setArguments(args.toBundle())
            .createPendingIntent()
    }
}


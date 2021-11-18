package com.example.a11_alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver() : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {

        createNotificationChannel(p0)
        notifyNotification(p0)
    }

    private fun createNotificationChannel(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//O가 26버전
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "기상알림",
                NotificationManager.IMPORTANCE_HIGH //중요도.
            )

            val a = NotificationManagerCompat.from(context) //노티피케이션 매니져 가져오기
            a.createNotificationChannel(notificationChannel) //가져와서 notificationchannel만들기
        }
    }

    private fun notifyNotification(context: Context){
        with(NotificationManagerCompat.from(context)){
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("알람")
                .setContentText("일어날 시간입니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notify(NOTIFICATION_ID,build.build())
        }

    }

    companion object{
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID  = "1000"
    }


}
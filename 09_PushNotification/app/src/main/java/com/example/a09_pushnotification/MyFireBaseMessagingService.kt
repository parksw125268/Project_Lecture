package com.example.a09_pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseMessagingService :FirebaseMessagingService() {

    override fun onNewToken(p0: String) {//토큰이 바뀔수 있기 때문에 이를 서버에 갱신해주는 메소드(필수)
        super.onNewToken(p0)
    }


    override fun onMessageReceived(message: RemoteMessage) { //message를 수신하면 발생됨. 여기서 메세지를 처리.
        super.onMessageReceived(message)

        createNotificationChannel()

        val title  = message.data["title"] //firebase 에서 날린 메세지 title, message
        val message = message.data["message"]
        //알림 컨텐츠 만들기
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)//안드로이드 8 oreo 버전 이하를 위해.

        //만든 컨텐츠를 실제로  Notify 하기
        NotificationManagerCompat.from(this)
                .notify(1,notificationBuilder.build())//notification id, Notification자체
    }

    //채널이라는것을 설정해야됨 노티피케이션이 실행되기 전 한번만 설정되면됨. 앱 실행 시에 하는것이 좋음. 8.0 버전부터 생김. 7.0에는 넣으면 안됨
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//oreo 버젼 이상일 경우 채널을 만듬.
            val channel = NotificationChannel(
                    CHANNEL_ID,  //id
                    CHANNEL_NAME ,//name
                    NotificationManager.IMPORTANCE_DEFAULT //중요도
            )
            channel.description = CHANNEL_DESCRIPTION

            //매니저에 추가
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(channel)

        }
    }
    companion object{
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }
}
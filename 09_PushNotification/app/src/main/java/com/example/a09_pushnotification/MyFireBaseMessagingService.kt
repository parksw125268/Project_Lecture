package com.example.a09_pushnotification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseMessagingService :FirebaseMessagingService() {

    override fun onNewToken(p0: String) {//토큰이 바뀔수 있기 때문에 이를 서버에 갱신해주는 메소드(필수)
        super.onNewToken(p0)
    }

    override fun onMessageReceived(message: RemoteMessage) { //message를 수신하면 발생됨. 여기서 메세지를 처리.
        super.onMessageReceived(message)
    }
}
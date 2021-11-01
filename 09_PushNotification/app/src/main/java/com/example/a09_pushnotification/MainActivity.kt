package com.example.a09_pushnotification

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val resultTextView : TextView by lazy {
        findViewById(R.id.resultTextView)
    }
    private val firebaseToken : TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        updateResult()
    }
    private fun initFirebase(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if(task.isSuccessful){    //성공적으로 가져오면 토큰을 가져온다.
                    firebaseToken.text = task.result
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {//앱이 종료된 상태가 아니라서 OnCreate가 아니라 OnNew 된다.
        super.onNewIntent(intent)
        setIntent(intent)
        updateResult(true)
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent : Boolean = false){
        //isNewIntent :  false 앱이 종료된상태에서 알림을 눌러 들어왔느냐, true 앱이 실행되어있는상태에서 눌러 갱신했느냐
        resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런쳐") +
        if(isNewIntent){
            "(으)로 갱신했습니다."
        }else{
            "(으)로 실행했습니다."
        }

    }



}
package com.example.a03_secretdiary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener


class DiaryActivity: AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper()) // MainLooper를 넣어주면 메인스레드에 연결된 스레드

    private val privateText: EditText by lazy {
        findViewById(R.id.privateText)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)
        val detailPreference = getSharedPreferences("detail", MODE_PRIVATE)
        privateText.setText(detailPreference.getString("detail", ""))

        //쓰레드 기능 이용 //아래는 변경할 때마다 저장함. 그게아니라 뭔가 하다가 멈칫 했을때만 저장 하도록. 병경

        val runnable  = Runnable {
             getSharedPreferences("detail", MODE_PRIVATE).edit {
                 putString("detail", privateText.text.toString())
             }
        }


        privateText.addTextChangedListener {
            handler.removeCallbacks(runnable)//전에 실행 안되서 남은 쓰레기값들
            handler.postDelayed(runnable, 500)
        }
    }
}
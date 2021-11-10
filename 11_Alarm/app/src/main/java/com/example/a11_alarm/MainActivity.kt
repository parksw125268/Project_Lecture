package com.example.a11_alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //step0 뷰를 초기화 해주기
        initOnOffButton()
        initChangeAlarmTimeButton()
        //step1 데이터 가져오기
        //step2 가져온 데이터를 view에 그려주기
        //step3
    }
    private fun initOnOffButton(){
        val onOffButton = findViewById<Button>(R.id.onOffButton)
        onOffButton.setOnClickListener {
            // 데이터를 확인한다.

            // 온 오프에 따라 작업을 처리한다.
            // 온 -> 알람 등록
            // 오프 -> 알람제거
        }

    }

    private fun initChangeAlarmTimeButton(){

    }
}
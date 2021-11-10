package com.example.a11_alarm

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.*

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
        val changeAlarmButton = findViewById<Button>(R.id.changeAlarmTimeButton)
        changeAlarmButton.setOnClickListener {
            //현재시가 가져오기
            val calender = Calendar.getInstance()//시스템에 적용된 현재 시간을 가져온다

            //Time다이얼로그 띄워서 시간 지정후 종료할때 작업해주기
            TimePickerDialog(this,
                { picker, hour, minute -> //타임을 지정하고 나올때 발생하는 이벤트 람다.
                    //1.데이터를 저장하고 2.View를 업데이트하고 3.기존에 있던 알람을 삭제하고
                    val model = saveAlarmModel(hour, minute, false)
                },
                calender.get(Calendar.HOUR_OF_DAY) ,
                calender.get(Calendar.MINUTE),
                false
            ).show()
        }
    }
    private fun saveAlarmModel(
        hour:Int,
        minute:Int,
        onOff : Boolean
    ):AlarmDisplayModel{
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = false
        )
        return model
    }
}
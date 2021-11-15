package com.example.a11_alarm

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.edit
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initOnOffButton()
        initChangeAlarmTimeButton()

        val model = fetchDataFromSharedPreference() //데이터 가져오기
      //  renderView(model) //가져온 데이터 view에 그려주기
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
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        with(sharedPreference.edit()){
            putString(ALARM_KEY, model.makeDataForDB()) //데이터 저장
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }
    private fun fetchDataFromSharedPreference():AlarmDisplayModel{
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val tiemDBValue = sharedPreference.getString(ALARM_KEY,"9:30") ?: "9:30" //데이터 가져오기(없거나 null이면 "9:30")
        val onOffDBValue = sharedPreference.getBoolean(ONOFF_KEY,false)
        val alramDAta = tiemDBValue.split(":")

        return AlarmDisplayModel(1,2,false)
    }
    companion object{
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
    }
}
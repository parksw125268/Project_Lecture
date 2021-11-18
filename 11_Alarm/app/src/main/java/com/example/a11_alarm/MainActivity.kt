package com.example.a11_alarm

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initOnOffButton()
        initChangeAlarmTimeButton()

        val model = fetchDataFromSharedPreference() //데이터 가져오기
        renderView(model) //가져온 데이터 view에 그려주기
    }
    private fun initOnOffButton(){
        val onOffButton = findViewById<Button>(R.id.onOffButton)
        onOffButton.setOnClickListener {
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener //tag는 object인 상태 이것을 형변환 해줘야함.
            val newModel =  saveAlarmModel(                                       // as?는 형변환 실패하면 null로 떨어지게함.
                model.hour,
                model.minute,
                model.onOff.not()//반전
            )
            renderView(newModel)

            //데이터 확인을 한다.
            if (newModel.onOff){
                //알람이 켜진경우 -> 알람을 등록

            }else{
                //알람이 꺼진경우 -> 알람을 제거
                    // 아래도 같은 코드가 이있음 함수로 빼도 좋다.
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    ALARM_REQUEST_CODE,
                    Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
                pendingIntent?.cancel() // 알람이 없다면 null이기 때문에 널 방어

            }


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
                    //1.데이터를 저장하고
                    val model = saveAlarmModel(hour, minute, false)
                    //2.View를 업데이트하고
                    renderView(model)

                    //3.기존에 있던 알람을 삭제하고
                    val pendingIntent = PendingIntent.getBroadcast(
                        this,
                        ALARM_REQUEST_CODE,                                    //있으면 가져오고 없으면 안가져온다.
                        Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
                    pendingIntent?.cancel() // 알람이 없다면 null이기 때문에 널 방어
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
            onOff = onOff
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
        val alramData = tiemDBValue.split(":")

        val  alarmModel = AlarmDisplayModel(
            hour = alramData[0].toInt(),
            minute =  alramData[1].toInt(),
            onOff = onOffDBValue  )

        //데이터 보정.
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,         //만들거임.                 //있으면 가져오고 없으면 안가져온다.
            Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)

        if ((pendingIntent == null ) and alarmModel.onOff ){
            //알람이 등록되어있지 않은데 알람은 model에는 켜져있다고 되어있는 경우 '
            alarmModel.onOff = false
        }else if((pendingIntent != null) and  alarmModel.onOff.not()){
            //알람은 등록되어있는데 알람데이터는 꺼져있음.-> 알람을 취소함.
            pendingIntent.cancel()
        }
        return alarmModel
    }
    private fun renderView(model: AlarmDisplayModel){
        findViewById<TextView>(R.id.ampmTextView).apply{
            text = model.ampmText
        }
        findViewById<TextView>(R.id.timeTextView).apply{
            text = model.timeText
        }
        findViewById<Button>(R.id.onOffButton).apply{
            text = model.onOffText
            tag = model //테그에는 아무거나 넣어놓을 수 있음.
        }
    }

    companion object{
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val ALARM_REQUEST_CODE = 1000;
    }
}
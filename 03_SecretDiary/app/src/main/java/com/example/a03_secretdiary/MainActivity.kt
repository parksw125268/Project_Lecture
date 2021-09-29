package com.example.a03_secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private val numPicker1 : NumberPicker by lazy {//메인액티비티가 생성될 시점에는 아직 뷰가 그려지지 않음
        findViewById<NumberPicker>(R.id.numPicker1)//뷰가 다 그려졌다고 알려주는 시점이 onCreate임. onCreate가 된 이후에 View에 접근해야함.
            .apply {
                this.minValue=0
                this.maxValue=9
            }
    }
    private val  numPicker2 : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numPicker2)
            .apply {
                this.minValue = 0
                this.maxValue = 9
            }
    }
    private val numPicker3 : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numPicker3)
            .apply {
                this.minValue = 0
                this.maxValue = 9
            }
    }
    private val openButton : AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }
    private val changePasswordButton : AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePWButton)
    }
    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numPicker1 //numberPicker를 lazy하게 선언해줘서 사용될때 apply되는데 numberPikcer를 직접 사용하지는 않아서 여기서 한번 써줘서 apply 하도록
        numPicker2
        numPicker3
        openButton.setOnClickListener {                    //파일이름 ,  mode
            if (changePasswordMode){
                Toast.makeText(this, "비밀번호 변경 작업중입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val pwPreference = getSharedPreferences("password", Context.MODE_PRIVATE)//preference파일을 다른 앱과 share해서 사용하도록 해주는 함수. //MODEPRIVATE으로 이 앱에서만 사용하겟따.
            val passwordFromUser = "${numPicker1.value}${numPicker2.value}${numPicker3.value}" //입력하려는 pw
            if(pwPreference.getString("pw","000").equals(passwordFromUser)) {//password파일안에 pw라는 키로 초기값 "000"으로  저장.
                startActivity(Intent(this,DiaryActivity::class.java))
            } else {//실패
                showErrorPopup()
            }
        }
        changePasswordButton.setOnClickListener {
            val pwPreference = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numPicker1.value}${numPicker2.value}${numPicker3.value}"
            if (changePasswordMode){//번호를 저장하는 기능
                pwPreference.edit(commit = true){
                    putString("pw",passwordFromUser)
                }
                changePasswordButton.setBackgroundColor(Color.BLACK)
                changePasswordMode = false
                Toast.makeText(this,"비밀번호 변경되었습니다.", Toast.LENGTH_SHORT).show()
            }else{//changePasswordMode 활성화 :: 비밀번호가 맞는경우
                if(pwPreference.getString("pw","000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this,"비밀번호 변경모드가 활성화 되었습니다.", Toast.LENGTH_SHORT).show()
                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {//실패
                    showErrorPopup()
                }
            }
        }
    }
    private fun showErrorPopup(){
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호가 잘목되었습니다.")
            .setPositiveButton("확인"){ _, _ ->   }  //람다 함수로 해야할일을 바로 적어줌.
            .create()
            .show()
    }
}
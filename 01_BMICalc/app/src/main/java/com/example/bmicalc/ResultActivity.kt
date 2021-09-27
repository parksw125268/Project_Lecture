package com.example.bmicalc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class ResultActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        var height = intent.getIntExtra("height", 0)//intent 안에 height이라는 애가 없다면?? 0 을 널허라.
        var weight = intent.getIntExtra("weight", 0)//intent 안에 weight이라는 애가 없다면?? 0 을 널허라.
        val buttonBack = findViewById<Button>(R.id.buttoBack)


        val bmi =  weight / (height / 100.0).pow(2.0)
        var resultTest = when{
            bmi >= 35.0 ->"고도 비만"
            bmi >= 30.0 ->"중정도 비만"
            bmi >= 25.0 ->"경도 비만"
            bmi >= 23.0 ->"과체중"
            bmi >= 18.5 ->"정상체중"
            else -> "저체중"
        }

        val resultValueTextView = findViewById<TextView>(R.id.bmiTextView)
        val resultStringTextView = findViewById<TextView>(R.id.resultTextView)
        resultValueTextView.text = bmi.toString()
        resultStringTextView.text = resultTest

        buttonBack.setOnClickListener {
            this.finish()
        }
    }
}
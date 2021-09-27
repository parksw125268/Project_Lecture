package com.example.bmicalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val heightEditText: EditText = findViewById(R.id.heightEditText)
        val weightEditText  = findViewById<EditText>(R.id.weightEditText)//View가 무슨 타입인지 모름 그래서 <>안에 명시해줌.
        val resultButton  = findViewById<Button>(R.id.resultButton)

        resultButton.setOnClickListener {
            Log.d("MainActivity","ResultButton이 클릭되었습니다. ")
            if (heightEditText.text.isEmpty() || weightEditText.text.isEmpty()){
                Toast.makeText(this,"빈 값이 있습니다. ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //빈값이 아닌경우
            val height = heightEditText.text.toString().toInt()
            val weight=  weightEditText.text.toString().toInt()

            val intent1 = Intent(this, ResultActivity::class.java)
            intent1.putExtra("height",height)
            intent1.putExtra("weight",weight)
            heightEditText.text.clear()
            weightEditText.text.clear()
            startActivity(intent1)


        }


    }
}
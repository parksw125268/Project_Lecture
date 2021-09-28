package com.kotlin.Lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.size
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private val clearButton : Button by  lazy {
        findViewById<Button>(R.id.clearButton)
    }
    private val addButton : Button by lazy {
        findViewById<Button>(R.id.addButton)
    }
    private val runButton : Button by lazy {
        findViewById<Button>(R.id.runButton)
    }
    private val numberPicker : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }
    private val numberTextViewList : List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.textView1),
            findViewById<TextView>(R.id.textView2),
            findViewById<TextView>(R.id.textView3),
            findViewById<TextView>(R.id.textView4),
            findViewById<TextView>(R.id.textView5),
            findViewById<TextView>(R.id.textView6)
        )
    }

    private var didRun = false //이미 실행되었는지 체크할 변수
    private val pickNumberSet = hashSetOf<Int>()//똑같은 숫자는 더이상 Add되지 않도록

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }


    private fun initAddButton(){
        addButton.setOnClickListener {
            if(didRun){
                Toast.makeText(this,"초기화 후에 시도해주세요 ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.size >= 5){
                Toast.makeText(this,"번호는 5개까지만 선택 할 수 있습니다. ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.contains(numberPicker.value)){
                Toast.makeText(this,"이미 선택한 번호입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            setBackgroundColor(textView,numberPicker.value)
            textView.text = numberPicker.value.toString()
            pickNumberSet.add(numberPicker.value)
        }
    }
    private fun initRunButton() {
        runButton.setOnClickListener(){
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed { index, i ->  //idx와 value를 둘다 가져와준다.
                val textView = numberTextViewList[index]
               // textView.text = list[index].toString()
                textView.text = i.toString()
                setBackgroundColor(textView,i)
                textView.isVisible = true
            }
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener{
            pickNumberSet.clear()
            numberTextViewList.forEach { //textView가 하나씩 for문 돌리듯이 하나씩 튀어나온다.
                it.isVisible = false // 다시 숨기기
            }
            didRun = false
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>().apply{
            for (i in 1..45){
                if (pickNumberSet.contains(i))
                    continue
                this.add(i)
            }
            this.shuffle()
        }
        val newList  = pickNumberSet.toList() +  numberList.subList(0,6-pickNumberSet.size)
        return newList.sorted()
    }
    private fun setBackgroundColor(textView: TextView , num : Int){
        when (num){
            in  1..10 ->textView.background = ContextCompat.getDrawable(this,R.drawable.circle_yello)
            in 11..20 ->textView.background = ContextCompat.getDrawable(this,R.drawable.circle_blue)
            in 21..30 ->textView.background = ContextCompat.getDrawable(this,R.drawable.circle_red)
            in 31..40 ->textView.background = ContextCompat.getDrawable(this,R.drawable.circle_gray)
            else ->textView.background = ContextCompat.getDrawable(this,R.drawable.circle_green)
        }
    }
}
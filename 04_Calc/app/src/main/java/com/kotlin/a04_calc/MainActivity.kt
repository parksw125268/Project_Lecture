package com.kotlin.a04_calc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.room.Room
import com.kotlin.a04_calc.model.History
import kotlin.NumberFormatException

class MainActivity : AppCompatActivity() {
    private val expressionTextView : TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }
    private val resultTextView : TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }
    private val historyLayout : View by lazy {
        findViewById<View>(R.id.histroyLayout)
    }
    private val historyLinearLayout : LinearLayout by lazy{
        findViewById<LinearLayout>(R.id.historyLinearLayout)
    }

    var lastIsOperator = false //맨마지막이 operator인지
    var hasOperator = false//이미 연산자를 입력했는지

    lateinit var db: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
                applicationContext,
                AppDataBase::class.java,
                "historyDB"
        ).build()

        expressionTextView
        resultTextView

    }
    fun buttonClicked(v : View){
        when(v.id){
            R.id.button0 -> numberButtonClicked("0")
            R.id.button1 -> numberButtonClicked("1")
            R.id.button2 -> numberButtonClicked("2")
            R.id.button3 -> numberButtonClicked("3")
            R.id.button4 -> numberButtonClicked("4")
            R.id.button5 -> numberButtonClicked("5")
            R.id.button6 -> numberButtonClicked("6")
            R.id.button7 -> numberButtonClicked("7")
            R.id.button8 -> numberButtonClicked("8")
            R.id.button9 -> numberButtonClicked("9")
            R.id.buttonPlus -> opertorButtonClicked("+")
            R.id.buttonMinus ->opertorButtonClicked("-")
            R.id.buttonMulti ->opertorButtonClicked("*")
            R.id.buttonDiv ->opertorButtonClicked("/")
            R.id.buttonModulo ->opertorButtonClicked("%")
        }
    }
    private fun numberButtonClicked(number : String){
        if (lastIsOperator){
            expressionTextView.append(" ")
            lastIsOperator = false
        }
        val expressionText = expressionTextView.text.split(" ")
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15){
            Toast.makeText(this, "15자리까지밖에 입력할 수 없습니다.",Toast.LENGTH_SHORT).show()
            return
        }else if (expressionText.last().isEmpty() && number == "0" ){
            return
        }
        expressionTextView.append(number)
        resultTextView.text = calculatreExpression()
    }
    private fun opertorButtonClicked(operator : String){
        if (expressionTextView.text.isEmpty()) {
            return
        }
        when{
            lastIsOperator ->{
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator
            }
            hasOperator ->{
                Toast.makeText(this,"연산자는 한번만 사용할 수 있습니다.",Toast.LENGTH_SHORT).show()
                return
            }
            else ->{
                lastIsOperator = true
                hasOperator = true
                expressionTextView.append(" ${operator}")
            }
        }
        //연산자만 색상을 변경
        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            expressionTextView.text.length-1,
             expressionTextView.text.length,
             Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        expressionTextView.text = ssb
    }
    fun clearButtonClicked(v : View){
        expressionTextView.text = ""
        resultTextView.text = ""
        hasOperator = false
        lastIsOperator = false

    }
    fun historyButtonClicked(v : View){
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews() //리니어  레이아웃 안에 있는 모든 뷰들 삭제
        Thread(Runnable {           // 최신순으로 뒤집음.
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row,null, false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = "= ${it.result}"

                    historyLinearLayout.addView(historyView)
                }

            }
        }).start()
    }
    fun closeHistroyButtonClicked(v : View){
        historyLayout.isVisible = false

    }fun historyClearButtonClicked(v : View){
        historyLinearLayout.removeAllViews()
        Thread(Runnable { //db에서 모두 삭제
            db.historyDao().deleteAll()
        }).start()
    }
    fun resultButtonClicked(v : View){
        val expressionTexts = expressionTextView.text.split(" ")
        if (expressionTextView.text.isEmpty() || expressionTexts.size == 1){
            return
        }else if (expressionTexts.size != 3 && hasOperator){
            Toast.makeText(this,"아직 완성되지 않은 수식입니다.",Toast.LENGTH_SHORT).show()
            return
        }else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()){ //String.isNumber라는 함수가 없어서 확장함수 만듬.
            Toast.makeText(this,"오류가 발생하였습니다..",Toast.LENGTH_SHORT).show()
            return
        }
        //계산, 계산 결과 저장하기
        val expressionText = expressionTextView.text.toString()
        val resultText = calculatreExpression()

       //db저장
        Thread(Runnable {                         //ui는 pk이므로 자동으로 들어가는듯?
            db.historyDao().insertHistory(History(null,expressionText,resultText))
        }).start()

        resultTextView.text = "${expressionTextView.text} = "
        expressionTextView.text = resultText
        lastIsOperator = false
        hasOperator =  false
    }
    private fun calculatreExpression():String{
        val expressionText = expressionTextView.text.split(" ")
        if(hasOperator.not() || expressionText.size != 3){
            return ""
        }else if (expressionText[0].isNumber().not() || expressionText[2].isNumber().not()){ //String.isNumber라는 함수가 없어서 확장함수 만듬.
            return ""
        }
        val exp1= expressionText[0].toBigInteger()
        val exp2= expressionText[2].toBigInteger()
        val op = expressionText[1]
        return when(op){
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""

        }

    }
}
//String 확장 함수
fun String.isNumber():Boolean{
    try {
        this.toBigInteger()
        return true // 숫자로 바꿨는데 에러 안나면 int
    }catch (e: NumberFormatException){
        return false
    }
}
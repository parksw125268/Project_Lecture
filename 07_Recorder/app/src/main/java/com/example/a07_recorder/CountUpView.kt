package com.example.a07_recorder

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountUpView(
    context : Context,
    attrs : AttributeSet
) : AppCompatTextView (context,attrs) {
    private var startTimeStemp = 0L

    private val countUpAction : Runnable = object :Runnable{
        override fun run() {
            val currentTimeStemp = SystemClock.elapsedRealtime()
            val countTimeSeconds =( (currentTimeStemp - startTimeStemp)/1000L).toInt()
            updateCountTime(countTimeSeconds)

            handler?.postDelayed(this,1000L)
        }
    }
    fun startCountUp(){
        startTimeStemp = SystemClock.elapsedRealtime()
        handler?.post(countUpAction)
    }
    fun stopCountUp(){
        handler?.removeCallbacks(countUpAction)
    }
    fun clearCountTime(){
        updateCountTime(0)
    }

    private fun updateCountTime(countTimeSeconds : Int){
        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60
        text = "%02d:%02d".format(minutes,seconds)
    }
}
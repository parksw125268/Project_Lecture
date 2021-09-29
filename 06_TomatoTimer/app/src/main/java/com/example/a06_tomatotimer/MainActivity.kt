package com.example.a06_tomatotimer

import android.annotation.SuppressLint
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val remainMinutesTextView : TextView by lazy{
        findViewById<TextView>(R.id.remainMinuteTextView)
    }
    private val remainSecondsTextView : TextView by lazy{
        findViewById<TextView>(R.id.remainSecondsTextView)
    }
    private val seekBar : SeekBar by lazy {
        findViewById<SeekBar>(R.id.seekBar)
    }
    private var currenCountDownTimer: CountDownTimer? = null

    private val soundPool = SoundPool.Builder().build()
    private var tickingSoundId:Int? = null
    private var bellSoundId:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindView()
        initSounds()//사운드파일 로드
    }

    override fun onResume() {
        super.onResume()
        // soundPool.Resume(tickingSoundId) //사운드id 에 해당하는 사운드를 다시 재생
        soundPool.autoResume() //모든 사운드 풀의 사운드를 다시 재생
    }

    override fun onPause() {
        super.onPause()
       // soundPool.pause(tickingSoundId) //사운드id 에 해당하는 사운드만 멈춤
        soundPool.autoPause() //모든 사운드 풀의 사운드를  멈춤
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun initSounds() {
        //sound들을 메모리에 로드          //soundId를 Int형으로 반환
        tickingSoundId =  soundPool.load(this, R.raw.timer_ticking,1) //priority는 아무 효과가 없다고 하지만 1을 넣으라고 함.
        bellSoundId = soundPool.load(this, R.raw.timer_bell,1)
    }

    private fun bindView() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(fromUser){
                        updateRemainTimes(progress*60*1000L) }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    currenCountDownTimer?.cancel()
                    currenCountDownTimer = null
                    tickingSoundId?.let {
                        soundPool.stop(it)
                    }
                    soundPool.autoPause()
                }

                override fun onStopTrackingTouch(seekBar:  SeekBar?){
                    seekBar ?:return
                    if(seekBar.progress == 0){
                        currenCountDownTimer?.cancel()
                        currenCountDownTimer = null
                        tickingSoundId?.let {
                            soundPool.stop(it)
                        }
                        soundPool.autoPause()

                    }else{
                        startCountDown()
                    }
                }
            }
        )
    }
    private fun createCountDownTimer(initialMillis:Long) =
        object :CountDownTimer(initialMillis,1000L)  {
            override fun onFinish() {
                completeCountDown()
            }

            override fun onTick(millisUntilFinished: Long) {
                updateRemainTimes(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }
        }
    private fun startCountDown(){
        currenCountDownTimer = createCountDownTimer(seekBar.progress *60 *1000L)
        currenCountDownTimer?.start()
        tickingSoundId?.let { soundId ->
            //soundId, 좌우 볼륨, 우선순위, loop : -1 (영원히), 재생속도 f1
            soundPool.play(soundId, 1F, 1F, 0, -1, 1F)
        }
    }

    private fun completeCountDown() {
        updateRemainTimes(0)
        updateSeekBar(0)
        soundPool.autoPause()
        bellSoundId?.let {  soundId ->
            soundPool.play(soundId,1f,1f,0,-1,1f)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRemainTimes(remainMillis: Long){
        val remainSeconds = remainMillis/1000
        remainMinutesTextView.text = "%02d'".format(remainSeconds/60)
        remainSecondsTextView.text = "%02d".format(remainSeconds%60)
    }
    private fun updateSeekBar(remainMillis: Long){
        seekBar.progress = (remainMillis/1000/60).toInt()

    }

}
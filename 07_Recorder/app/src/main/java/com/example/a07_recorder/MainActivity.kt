package com.example.a07_recorder

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private val soundVisualizerView : SoundVisualizerView by lazy{
        findViewById(R.id.soundVisualizeView)
    }

    private val resetButton : Button by lazy {
        findViewById(R.id.resetButton)
    }
    private val recordTimeTextView : CountUpView by lazy{
        findViewById(R.id.recordTimeTextView)
    }
    private val recordButton : RecordButton by lazy {
        findViewById<RecordButton>(R.id.recordButton)
    }
    private val requiredPermissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    private val recordingFilePath :String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    private var recorder : MediaRecorder?= null
    private var player : MediaPlayer?=null
    private var state = State.BEFORE_RECORDING // State.~~ 를 하게되면 아래 set코드가 발동!
        set(value) {
            field = value //field는 state, value는 State.~~~
            resetButton.isEnabled = value == State.AFTER_RECORDING || value == State.ON_PLAYING
            recordButton.updateIconWithState(value)
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission() //앱 시작하자마자 ㄱㄱ
        initViews()
        bindViews()
        initVariables()
    }

    override fun onRequestPermissionsResult( //generate override
        requestCode: Int, //<-201이들어옴
        permissions: Array<out String>, //<- RECORD_AUDIO 권한이 들어올 것임.
        grantResults: IntArray          //<- 권한 요청한것에 대한 결과
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)//
        var audioRecordPermissionGranted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                                           grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
                                            //firstOrNull : list의 첫번째값을 반환하되 null이면 error
        if(!audioRecordPermissionGranted){
            finish()
        }
    }

    private fun requestAudioPermission(){
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)//권한 종류, 리퀘스트 코드.
    }
    private fun initViews(){
        recordButton.updateIconWithState(state)
    }
    private fun initVariables(){
        state = State.BEFORE_RECORDING
    }
    private fun bindViews(){
        soundVisualizerView.onRequestCurrentAmplitude = { //콜백 함수에 back함수
            recorder?.maxAmplitude ?: 0 //진폭값이 있으면 진폭값의 max를 받아오고 null이면 0을 리턴함.
        }
        resetButton.setOnClickListener{
            stopPlaying()
            soundVisualizerView.clearVisualization()
            recordTimeTextView.clearCountTime()
            state=State.BEFORE_RECORDING
        }

        recordButton.setOnClickListener {
            when(state){
                State.BEFORE_RECORDING->startRecording()
                State.ON_RECORDING->stopRecording()
                State.AFTER_RECORDING->startPlaying()
                State.ON_PLAYING->stopPlaying()
            }
        }
    }

    private fun startRecording(){
        recorder = MediaRecorder().run { //apply말고 한번 run의 개념 이해를 위해 run으로 시도해봄.
            setAudioSource(MediaRecorder.AudioSource.MIC) //마이크
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)//컨테이너 역할. 인코딩(압축)된 파일들을 차곡차곡 정리해둠
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)//AMR_NB방식으로 인코딩(버전에 상관없이 가능해서 AMR_NB방식을 체택)
            setOutputFile(recordingFilePath)//저장할 경로
            prepare() //세팅 다 끝내고 얘를 해줘야 녹음할 수 있는 상태가 됨.
            this //run은 맨마지막 값을 반환하므로 this를 써줌.
        }
        recorder?.start() //실제 녹음 시작.
        recordTimeTextView.startCountUp()
        soundVisualizerView.startVisualizing(false)
        state = State.ON_RECORDING
    }

    private fun stopRecording(){
        recorder?.run {
            stop()//녹음 멈춤.
            release()//메모리 해제
        }
        soundVisualizerView.stopVisualizing()
        recorder = null

        recordTimeTextView.stopCountUp()
        state=State.AFTER_RECORDING
    }

    private fun startPlaying(){
        player  = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare() //:온전히 재생하기위해서 데이터를 가져올때 까지 기다림 vs prepareasync 스트리밍 할때 방식
        }

        //녹음한것 재생이 다 되면.
        player?.setOnCompletionListener {
            stopPlaying()
            state = State.BEFORE_RECORDING
        }
        player?.start()
        recordTimeTextView.startCountUp()
        soundVisualizerView.startVisualizing(true)
        state = State.ON_PLAYING
    }
    private fun stopPlaying(){
        player?.release()
        player = null
        soundVisualizerView.stopVisualizing()
        recordTimeTextView.stopCountUp()
        state = State.AFTER_RECORDING
    }

    companion object{ //자바의 상수와 같은 것
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}
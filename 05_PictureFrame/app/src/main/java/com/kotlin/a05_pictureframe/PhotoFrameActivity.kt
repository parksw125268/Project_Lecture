package com.kotlin.a05_pictureframe

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity(){
    private val photoList  =  mutableListOf<Uri>()
    private var timer : Timer? = null
    private val photoImageView : ImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }
    private val backgroundPhotoImageView : ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }
    private var currentPosition =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreate","크리에이트")
        setContentView(R.layout.activity_photoframe)


        getPhotoUriFromIntent()
    }
    private fun getPhotoUriFromIntent(){
        val size = intent.getIntExtra("photoListSize",0)
        for (i in 0 until size){
            intent.getStringExtra("photo$i")?.let { //null이 오면 let문이 실행 안됨.
                photoList.add(Uri.parse(it))
            }
        }
    }
    private fun startTime(){
        timer = timer(period = 5000){
            runOnUiThread {
                Log.d("PhotoFrame","5초 지나감 ")
                val current = currentPosition
                val next = if(photoList.size <= currentPosition +1) 0 else currentPosition + 1
                backgroundPhotoImageView.setImageURI(photoList[current])

                photoImageView.alpha = 0.0f //투명도 0.0f 가 투명 1.0f 가 불투명
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate()
                        .alpha(1.0f) //
                        .setDuration(1000) //1초동안
                        .start()
                currentPosition = next
            }

        }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        Log.d("onStop","스탑")
    }

    override fun onStart() {
        super.onStart()
        startTime()
        Log.d("onStart","스타트")
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        Log.d("onDestroy","디스트로이")
    }

}
package com.example.a07_recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.concurrent.RunnableFuture

class SoundVisualizerView (
    context : Context,
    attrs : AttributeSet? = null
): View( context, attrs ){
    var onRequestCurrentAmplitude : (() -> Int)? = null //빈값 ()을 넘겨서 Int를 반환하는 익명함수(람다)를 선언. 그 함수가 null이면 null을 반환.
                                                        //콜백함수이고 콜 이라고 할수 잇음. 음성녹음 하는동안 계속해서 진폭값을 받아올 예정

                                    //ANTI_ALIAS_FLAG: 계단화 방지 라고해서 곡선을 잘 그릴수 있도록 도와줌
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)//색상지정
        strokeWidth  =  LINE_WIDTH //라인 굵기
        strokeCap    =  Paint.Cap.ROUND // 라인의 양 끄트머리를 둥글게
    }
    //== 그릴 사이즈 지정 ==
    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes : List<Int> = emptyList()//내가 그릴것을 list에 넣어서
    private var isReplaying = false
    private var replayingPosition = 0

    private val visualizeRepeatAction : Runnable =  object : Runnable{
        override fun run(){
            if(!isReplaying) {
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0  //함수 발동!
                drawingAmplitudes =
                    listOf(currentAmplitude) + drawingAmplitudes // 기존 리스트에 앞대가리로 붙도록 // 제일 앞대가리가 오른쪽부터 왼쪽으로 그려지니...
            }else{
                replayingPosition++
            }
            invalidate() //뷰 갱신. 안하면 데이터는 쌓이는데 뷰가 갱신이 안됨.

            handler?.postDelayed(this, 20L) //20미리 세컨드 마다 자기 자신을 반복
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) { // 디바이스에 맞게 사이즈를 가져다줌.
        super.onSizeChanged(w, h, oldw, oldh)                          // old 필요없음 .
        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {//그리기
        super.onDraw(canvas)

        canvas ?: return //null이면 return
        val centerY = drawingHeight/2f //그려질 사이즈의 절반.
        var offsetX = drawingWidth.toFloat() //그릴 위치 (처음에 오른쪽 끝에서 나와서 왼쪽으로 가면서 그려지도록 할 예정

        drawingAmplitudes
            .let {
                if (isReplaying){
                    drawingAmplitudes.takeLast(replayingPosition)
                }else{
                    drawingAmplitudes
                }
            }.forEach{ amplitude ->
            val lineLength = amplitude  / MAX_AMPLITUDE * drawingHeight * 0.8F//그릴려는 높이(drawingHeight) 데비 몇퍼센트 인가 //0.8은 좀더 작게
            offsetX -= LINE_SPACE //그릴 위치를 왼쪽으로 이동시킴
            //왼쪽으로 이동하다가 View를 넘어간다면
            if (offsetX < 0) return@forEach

            canvas.drawLine(
                offsetX, //시작 x
                centerY - lineLength/2f, //시작 y값
                offsetX, //끝 x값
                centerY + lineLength/2f, //끝 y값
                amplitudePaint //그릴것. paint. 긴 라인.
            )
        }
    }

    fun startVisualizing(isReplaying: Boolean){//replay인지 아닌지
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }
    fun stopVisualizing(){
        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualization(){
        drawingAmplitudes = emptyList()
        invalidate()
    }

    companion object{
        private const val LINE_WIDTH = 10F //오디오 녹음시 세로 라인의 굵기
        private const val LINE_SPACE = 15F //라인과 라인 사이의 간격
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
    }

}
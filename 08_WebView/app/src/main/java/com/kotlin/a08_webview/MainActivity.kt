package com.kotlin.a08_webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val goHomeButton: ImageButton by lazy{
        findViewById(R.id.goHomeButton)
    }
    private val goBackButton: ImageButton by lazy{
        findViewById(R.id.goBackButton)
    }
    private val goForwardButton: ImageButton by lazy{
        findViewById(R.id.goForwardButton)
    }

    private val webView : WebView by lazy{
        findViewById(R.id.webView)
    }
    private val addressBar : EditText by lazy{
        findViewById(R.id.addressBar)
    }
    private val refreshLayout : SwipeRefreshLayout by lazy{
        findViewById(R.id.refreshLayout)
    }
    private val progressBar : ContentLoadingProgressBar by lazy{
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }
    override fun onBackPressed() {
        if(webView.canGoBack()){ //뒤로 갈 수 있는지 확인
            webView.goBack()
        }else{
            super.onBackPressed()
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews(){
        webView.apply {
            webViewClient = WebViewClient()//기본 브라우저가 아니라 우리가 만든 웹뷰로 열겠다.
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true //자바스크립트가 안드에서는 기본적으로 차단됨(보안상의 이유) 그걸 허용.
            loadUrl(DEFAULT_URL)
        }
    }
    private fun bindViews(){
        goHomeButton.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }
        //v: 액션이 발생한 View(주소창) , actionId : actionDone , event : 눌렀는지 땠는 지 등등..
        //반환 : true : 이벤트 다썼다. false : 아직 다 안썼고 다른데서도 써야되니 남겨놔라
        addressBar.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                val loadingUrl = v.text.toString()
                if(URLUtil.isNetworkUrl(loadingUrl)) {//http of https 로 시작하는지 확인.
                    webView.loadUrl(loadingUrl)
                }else{
                    webView.loadUrl("http://${loadingUrl}")//https로 시작하는 주소는 http로 해도 자동으로 바까준다.
                }
            }
            return@setOnEditorActionListener false //키보드 닫는 처리도 해야되므로
        }
        goBackButton.setOnClickListener {
            webView.goBack()
        }
        goForwardButton.setOnClickListener {
            webView.goForward()
        }
        refreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }
    // inner 해주지 않으면 메인클레스에 속성에 접근 하지 못한다.
    // webView, Button 같은것들에 접근하려면  inner를 붙여줘야한다.
    inner class WebViewClient() : android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            refreshLayout.isRefreshing = false
            progressBar.hide()
            goBackButton.isEnabled = webView.canGoBack()
            goForwardButton.isEnabled = webView.canGoForward()
            addressBar.setText(url)

        }
    }

    //브라우저 차원의 이벤트들을 오버라이드해서 사용할 때.. (ex) alert같은 javascript의 이벤트들?)
    inner class WebChromeClient() : android.webkit.WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            //newProgressBar는 페이지 로딩 진행정보를 0~100 사이의 값으로 전달해줌
            //그런데 그냥 progressBar도 기본 세팅이 min 0 , max 100이므로 가공없이 그냥사용하면됨.
            progressBar.progress  = newProgress
        }
    }

    companion object{
        private const val DEFAULT_URL = "http://www.google.com"
    }
}
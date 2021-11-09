package com.kotlin.a10_daliyquote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val progressBar : ProgressBar by lazy{
        findViewById(R.id.progressBar)
    }
    private val viewPager: ViewPager2 by lazy{
        findViewById(R.id.viewPager)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initView(){
        //page 현재 눈에보이는 View, position : 오른쪽으로 넘기면 position이 0~2까지 증가 왼쪽은 0~-2까지 감소
        viewPager.setPageTransformer { page, position ->
            when{
                position.absoluteValue >= 1F ->{  // absoluteValue  : 절대값
                    page.alpha = 0F //투명도 0
                }
                position == 0f ->{
                    page.alpha = 1f //투명도 100
                }
                else -> {
                    page.alpha = 1f - position.absoluteValue
                }
            }
        }
    }

    private fun initData(){
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync( //비동기로 세팅이 된다.
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 0; //fetch는 기본적으로 12시간인데 0으로 설정함으로 앱을 들어갈때마다 fetch되게 함.
                }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            progressBar.visibility = View.GONE

            //fetch랑 activity랑 task를 완료했다.
            if(it.isSuccessful){
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                //어뎁터를 추가하고 랜더링 하는작업을 할것.
                displayQuotesPager(quotes, isNameRevealed)
            }
        }

    }

    private  fun parseQuotesJson(json: String):List<Quote>{
        val jsonArray = JSONArray(json) //JSONArray는 아래 JSONObject로 구성되어있다.
        var jsonList = emptyList<JSONObject>() //JSONArray 자체에서 for문을 돌릴수 있는게 없다. ㅠㅠ
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }
        return jsonList.map { //jsonList를 QuoteList로 바꾸는 것.
            Quote(
                    quote = it.getString("quote"),
                    name = it.getString("name")
            )
        }
    }
    private fun  displayQuotesPager(quotes:List<Quote>, isNameRevealed:Boolean){
        val adapter = QuotesPagerAdapter(
            quotes,
            isNameRevealed
        )
        viewPager.adapter = adapter

        //smoothScroll부드럽게 넘어가는것인데 이렇게 하면 처음 실행시 화면이 막 여러게 넘어가는게 보인다.
        viewPager.setCurrentItem(adapter.itemCount/2, false)
    }

}
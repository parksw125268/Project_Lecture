package com.kotlin.a10_daliyquote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy{
        findViewById(R.id.viewPager)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
    }

    private fun initData(){
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync( //비동기로 세팅이 된다.
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 0; //fetch는 기본적으로 12시간인데 0으로 설정함으로 앱을 들어갈때마다 fetch되게 함.
                }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
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
        viewPager.adapter = QuotesPagerAdapter(
                quotes,
                isNameRevealed
        )

        viewPager.adapter = QuotesPagerAdapter(
                quotes = quotes,
                isNameRevealed = isNameRevealed
        )
    }

}
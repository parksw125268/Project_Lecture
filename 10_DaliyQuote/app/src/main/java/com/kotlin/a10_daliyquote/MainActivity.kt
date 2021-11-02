package com.kotlin.a10_daliyquote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy{
        findViewById(R.id.viewPager)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }
    private fun initViews(){
        viewPager.adapter = QuotesPagerAdapter(
            listOf<Quote>(
                Quote("나는 생각한다. 고로 나는 존재한다.",
                    "데카르트"),
                Quote("12345.",
                    "12341"),
                Quote("686969.",
                    "69696799")

            )
        )
    }
}
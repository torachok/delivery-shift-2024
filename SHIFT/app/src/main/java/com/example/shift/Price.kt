package com.example.shift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Price : AppCompatActivity() {
    private lateinit var text1 : TextView
    private lateinit var text2 : TextView
    private lateinit var text3 : TextView
    private lateinit var text4 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_price)

        text1 = findViewById(R.id.nameusual)
        text2 = findViewById(R.id.usual)
        text3 = findViewById(R.id.nameexpress)
        text4 = findViewById(R.id.express)

        var arr = listOf(text1, text2, text3, text4)

        for(i in arr.indices){
            val args = intent.extras!!.get("texts") as Array<String>
            arr[i].text = args[i].capitalize()
        }
    }
}
package com.example.shift

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.shift.data.CalcAns
import com.example.shift.data.CalcResponse
import com.example.shift.data.DeliveryRepository
import com.example.shift.data.PackageResponse
import com.example.shift.data.PostResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner
    private lateinit var button: Button

    private val deliveryRepository = DeliveryRepository()
    private fun getPoints():Array<String>{
        val ans = mutableListOf<String>()
        val job = CoroutineScope(Dispatchers.IO).launch{
            val points = deliveryRepository.getPoints().points

            for(i in points) ans.add(i.name)
        }

        runBlocking {
            job.join()
        }
        return ans.toTypedArray()
    }

    private fun getTypes():Array<String>{
        val ans = mutableListOf<String>()
        val job = CoroutineScope(Dispatchers.IO).launch{
            val types = deliveryRepository.getTypes().packages

            for(i in types) ans.add(i.name)
        }
        runBlocking {
            job.join()
        }
        return ans.toTypedArray()
    }

    private fun getCalc(query:CalcResponse):CalcAns{
        lateinit var ans : CalcAns
        val job = CoroutineScope(Dispatchers.IO).launch{
            ans = deliveryRepository.getCalc(query)
        }
        runBlocking { job.join() }
        return ans
    }

    private fun onButtonClick(){
        val sender = spinner1.selectedItem.toString()
        val receiver = spinner2.selectedItem.toString()
        val type = spinner3.selectedItem.toString()

        var senderResponse : PostResponse? = null
        var receiverResponse : PostResponse? = null
        var typeResponse : PackageResponse? = null

        val job1 = CoroutineScope(Dispatchers.IO).async{
            val points = deliveryRepository.getPoints().points
            for (i in points){
                if(i.name == sender){
                    senderResponse = PostResponse(i.latitude, i.longitude)
                }
                if(i.name == receiver){
                    receiverResponse = PostResponse(i.latitude, i.longitude)
                }
            }
        }

        val job2 = CoroutineScope(Dispatchers.IO).async{
            val types = deliveryRepository.getTypes().packages
            for (i in types) {
                if (i.name == type) {
                    typeResponse = PackageResponse(i.length, i.width, i.weight, i.height)
                }
            }
        }
        runBlocking { job1.join(); job2.join() }

        val calcResponse = CalcResponse(typeResponse!!,senderResponse!!,receiverResponse!!)
        val ans = getCalc(calcResponse)
        val data = mutableListOf<String>()

        for(i in ans.options){
            data.add(i.name)
            data.add(i.price.toString())
        }

        val intent = Intent(this@MainActivity, Price::class.java)

        intent.putExtra("texts", data.toTypedArray())
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val array = getPoints()

        spinner1 = findViewById(R.id.spinner1)
        val adapter1 = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            array
        )
        spinner1.adapter = adapter1

        spinner2 = findViewById(R.id.spinner2)
        val adapter2 = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            array
        )
        spinner2.adapter = adapter2

        val array1 = getTypes()

        spinner3 = findViewById(R.id.spinner3)
        val adapter3 = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            array1
        )
        spinner3.adapter = adapter3

        button = findViewById(R.id.button)
        button.setOnClickListener{

        }
    }


}
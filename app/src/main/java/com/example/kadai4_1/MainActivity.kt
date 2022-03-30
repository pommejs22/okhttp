package com.example.kadai4_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //   東京のボタンを押した時取得する
        val btnTokyo = findViewById<Button>(R.id.btnTokyo)
        btnTokyo.setOnClickListener {

            GlobalScope.launch(Dispatchers.Main) {  // GlobalScopeは現在のスコープの影響を受けてほしくないコルーチンを起動したりするときに使用する
                // Androidではインターネット通信などを行う際にメインスレッドで行うと例外が発生する
                withContext(Dispatchers.IO) { //Dispatcherはコルーチンをどこで実行するのかというのかを決定する(どのスレッドで動作をするのか)

//                    try {
                        val client = OkHttpClient()  // OkHttpClientクラスのオブジェクト生成
                        val url = "https://weather.tsukumijima.net/api/forecast?city=130010"
                        val request = Request.Builder().url(url).get().build() //get requestを作る（GET)



                    // OkHttpClientクラスのenqueueメソッドでリクエストの送信及び、レスポンス（Responseオブジェクト）（Callbackメソッド ）取得ををする。
                    //GET リクエスト(非同期)
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {}



                        override fun onResponse(call: Call, response: Response) {
                            val responseText: String? = response.body?.string()


                            GlobalScope.launch {
                                withContext(Dispatchers.Main) {
                                    val json = JSONObject(responseText)
                                    val cityName = findViewById<TextView>(R.id.cityName)
                                    val cityWeatherToday =
                                        findViewById<TextView>(R.id.cityWeatherToday)
                                    val cityWeather = findViewById<TextView>(R.id.cityWeather)

                                    // jsonCityNameは、jsonの"title"名を定義(都市名が表示される)
                                    val jsonCityName = json.getString("title")
                                    cityName.text = jsonCityName.toString()
//
                                    val weatherJsonArray = json.getJSONArray("forecasts")
//                             配下のオブジェトから順にキーを取得する
//                                    for (i in 0 until weatherJsonArray.length()) {
                                        val forecastJSON = weatherJsonArray.getJSONObject(0)
                                     // 日付が表示される
                                        val jsonDate = forecastJSON.getString("date")

//                                val weatherJsonObject = weatherJsonArray.getJSONObject(0)
//                                val weatherJsonDate = weatherJsonObject.getString("date")
                                        cityWeatherToday.text = jsonDate

                                    // 天気が表示される
                                        val jsonTelop = forecastJSON.getString("telop")
                                        cityWeather.text = jsonTelop






                                    }
//                                }
                            }
                        }
                    })
                }
            }
        }
    }
}
package com.example.sunnyweather.ui.weather

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView

        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_weather)

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }

        if (viewModel.plcaeName.isEmpty()) {
            viewModel.plcaeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
//            Log.d("WeatherActivity", "${viewModel.plcaeName}: ${viewModel.locationLng}, " +
//                    "${viewModel.locationLat}")
            if (weather != null) {
//                Log.d("WeatherActivity", weather.daily.temperature.toString())
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
        val placeName: TextView = findViewById(R.id.placeName)
        placeName.text = viewModel.plcaeName
        val realtime = weather.realtime
        val daily = weather.daily
        // 填充 now.xml 布局中的数据
        val currentTemp: TextView = findViewById(R.id.currentTemp)
        val currentSky: TextView = findViewById(R.id.currentSky)
        val currentAQI: TextView = findViewById(R.id.currentAQI)
        val nowLayout: RelativeLayout = findViewById(R.id.nowLayout)

        val currentTempText = "${realtime.temperature.toInt()} °C"
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"

        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // 填充 forecast.xml 布局中的数据
        val forecastLayout: LinearLayout = findViewById(R.id.forecastLayout)

        forecastLayout.removeAllViews()
        var days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastLayout, false)

            val dateInfo: TextView = view.findViewById(R.id.dateIndo)
            val skyIcon: ImageView = view.findViewById(R.id.skyIcon)
            val skyInfo: TextView = view.findViewById(R.id.skyInfo)
            val temperatureInfo: TextView = view.findViewById(R.id.temperatureInfo)
            val simpleDdateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sky = getSky(skycon.value)
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} °C"

            // 3. 示例：假设 skycon.date 是字符串 "2025-09-05T00:00+08:00"
            val dateString = skycon.date  // String 类型
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX", Locale.getDefault())
            val date: Date = inputFormat.parse(dateString)  // ✅ 先 parse 成 Date
            val formattedDate = simpleDdateFormat.format(date)   // ✅ 再 format 成你需要的格式

            dateInfo.text = formattedDate
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }

        // 填充 life_index.xml 布局中的数据
        val lifeIndex = daily.lifeIndex
        val clodRiskText: TextView = findViewById(R.id.coldRiskText)
        val dressingText: TextView = findViewById(R.id.dressingText)
        val ultravioletText: TextView = findViewById(R.id.ultravioletText)
        val carWashingText: TextView = findViewById(R.id.carWashingText)
        val weatherLayout: ScrollView = findViewById(R.id.weatherLayout)

        clodRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }
}
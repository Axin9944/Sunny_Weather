package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)

    data class Realtime(val skycon: String, val temperature: Float,
        @SerializedName("air_quality")val airQuality: AirQuality, val wind: Wind,
        @SerializedName("apparent_temperature")val apparentTemperature: Float)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float, val usa: Float)

    data class Wind(val speed: Float, val direction: Float)
}

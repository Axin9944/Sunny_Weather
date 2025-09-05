package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class DailyResponse(val status: String, val result: Result) {

    data class Result(val daily: Daily)

    data class Daily(val temperature: List<Temperature>, val skycon: List<Skycon>,
        @SerializedName("life_index") val lifeIndex: LifeIndex)

    data class Temperature(val max: Float, val date: String, val min: Float, val avg: Float)

    data class Skycon(val value: String, val date: String)

    data class LifeIndex(val ultraviolet: List<LifeDescription>, val carWashing: List<LifeDescription>,
        val dressing: List<LifeDescription>, val comfort: List<LifeDescription>,
        val coldRisk: List<LifeDescription>)

    data class LifeDescription(val date: String, val desc: String)
}

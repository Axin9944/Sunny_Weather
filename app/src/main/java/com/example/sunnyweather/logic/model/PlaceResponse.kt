package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(val name:String, val location: Location,
    @SerializedName("formatted_address") val address: String, val id: String,
    @SerializedName("place_id") val place: String)

data class Location(val lng: String, val lat: String)
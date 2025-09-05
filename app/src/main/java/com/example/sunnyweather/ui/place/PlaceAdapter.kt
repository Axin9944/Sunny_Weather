package com.example.sunnyweather.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placName: TextView = view.findViewById(R.id.placeName)
        val placAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,
            parent, false)

//        val holder = ViewHolder(view)
        /*holder.itemView.setOnClickListener {
            var position = holder.bindingAdapterPosition
            Log.d("PlaceAdapter", position.toString())
            if (position == RecyclerView.NO_POSITION) {
                position = 0
            }
            val place = placeList[position]

            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }*/

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placName.text = place.name
        holder.placAddress.text = place.address

        holder.itemView.setOnClickListener {
            var position = holder.bindingAdapterPosition
            Log.d("PlaceAdapter", position.toString())
            if (position == RecyclerView.NO_POSITION) {
                position = 0
            }
            val place = placeList[position]

            val intent = Intent(fragment.requireContext(), WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }

    }

    override fun getItemCount() = placeList.size
}
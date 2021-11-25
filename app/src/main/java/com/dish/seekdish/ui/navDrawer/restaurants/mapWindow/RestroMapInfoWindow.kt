package com.dish.seekdish.ui.navDrawer.restaurants.mapWindow

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dish.seekdish.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class RestroMapInfoWindow(
    val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.info_window_restro_map, null)
        val tvRestro = view.findViewById<TextView>(R.id.tvRestro)
        val imgInfoWindow = view.findViewById<ImageView>(R.id.imgInfoWindow)
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)

        val infoWindowGoogleMap = marker.tag as InfoWindowModel

        /* GlideApp.with(context)
             .load(infoWindowGoogleMap.imageUrl)
             .override(50,50)
             .placeholder(R.drawable.app_logo)
             .into(imgInfoWindow)*/
        tvRestro.setText(infoWindowGoogleMap.restroTitle)
        tvAddress.text = infoWindowGoogleMap.address.toString()
        Picasso.with(context).load(infoWindowGoogleMap.imageUrl).resize(50, 50)
            .centerCrop().noFade()
            .placeholder(R.drawable.app_logo)
            .into(imgInfoWindow,  MarkerCallback(marker));



        return view
    }

}

// introduced as per image was showing when double click, so this solved the issue with picasso
internal class MarkerCallback(marker:Marker):Callback {
    var marker: Marker? = null
    init{
        this.marker = marker
    }
    override fun onError() {
        Log.e(javaClass.getSimpleName(), "Error loading thumbnail!")
    }
    override fun onSuccess() {
        if (marker != null && marker!!.isInfoWindowShown())
        {
            marker!!.showInfoWindow()
        }
    }
}

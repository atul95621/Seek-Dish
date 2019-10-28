package com.dish.seekdish.ui.home.mapInfoWindow

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowGoogleMap(
    val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.custom_info_window, null)


        val star_rating=  view.findViewById<RatingBar>(R.id.infowindow_rating)
        val euro_rating=  view.findViewById<RatingBar>(R.id.euro_rating)
        val tvRestro = view.findViewById<TextView>(R.id.tvRestro)
        val imgInfoWindow = view.findViewById<ImageView>(R.id.imgInfoWindow)


        val infoWindowGoogleMap = marker.tag as InfoWindowData

        val imageId = context.getResources().getIdentifier(
            infoWindowGoogleMap!!.imageUrl!!.toLowerCase(),
            "drawable", context.getPackageName()
        )
        //        imgUrl.setImageResource(imageId);
//        imgInfoWindow.setImageResource(R.drawable.ic_foodex)


        GlideApp.with(context)
            .load(infoWindowGoogleMap.imageUrl)
            .into(imgInfoWindow)
        tvRestro.setText(infoWindowGoogleMap.restroTitle)

        star_rating.rating= infoWindowGoogleMap.starRating!!.toFloat()
        euro_rating.rating= infoWindowGoogleMap.euroRating!!.toFloat()

        Log.e(
            "rate",
            "star:  " + infoWindowGoogleMap.starRating + "\neuro :" + infoWindowGoogleMap.euroRating + "\nimageurl :" + infoWindowGoogleMap.imageUrl + "\neuro :" + infoWindowGoogleMap.restroTitle
        )

        return view
    }

}
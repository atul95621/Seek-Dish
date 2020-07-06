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
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class CustomInfoWindowGoogleMap(
    val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.custom_info_window, null)


        val star_rating = view.findViewById<RatingBar>(R.id.infowindow_rating)
//        val euro_rating = view.findViewById<RatingBar>(R.id.euro_rating)
        val tvRestro = view.findViewById<TextView>(R.id.tvRestro)
        val imgInfoWindow = view.findViewById<ImageView>(R.id.imgInfoWindow)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)

        val infoWindowGoogleMap = marker.tag as InfoWindowData

        val imageId = context.getResources().getIdentifier(
            infoWindowGoogleMap!!.imageUrl!!.toLowerCase(),
            "drawable", context.getPackageName()
        )

        GlideApp.with(context)
            .load(infoWindowGoogleMap.imageUrl)
            .placeholder(R.drawable.app_logo)
            .override(50,50)
            .into(imgInfoWindow)

//        Log.e("imageuRL","  "+imageId+"    "+infoWindowGoogleMap.imageUrl)

        tvRestro.setText(infoWindowGoogleMap.restroTitle)
        star_rating.rating = infoWindowGoogleMap.starRating!!.toFloat()
        tvPrice.setText(infoWindowGoogleMap.mealSymbol + " " + infoWindowGoogleMap.mealPrice)
        return view
    }

}

class InfoWindowRefresher(private val markerToRefresh: Marker) :
     Callback {
    override fun onSuccess() {
        markerToRefresh.showInfoWindow()
    }

    override fun onError() {}
}
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
import com.dish.seekdish.ui.navDrawer.restaurants.mapWindow.MarkerCallback
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

   /*     Picasso.with(context).load(infoWindowGoogleMap.imageUrl).resize(50, 50)
            .centerCrop().noFade()
            .placeholder(R.drawable.app_logo)
            .into(imgInfoWindow,  MarkerCallback(marker));*/
        Picasso.with(context).load(infoWindowGoogleMap.imageUrl).resize(50, 50)
            .centerCrop().noFade()
            .placeholder(R.drawable.app_logo)
            .into(imgInfoWindow);

        tvRestro.setText(infoWindowGoogleMap.restroTitle)
        star_rating.rating = infoWindowGoogleMap.starRating!!.toFloat()
        tvPrice.setText(infoWindowGoogleMap.mealSymbol + " " + infoWindowGoogleMap.mealPrice)
        return view
    }

}

// introduced as per image was showing when double click, so this solved the issue with picasso
internal class MarkerCallback(marker:Marker): Callback {
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
package com.dish.seekdish.ui.navDrawer.myFavourite.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions



class FavouriteMapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {


    private lateinit var myContext: HomeActivity

    private var mMap: GoogleMap? = null

    val PERMISSION_REQUEST_LOCATION_CODE = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        myContext = activity as HomeActivity

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite_map, container, false)


        //main map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFavourite) as SupportMapFragment
        mapFragment.getMapAsync(this)


        return view

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney, Australia, and move the camera.
        val sydney = LatLng(-34.0, 151.0)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))


        if (ContextCompat.checkSelfPermission(
                myContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // enable the location button
            mMap?.isMyLocationEnabled = true
            mMap?.getUiSettings()?.setMyLocationButtonEnabled(true);
            mMap!!.setMyLocationEnabled(true);


            mMap!!.setOnMyLocationButtonClickListener(this);
            mMap!!.setOnMyLocationClickListener(this);

        } else {
            requestImagePermission()
        }

    }

    //images
    private fun requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_REQUEST_LOCATION_CODE
            )
        }

    }


    override fun onMyLocationButtonClick(): Boolean {
        mMap?.setMinZoomPreference(5f)
        return false
    }

    override fun onMyLocationClick(location: Location) {
        mMap?.setMinZoomPreference(5f)

        val circleOptions = CircleOptions()
        circleOptions.center(
            LatLng(
                location.latitude,
                location.longitude
            )
        )

        Log.e("latlong1", " " + location.latitude)
        Log.e("latlong2", " " + location.longitude)

        circleOptions.radius(100.0)
        circleOptions.fillColor(Color.RED)
        circleOptions.strokeWidth(6f)

        mMap?.addCircle(circleOptions)
    }


}


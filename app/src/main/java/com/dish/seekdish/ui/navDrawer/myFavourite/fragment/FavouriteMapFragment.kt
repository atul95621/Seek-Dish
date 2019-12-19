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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dish.seekdish.Constants
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.home.mapInfoWindow.CustomInfoWindowGoogleMap
import com.dish.seekdish.ui.home.mapInfoWindow.InfoWindowData
import com.dish.seekdish.ui.navDrawer.myFavourite.VM.FavoriteVM
import com.dish.seekdish.ui.navDrawer.toDo.list.Data_todo
import com.dish.seekdish.util.SessionManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers


class FavouriteMapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {


    private lateinit var myContext: HomeActivity

    private var mMap: GoogleMap? = null

    val PERMISSION_REQUEST_LOCATION_CODE = 1

    var favoriteVM: FavoriteVM? = null

    internal var arrayList = ArrayList<Data_todo>()

    internal var markerMapHash: MutableMap<Marker, InfoWindowData> = HashMap<Marker, InfoWindowData>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite_map, container, false)

        myContext = activity as HomeActivity
        favoriteVM = ViewModelProviders.of(this).get(FavoriteVM::class.java)


        //main map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFavourite) as SupportMapFragment
        mapFragment.getMapAsync(this)


        return view

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap!!.clear();

        //check connection
        if (myContext.connectionDetector.isConnectingToInternet) {

            getMapList()
            getMapRespObserver()

            var customInfoWindow = CustomInfoWindowGoogleMap(conxt)
            mMap!!.setInfoWindowAdapter(customInfoWindow);

        } else {
            showSnackBar(getString(R.string.check_connection))
        }

        googleMap.setOnMarkerClickListener {


            false
        }


        googleMap.setOnInfoWindowClickListener { marker ->
            Log.e("rate", "info window clicked")
        }


        var cameraMove = LatLng(Constants.Latitude.toDouble(), Constants.Longitude.toDouble())
//        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(cameraMove))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraMove, 14F));


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
/*        mMap?.setMinZoomPreference(5f)

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

        mMap?.addCircle(circleOptions)*/
    }

    fun getMapRespObserver() {


        //observe
        favoriteVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        favoriteVM!!.getFavoriteLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspgemap", response.toString())

                Log.e("rspgetmaptat", response.status.toString())

                if (response.status == 1) {

                    arrayList = response.data


                    activity?.runOnUiThread(
                        object : Runnable {
                            override fun run() {

                                for (i in 0 until arrayList.size) {

                                    var latititude = arrayList[i].latitude.toDouble()
                                    var longitude = arrayList[i].longitude.toDouble()
                                    var imageUrl = arrayList[i].meal_image
                                    var starRate = arrayList[i].meal_avg_rating.toString()
                                    var euroRate = arrayList[i].budget_rating.toString()
                                    var mealName = arrayList[i].name


                                    // adding custom info window
                                    var locationPos = LatLng(latititude, longitude);
                                    var markerOptions = MarkerOptions();
                                    markerOptions.position(locationPos)
                                        .title(arrayList[i].name)
                                        .icon(bitmapDescriptorFromVector(myContext, R.drawable.ic_markersvg))


                                    var info = InfoWindowData(
                                        imageUrl,
                                        starRate,
                                        euroRate,
                                        mealName
                                    );


                                    var marker = mMap!!.addMarker(markerOptions);
                                    marker.setTag(info)
                                    marker.showInfoWindow()

                                    markerMapHash.put(marker, info)
                                }
                            }
                        }
                    )
                }

            } else {
                showSnackBar("OOps! Error Occured.")

                Log.e("rspMaperror", "else error")

            }
        })
    }

    private fun getMapList() {
        // hitting api
        favoriteVM?.doGetFavList(
            sessionManager.getValue(SessionManager.USER_ID).toString()
        )
    }


}


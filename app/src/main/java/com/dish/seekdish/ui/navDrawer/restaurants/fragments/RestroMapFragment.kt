package com.dish.seekdish.ui.navDrawer.restaurants.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dish.seekdish.Constants
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.home.dataModel.Location
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.Data_Restro_Map
import com.dish.seekdish.ui.navDrawer.restaurants.mapWindow.InfoWindowModel
import com.dish.seekdish.ui.navDrawer.restaurants.mapWindow.RestroMapInfoWindow
import com.dish.seekdish.ui.navDrawer.restaurants.viewModel.RestroMapVM
import com.dish.seekdish.util.BaseFragment
import com.dish.seekdish.util.SessionManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RestroMapFragment : BaseFragment(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {


    lateinit var marker: Marker
    private lateinit var myContext: HomeActivity
    private var mMap: GoogleMap? = null
    val PERMISSION_REQUEST_LOCATION_CODE = 1
    var restroMapVM: RestroMapVM? = null
    internal var arrayList = ArrayList<Data_Restro_Map>()
    var bitmapdraw: BitmapDrawable? = null
    var customSizeMarker: Bitmap? = null;

    var restroIDInfoWindow = ""
    internal var markerMapHash: MutableMap<Marker, InfoWindowModel> =
        HashMap<Marker, InfoWindowModel>()
    var markerSet: Hashtable<String, Boolean> = Hashtable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        myContext = activity as HomeActivity
        restroMapVM = ViewModelProvider(this).get(RestroMapVM::class.java)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restro_map, container, false)
        //main map fragment
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapRestro) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.clear();

        //check connection
        if (myContext.connectionDetector.isConnectingToInternet) {

            //hitting api
            getMapMarkers()
            //observer
            getMapRespObserver()

            var customInfoWindow = RestroMapInfoWindow(conxt,markerSet)
            mMap!!.setInfoWindowAdapter(customInfoWindow);

        } else {
            showSnackBar(myContext, getString(R.string.check_connection))
        }

        googleMap?.setOnMarkerClickListener { marker ->

            false
        }



        googleMap.setOnInfoWindowClickListener { marker ->

            val infoModel: InfoWindowModel? = marker.tag as InfoWindowModel?


            var restr_id = infoModel?.restro_id.toString()
            val intent = Intent(myContext, RestroDescrpActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restr_id)
            myContext.startActivity(intent)
        }


        /*  // Add a marker in Sydney, Australia, and move the camera.
          val sydney = LatLng(-34.0, 151.0)
          mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
          mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/


        /*     // adding custom info window
             var snowqualmie = LatLng(47.5287132, -121.8253906);

             var markerOptions = MarkerOptions();
             markerOptions.position(snowqualmie)
                 .title("Snowqualmie Falls")
                 .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

             var info = InfoWindowData(" ", "3", "4", "hOTEL tITLE");


             var marker = mMap!!.addMarker(markerOptions);
             marker.setTag(info);*/
//        marker.showInfoWindow();

        var bitmapdraw = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_marker, null);
        val b = bitmapdraw?.let { drawableToBitmap(it) }
        customSizeMarker = b?.let { Bitmap.createScaledBitmap(it, 100, 100, false) }


//        var cameraMove = LatLng(Constants.Latitude.toDouble(), Constants.Longitude.toDouble())
////        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(cameraMove))
//        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraMove, 14F));


        if (ContextCompat.checkSelfPermission(
                myContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // enable the location button
            mMap?.isMyLocationEnabled = true
            mMap?.getUiSettings()?.setMyLocationButtonEnabled(true);
            mMap!!.setMyLocationEnabled(true);
            // to hide the location center buttton
            mMap!!.getUiSettings().setMyLocationButtonEnabled(false);

            mMap!!.setOnMyLocationButtonClickListener(this);
            mMap!!.setOnMyLocationClickListener(this);

        } else {
            requestImagePermission()
        }

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        marker.showInfoWindow();
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    fun onMyLocationClick(location: Location) {
        /*       mMap?.setMinZoomPreference(5f)

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


    override fun onMyLocationClick(p0: android.location.Location) {
    }


    fun getMapRespObserver() {
        //observe
        restroMapVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        restroMapVM!!.getMapData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.status == 1) {

                    arrayList = response.data

                    Thread(Runnable {

                        // try to touch View of UI thread
                        myContext.runOnUiThread(java.lang.Runnable {

                            if (arrayList.size > 0 && arrayList.isNotEmpty()) {

                                for (i in 0 until arrayList.size) {

                                    var latititude = arrayList[i].latitude.toDouble()
                                    var longitude = arrayList[i].longitude.toDouble()
                                    var imageUrl = arrayList[i].restaurant_image
                                    var starRate = arrayList[i].rating.toString()
                                    var mealName = arrayList[i].name
                                    var address =
                                        arrayList[i].street + ", " + arrayList[i].city + ", " + arrayList[i].zipcode
                                    var restro_id = arrayList[i].id

                                    // adding custom info window
                                    var locationPos = LatLng(latititude, longitude);
                                    var markerOptions = MarkerOptions();
                                    markerOptions.position(locationPos)
                                        .title(arrayList[i].name)
                                        .icon(
                                            bitmapDescriptorFromVector(
                                                myContext,
                                                R.drawable.ic_markersvg
                                            )
                                        )  // custom size maekr is used here

                                    var info = InfoWindowModel(
                                        imageUrl,
                                        starRate,
                                        mealName,
                                        address,
                                        restro_id
                                    );

                                    var marker = mMap!!.addMarker(markerOptions);
                                    marker.setTag(info)
//                                    marker.showInfoWindow()

                                    markerMapHash.put(marker, info)
                                    markerSet.put(marker.getId(), false);

                                }

                                var cameraMove = LatLng(
                                    arrayList[0].latitude.toDouble(),
                                    arrayList[0].longitude.toDouble()
                                )
                                mMap!!.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        cameraMove,
                                        14F
                                    )
                                );

                            }
                        })
                    }).start()
                } else {
                    showSnackBar(myContext, response.message)
                }

            } else {
                showSnackBar(myContext, myContext.getString(R.string.error_occured))
            }
        })
    }

    private fun getMapMarkers() {

        var radius: String = sessionManager.getValue(SessionManager.RADIUS)
        if (radius != null && radius != "null" && radius != "") {
            radius = sessionManager.getValue(SessionManager.RADIUS)
        } else {
            radius = Constants.radius
        }

        // hitting api
        restroMapVM?.doGetRestroMapData(
            sessionManager.getValue(SessionManager.USER_ID).toString(),
            sessionManager.getValue(SessionManager.LONGITUDE),
            sessionManager.getValue(SessionManager.LATITUDE),
            radius,
            sessionManager.getValue(SessionManager.RESTRO_MAP_QTY),

            )
    }
}


package com.dish.seekdish.ui.home.fragments

import android.Manifest
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.home.dataModel.Data_Home_Map
import com.dish.seekdish.ui.home.viewModel.MapHomeVM
import com.dish.seekdish.ui.home.mapInfoWindow.CustomInfoWindowGoogleMap
import com.dish.seekdish.ui.home.mapInfoWindow.InfoWindowData
import com.dish.seekdish.util.SessionManager
import com.google.android.gms.maps.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.res.ResourcesCompat
import java.util.HashMap


class HomeMapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {


    lateinit var marker: Marker
    private lateinit var myContext: HomeActivity

    private var mMap: GoogleMap? = null

    val PERMISSION_REQUEST_LOCATION_CODE = 1

    var mapHomeVM: MapHomeVM? = null
    internal var arrayList = ArrayList<Data_Home_Map>()
    var bitmapdraw: BitmapDrawable? = null
    var customSizeMarker: Bitmap? = null;

    internal var markerMapHash: MutableMap<Marker, InfoWindowData> = HashMap<Marker, InfoWindowData>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        myContext = activity as HomeActivity
        mapHomeVM = ViewModelProviders.of(this).get(MapHomeVM::class.java)


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_map, container, false)


        //main map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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
        customSizeMarker = Bitmap.createScaledBitmap(b, 100, 100, false)


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

    override fun onMyLocationClick(location: Location) {
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


    fun getMapRespObserver() {


        //observe
        mapHomeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        mapHomeVM!!.getMapData.observe(this, Observer { response ->
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
                            var starRate = arrayList[i].rating.toString()
                            var euroRate = arrayList[i].budget.toString()
                            var mealName = arrayList[i].name


                            // adding custom info window
                            var locationPos = LatLng(latititude, longitude);
                            var markerOptions = MarkerOptions();
                            markerOptions.position(locationPos)
                                .title(arrayList[i].name)
                                .icon(BitmapDescriptorFactory.fromBitmap(customSizeMarker));   // custom size maekr is used here

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



                 /*   // try to touch View of UI thread
                    activity?.runOnUiThread(java.lang.Runnable {
                        // adding custom info window
                        var locationPos = LatLng(43.3367589, 3.224927999999977);
                        var markerOptions = MarkerOptions();
                        markerOptions.position(locationPos)
                            .title("")
                            .icon(BitmapDescriptorFactory.fromBitmap(customSizeMarker));   // custom size maekr is used here

                        var info = InfoWindowData("http://seekdish.com/seekdish_android/public/uploads/images/meals/199_20180909_131056.jpg", "3", "2", "mealName");


                        var marker = mMap!!.addMarker(markerOptions);
                        marker.setTag(info)
                        marker.showInfoWindow()

                    })*/

                /*    for(key in markerMapHash.keys){
                        Log.e("Element:   ","Element at key $key : ${markerMapHash[key]}")
                    }
*/

                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspMaperror", "else error")

            }
        })
    }

    private fun getMapMarkers() {

        var radius: String = sessionManager.getValue(SessionManager.RADIUS)
        if (radius != null && radius != "null" && radius != "") {
            radius = sessionManager.getValue(SessionManager.RADIUS)
        } else {
            radius = "15"
        }

        // hitting api
        mapHomeVM?.doGetMapData(
            sessionManager.getValue(SessionManager.USER_ID).toString(),
            sessionManager.getValue(SessionManager.LONGITUDE),
            sessionManager.getValue(SessionManager.LATITUDE), radius
        )
    }


}
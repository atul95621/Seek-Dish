package com.dish.seekdish.ui.home.fragments

import android.Manifest
import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
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
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import java.util.*
import kotlin.collections.ArrayList


class HomeMapFragment : BaseFragment(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    lateinit var marker: Marker
    private lateinit var myContext: HomeActivity
    private var mMap: GoogleMap? = null
    val PERMISSION_REQUEST_LOCATION_CODE = 1
    var mapHomeVM: MapHomeVM? = null
    internal var arrayList = ArrayList<Data_Home_Map>()
    var bitmapdraw: BitmapDrawable? = null
    var customSizeMarker: Bitmap? = null;

    internal var markerMapHash: MutableMap<Marker, InfoWindowData> =
        HashMap<Marker, InfoWindowData>()
    var markerSet: Hashtable<String, Boolean> = Hashtable()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        myContext = activity as HomeActivity
        mapHomeVM = ViewModelProvider(this).get(MapHomeVM::class.java)

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
            var customInfoWindow = CustomInfoWindowGoogleMap(conxt,markerSet)
            mMap!!.setInfoWindowAdapter(customInfoWindow);
        } else {
            showSnackBar(myContext, getString(R.string.check_connection))
        }

// did this because image in infowindow was opening on 2nd click, this make the infowindow to close
// and open again to show image
        mMap?.setOnMarkerClickListener { marker ->

            Log.e("infoopened", "  " + marker)
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            true
        }


        googleMap.setOnInfoWindowClickListener { marker ->
            val infoModel: InfoWindowData? = marker.tag as InfoWindowData?
            Log.e("MAP22:  ", "entered setOnInfoWindowClickListener")
            Log.e("MAP22:  ", "MEAL_ID  ${infoModel?.mealId.toString()}")
            Log.e("MAP22:  ", "RESTAURANT_ID  ${infoModel?.restaurantId.toString()}")

            val intent = Intent(myContext, DishDescriptionActivity::class.java)
            intent.putExtra("MEAL_ID", infoModel?.mealId.toString())
            intent.putExtra("RESTAURANT_ID", infoModel?.restaurantId.toString())
            myContext.startActivity(intent)
        }


        var bitmapdraw = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_marker, null);
        val b = bitmapdraw?.let { drawableToBitmap(it) }
        customSizeMarker = Bitmap.createScaledBitmap(b!!, 100, 100, false)

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
        Log.e("infoopened2", "  " + p0)
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

    }


    fun getMapRespObserver() {


        //observe
        mapHomeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        mapHomeVM!!.getMapData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    arrayList = response.data
                    if (arrayList.size > 0 && arrayList.isNotEmpty()) {
                        Thread(Runnable {

                            // try to touch View of UI thread
                            myContext.runOnUiThread(java.lang.Runnable {
                                for (i in 0 until arrayList.size) {

                                    var latititude = arrayList[i].latitude.toDouble()
                                    var longitude = arrayList[i].longitude.toDouble()
                                    var imageUrl = arrayList[i].meal_image
                                    var starRate = arrayList[i].rating.toString()
                                    var euroRate = arrayList[i].budget.toString()
                                    var mealName = arrayList[i].name
                                    var mealId = arrayList[i].meal_id.toString()
                                    var restroId = arrayList[i].restro_id.toString()
                                    var mealPrice = arrayList[i].meal_price.toString() ?: "null"
                                    var mealSymbol = arrayList[i].meal_symbol.toString()

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
                                        )

                                    var info = InfoWindowData(
                                        imageUrl,
                                        starRate,
                                        euroRate,
                                        mealName,
                                        mealId.toString(),
                                        restroId.toString(),
                                        mealPrice,
                                        mealSymbol
                                    )


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
                            })
                        }).start()

                    }

                } else {
                    showSnackBar(myContext, response.message)
                }

            } else {
                showSnackBar(
                    myContext,
                    resources.getString(R.string.error_occured) + "  $response"
                );
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
        mapHomeVM?.doGetMapData(
            sessionManager.getValue(SessionManager.USER_ID).toString(),
            sessionManager.getValue(SessionManager.LONGITUDE),
            sessionManager.getValue(SessionManager.LATITUDE),
            radius,
            sessionManager.getValue(SessionManager.MEAL_MAP_QTY),
        )
    }
}



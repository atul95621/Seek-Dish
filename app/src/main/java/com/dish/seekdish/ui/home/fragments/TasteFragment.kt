package com.dish.seekdish.ui.home.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.custom.PaginationScrollListener
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.home.adapter.TasteFragAdapter
import com.dish.seekdish.ui.home.dataModel.Data_Taste
import com.dish.seekdish.ui.home.viewModel.TasteFragVM
import com.dish.seekdish.ui.home.view.ITasteView
import com.dish.seekdish.util.SessionManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.location.*
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_taste.*
import kotlinx.android.synthetic.main.fragment_taste.view.edtSearch
import retrofit2.Response
import java.util.ArrayList


class TasteFragment : BaseFragment(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {


    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null

    var currentLatitude: Double? = 0.0
    var currentLongitude: Double? = 0.0

    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    internal lateinit var mLocationCallback: LocationCallback

    var updateLatLong = false

    //Define a request code to send to Google Play services
    private val CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000

    //location dilaog
    internal var showDialog = true
    internal var PERMISSION_REQUEST_CODE = 1


    private var recyclerView: RecyclerView? = null
    private var adapter: TasteFragAdapter? = null
    internal lateinit var layoutManager: LinearLayoutManager

    var tasteFragVM: TasteFragVM? = null


    private lateinit var homeActivity: HomeActivity
    internal var arrayList = ArrayList<Data_Taste>()


    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    var pageNumber: Int = 1
    var flagSearch: Boolean = false

    private var TAG = "TasteFragment"

    var alertShown: Boolean = false


    // lateinit var context  : Context
//     var sessionManager: SessionManager? = null;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_taste, container, false)


        homeActivity = activity as HomeActivity
        tasteFragVM = ViewModelProviders.of(this).get(TasteFragVM::class.java)

        // hiding keyboard
        hideKeyBoard()


        //check connection
        if (homeActivity.connectionDetector.isConnectingToInternet) {

            //hitting api
            getTasteMeals(pageNumber)

        } else {
            showSnackBar(getString(R.string.check_connection))
        }

        //observer
        getTasteResponseObserver()
        getLiveLocationObserver()

        //location
        startLocationUpdates()

        recyclerView = view.findViewById(R.id.rvTasteFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        // these adapter is allocated memory and set on beacuse as so that focus of recyler does not go on top again
        adapter = TasteFragAdapter(conxt, arrayList, homeActivity)
        recyclerView!!.adapter = adapter


        recyclerView!!.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }


            override fun loadMoreItems() {
                Log.e("loadMoreItems ", "hitted")


                if (flagSearch == false) {

                    Log.e("loadMoreItems", "entered   " + "isLastPage staus is " + isLastPage)

//                    progressBar.setVisibility(View.VISIBLE)
                    isLoading = true
                    if (!isLastPage) {

                        Log.e("loadMoreItems", "entered inide lastpage scope")

                        Handler().postDelayed({

                            //hitting api
                            getTasteMeals(pageNumber)
                        }, 200)
                    }
                }
            }
        })

        view.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                // search like ingredient
                var serchedText = edtSearch.text.toString();

                if (serchedText != null && serchedText != "null" && serchedText != "") {
                    flagSearch = true
                    Log.e("textWatcher", "entered if scope")
//                    getSearchedIngre(serchedText)
                } else {

                    Log.e("textWatcher", "entered else scope")

                    flagSearch = false
                    pageNumber = 1
                    getTasteMeals(pageNumber)

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        /* for (i in 0..6) {
             val tasteData = TasteFragDataClass("Manager", "Brochette De Boeuf", "1.85 Km", "3", "14", "4");
             arrayList.add(tasteData)
         }

         adapter = TasteFragAdapter(arrayList, homeActivity)
         recyclerView!!.setAdapter(adapter)*/


        return view
    }

/*    override fun onGetLocation(result: Boolean, response: Response<com.dish.seekdish.ui.home.dataModel.Location>) {


        if (result == true) {
            val locationModel = response.body() as com.dish.seekdish.ui.home.dataModel.Location

            sessionManager?.setValues(SessionManager.LATITUDE, locationModel.data.latitude)
            sessionManager?.setValues(SessionManager.LONGITUDE, locationModel.data.longitude)

            Log.e("sessionlat", " " + sessionManager.getValue(SessionManager.LATITUDE))
            Log.e("sessionlongitude", " " + sessionManager.getValue(SessionManager.LONGITUDE))

        } else {
//            showSnackBar(homeActivity.resources.getString(R.string.error_occured));
        }


    }*/


    // Trigger new location updates at interval
    protected fun startLocationUpdates() {

        Log.e("Loc", "method" + " startLocationUpdates() called")


        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                // do work here
                onLocationChanged(locationResult?.lastLocation)
            }
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(homeActivity)

        mGoogleApiClient = GoogleApiClient.Builder(homeActivity)
            // The next two lines tell the new client that “this” current class will handle connection stuff
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            //fourth line adds the LocationServices API endpoint from GooglePlayServices
            .addApi(LocationServices.API)
            .build()

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = (10 * 1000).toLong()        // 10 seconds, in milliseconds
        mLocationRequest?.fastestInterval = (2 * 1000).toLong() // 1 second, in milliseconds

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        val settingsClient = LocationServices.getSettingsClient(homeActivity)
        settingsClient.checkLocationSettings(locationSettingsRequest)


        if (!checkLocation()) {
            showAlert()
        }

    }


    private fun checkLocation(): Boolean {
        val lm = homeActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        return !(!gps_enabled && !network_enabled)

        /*  return if (!gps_enabled && !network_enabled) {

              false
          } else {
              true

          }*/
    }

    private fun showAlert() {
        // notify user
        val dialog = AlertDialog.Builder(homeActivity)
        dialog.setMessage("Location services need to be enabled ")
        dialog.setPositiveButton(
            "Go to Location Settings"
        ) { paramDialogInterface, paramInt ->
            // TODO Auto-generated method stub
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
            //get gps
        }
        dialog.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {

            override fun onClick(paramDialogInterface: DialogInterface, paramInt: Int) {
                // TODO Auto-generated method stub

            }
        })
        dialog.show()

    }


    override fun onConnected(p0: Bundle?) {

        Log.e(TAG, "onConnected called")
        tryLocation()
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(homeActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST)
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (e: IntentSender.SendIntentException) {
                // Log the error
                Log.e("errorMessage", "" + e.printStackTrace())
                e.printStackTrace()
            }

        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            // Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    override fun onLocationChanged(location: Location?) {

        currentLatitude = location?.latitude
        currentLongitude = location?.longitude



        Log.e("onLoc lat", currentLatitude.toString())
        Log.e("onLoc long", currentLongitude.toString())


        if (updateLatLong == false) {
            //check connection
            if (homeActivity.connectionDetector.isConnectingToInternet) {

                if (sessionManager.getValue(SessionManager.LATITUDE_SELECTED) == "" && sessionManager.getValue(
                        SessionManager.LONGITUDE_SELECTED
                    ) == ""
                ) {
                    // hit lat long api here  WITH THE DEFAULT COORDITAE
                    tasteFragVM?.getCurrentLocation(
                        sessionManager.getValue(SessionManager.USER_ID),
                        currentLongitude.toString(),
                        currentLatitude.toString()
                    )
                } else {
                    // hit lat long api here WITH THE COORDITAE SELECTED BY USER
                    tasteFragVM?.getCurrentLocation(
                        sessionManager.getValue(SessionManager.USER_ID),
                        sessionManager.getValue(SessionManager.LONGITUDE_SELECTED),
                        sessionManager.getValue(SessionManager.LATITUDE_SELECTED)
                    )

                    Log.e(
                        "selectCordTaste",
                        " " + "longi: " + sessionManager.getValue(SessionManager.LONGITUDE_SELECTED) + "lati:" + sessionManager.getValue(
                            SessionManager.LATITUDE_SELECTED
                        )
                    )
                }
            } else {
                showSnackBar(getString(R.string.check_connection))
            }
        }

        //SAVE TO LOCAL
        if (currentLatitude != 0.0 && currentLongitude != 0.0) {
            sessionManager.setValues(SessionManager.LATITUDE, currentLatitude.toString())
            sessionManager.setValues(SessionManager.LONGITUDE, currentLongitude.toString())

            // this will always have current live postition of the user
            sessionManager.setValues(SessionManager.CURRENT_LATITUDE, currentLatitude.toString())
            sessionManager.setValues(SessionManager.CURRENT_LONGITUDE, currentLongitude.toString())


        }
    }


    fun tryLocation() {
        try {


            val currentapiVersion = android.os.Build.VERSION.SDK_INT
            if (currentapiVersion >= 23) {
                if (!permissionGranted()) {

                    // --------------if permission is not granted check if user has permanetly flagged not to show----------------
                    //------------disabled completely--------------
                    if (sessionManager.getValue(SessionManager.LOCATION_STATUS) != null && sessionManager.getValue(
                            SessionManager.LOCATION_STATUS
                        ).equals("0")
                    ) {
                        // no need to ask for permission as permanently disable
                        //  Log.e("connection", "heree");

                        //----------------show dialog once when activity opens------------------------
                        if (showDialog) {
                            AlertDialog.Builder(homeActivity)
                                .setTitle("Permission Required")
                                .setMessage("Some of the functionalities may not work with location being disabled")
                                .setPositiveButton("Enable from Settings") { dialog, which ->
                                    // User pressed YES button. Write Logic Here
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", homeActivity.getPackageName(), null)
                                    intent.data = uri
                                    startActivityForResult(intent, PERMISSION_REQUEST_CODE)

                                    //----------------IMPORTANT--------------
                                    showDialog = false
                                    dialog.dismiss()
                                }
                                .setNegativeButton("Discard") { dialog, which ->
                                    // User pressed YES button. Write Logic Here

                                    //----------------IMPORTANT--------------
                                    showDialog = false
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    } else {

                        requestPermission()
                    }


                } else {

                    getLocation()

                }
            } else {

                getLocation()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun permissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            homeActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            homeActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            homeActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
        // }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (hasAllPermissionsGranted(grantResults)) {

            sessionManager.setValues(SessionManager.LOCATION_STATUS, "1")
            getLocation()

        } else {
            //if both are true
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                homeActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                homeActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (!showRationale) {
                // user denied flagging NEVER ASK AGAIN
                // you can either enable some fall back,
                // disable features of your app
                // or open another dialog explaining
                // again the permission and directing to
                // the app setting
                showSnackBar("Permission denied, so change from phone settings and Retry !")
                //Toast.makeText(HomeActivity.this, "Permission completely denied, so change from settings and Retry !", Toast.LENGTH_SHORT).show();
                sessionManager.setValues(SessionManager.LOCATION_STATUS, "0")


            } else {
                // permissionImportanceAlert();
            }

        }
    }

    fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    fun getLocation() {


        try {

            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
            )


        } catch (e: SecurityException) {
            Log.e("errorMess", " " + e.printStackTrace())
            e.printStackTrace()
        }


    }

    override fun onResume() {
        super.onResume()

        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            mGoogleApiClient!!.connect()

        } else {
            startLocationUpdates()
        }

    }


    fun getTasteResponseObserver() {

        Log.e("loadMoreItems", "entered getLikedResponseObserver ")

        //observe
        tasteFragVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        tasteFragVM!!.getTasteLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspgetLiked", response.toString())

                Log.e("rspgetLikedStat", response.status.toString())

                if (response.status == 1) {

                    var arrySize = arrayList.size

                    if (response.data.isEmpty() && alertShown == false) {
                        tvItemsAlert.visibility = View.VISIBLE
                    } else {

                        // this does not make 2 copies of item in recyclerview...
                        if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                            adapter?.getItemCount()?.minus(1)
                        ) {
                            // loading new items...
                            resultAction(response.data)

                            alertShown = true

                        }
                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }


    fun getLiveLocationObserver() {


        /*    //observe
            tasteFragVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
                setIsLoading(it)
            }
    */
        tasteFragVM!!.getLiveLocation.observe(this, Observer { response ->
            if (response != null) {

                Log.e("respLocationString", " " + response.toString())

                if (response.status == 1) {

                    sessionManager.setValues(SessionManager.LATITUDE, response.data.latitude)
                    sessionManager.setValues(SessionManager.LONGITUDE, response.data.longitude)

                    Log.e("sessionlat", " " + sessionManager.getValue(SessionManager.LATITUDE))
                    Log.e("sessionlongitude", " " + sessionManager.getValue(SessionManager.LONGITUDE))


                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }

    fun resultAction(data: ArrayList<Data_Taste>) {
        Log.e("data came", "" + data.toString())

//        progressBar.setVisibility(View.INVISIBLE)
        isLoading = false
        if (data != null) {
            adapter?.addItems(data)

            Log.e("data to bind", "" + data.toString())
            if (data.size == 0) {
                isLastPage = true
            } else {
                /* var pos:Int= adapter?.itemCount?.minus(2)!!
                 recyclerView?.scrollToPosition(pos)*/
                pageNumber = pageNumber + 1

                Log.e("pgNumber", "" + pageNumber)
            }
        }
    }


    private fun getTasteMeals(page: Int) {

        Log.e("loadMoreItems", "entered getLikedIngre ")
        var radius: String = sessionManager.getValue(SessionManager.RADIUS)
        if (radius != null && radius != "null" && radius != "") {
            radius = sessionManager.getValue(SessionManager.RADIUS)
        } else {
            radius = "15"
        }

        // hitting api
        tasteFragVM?.doGetTasteMealData(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            page.toString(),
            sessionManager.getValue(SessionManager.LONGITUDE),
            sessionManager.getValue(SessionManager.LATITUDE), radius
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        if(mFusedLocationClient!=null)
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

}
package com.dish.seekdish.ui.navDrawer.settings.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dish.seekdish.BuildConfig
import com.dish.seekdish.Constants
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SendUserGeneralSetting
import com.dish.seekdish.ui.navDrawer.settings.presenter.RadiusPresenter
import com.dish.seekdish.ui.navDrawer.settings.view.IRadiusView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_radius_center.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class RadiusCenterActivity : BaseActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, IRadiusView {


    lateinit var marker: Marker

    private var mMap: GoogleMap? = null

    val PERMISSION_REQUEST_LOCATION_CODE = 1

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    internal lateinit var apiInterface: APIInterface

    var selectedLat: String? = ""
    var selectedLong: String? = ""
    var selectedAddress: String? = ""
    var currentAddress: String? = ""

    var sessionManager: SessionManager? = null

    var addressFrom = 0   // 0 - for initialize, 1 - selected address,  2 - current address

    lateinit var geocoder: Geocoder;
    var addresses = ArrayList<Address>()
    lateinit var radiusPresenter: RadiusPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radius_center)

        sessionManager = SessionManager(this)
        geocoder = Geocoder(this, Locale.getDefault());
        radiusPresenter = RadiusPresenter(this, this)


        //main map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // new SDK code after deprecation
        setMapAutoComplete()

        tvBack.setOnClickListener()
        {
            finish()
        }

        tvSave.setOnClickListener()
        {
            if (!selectedAddress.equals("") && !selectedLat.equals("") && !selectedLong.equals("")) {

                /*sessionManager?.setValues(SessionManager.LATITUDE_SELECTED, selectedLat);
                sessionManager?.setValues(SessionManager.LONGITUDE_SELECTED, selectedLong);*/

                sessionManager?.setValues(SessionManager.LATITUDE, selectedLat);
                sessionManager?.setValues(SessionManager.LONGITUDE, selectedLong)
                sessionManager?.setValues(SessionManager.PLACE_SELECTED, selectedAddress)
//                Log.e("addressSELECTED", "$selectedLat ,  $selectedLong " + selectedAddress)


                // this way the user can selected a particular location
//                sessionManager?.setValues(SessionManager.LOCATION_SELECTED, "1");

                sessionManager?.setValues(
                    SessionManager.IS_CURRENT_LOCATION_SELECTED, Constants.FALSE_BOOLEAN
                );

                updateCordsOnServer(
                    sessionManager?.getValue(SessionManager.USER_ID).toString(),
                    selectedLong.toString(),
                    selectedLat.toString()
                )

                //saving the new location address to the server
                if (connectionDetector.isConnectingToInternet) {

                    radiusPresenter.setGeneralSettingInfo(
                        sessionManager?.getValue(SessionManager.USER_ID).toString(),
                        sessionManager?.getValue(SessionManager.IS_GEOLOCATION).toString(),
                        sessionManager?.getValue(SessionManager.IS_NOTIFICATION).toString(),
                        sessionManager?.getValue(SessionManager.IS_PRIVATE).toString(),
                        sessionManager?.getValue(SessionManager.RADIUS).toString(),
                        sessionManager?.getValue(SessionManager.PLACE_SELECTED).toString(),
                        sessionManager?.getValue(SessionManager.IS_CURRENT_LOCATION_SELECTED)
                            .toString()
                    )

                } else {
                    showSnackBar(getString(R.string.check_connection))
                }

                /*  val returnIntent = Intent()
                  setResult(Activity.RESULT_CANCELED, returnIntent)
                  finish()*/
            } else if (!currentAddress.equals("")) {

                sessionManager?.setValues(
                    SessionManager.IS_CURRENT_LOCATION_SELECTED, Constants.TRUE_BOOLEAN
                );

                /*   sessionManager?.setValues(
                       SessionManager.LATITUDE_SELECTED,
                       sessionManager?.getValue(SessionManager.LATITUDE)
                   );
                   sessionManager?.setValues(
                       SessionManager.LONGITUDE_SELECTED,
                       sessionManager?.getValue(SessionManager.LONGITUDE)
                   );*/

                // this way the user can selected current location and it will get updated if user moves with the app
//                sessionManager?.setValues(SessionManager.LOCATION_SELECTED, "0");

                sessionManager?.setValues(
                    SessionManager.LATITUDE,
                    sessionManager?.getValue(SessionManager.CURRENT_LATITUDE)
                );
                sessionManager?.setValues(
                    SessionManager.LONGITUDE,
                    sessionManager?.getValue(SessionManager.CURRENT_LONGITUDE)
                );
                sessionManager?.setValues(SessionManager.PLACE_SELECTED, currentAddress)

//                Log.e("addressCureent", " " + currentAddress)

                // updating coords over the db
                updateCordsOnServer(
                    sessionManager?.getValue(SessionManager.USER_ID).toString(),
                    sessionManager?.getValue(SessionManager.CURRENT_LONGITUDE).toString(),
                    sessionManager?.getValue(SessionManager.CURRENT_LATITUDE).toString()
                )


                //saving the new location address  empty according to new requirement if location is choosen the current one, to the server
                if (connectionDetector.isConnectingToInternet) {
                    radiusPresenter.setGeneralSettingInfo(
                        sessionManager?.getValue(SessionManager.USER_ID).toString(),
                        sessionManager?.getValue(SessionManager.IS_GEOLOCATION).toString(),
                        sessionManager?.getValue(SessionManager.IS_NOTIFICATION).toString(),
                        sessionManager?.getValue(SessionManager.IS_PRIVATE).toString(),
                        sessionManager?.getValue(SessionManager.RADIUS).toString(),
                        "",
                        sessionManager?.getValue(SessionManager.IS_CURRENT_LOCATION_SELECTED)
                            .toString()
                    )

                } else {
                    showSnackBar(getString(R.string.check_connection))
                }

/*                val intent = Intent()
                intent.putExtra("ADDRESS", currentAddress)
                setResult(RESULT_OK, intent);
                finish()*/
            }

            if (tvAddAddress.text.equals("") && tvCurrentAddress.text.equals("")) {
                showSnackBar("Please select any address first.")
            }

            Log.e("coords33","latit:  ${sessionManager?.getValue(SessionManager.LATITUDE)}  --   longitu:  ${sessionManager?.getValue(SessionManager.LONGITUDE)}")
        }

        linCurrentLocation.setOnClickListener()
        {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                linAddress.visibility = View.GONE

                var lattemp = sessionManager?.getValue(SessionManager.CURRENT_LATITUDE)
                var longtemp = sessionManager?.getValue(SessionManager.CURRENT_LONGITUDE)

                if (lattemp != null && longtemp != null && !lattemp.equals("") && !longtemp.equals("")) {

                    var lati: Double =
                        lattemp.toDouble()
                    var longi: Double =
                        sessionManager!!.getValue(SessionManager.CURRENT_LONGITUDE).toDouble()

                    // saing the current cordinate to main session shared prefrences
                    sessionManager?.setValues(SessionManager.LATITUDE, lati.toString())
                    sessionManager?.setValues(SessionManager.LONGITUDE, longi.toString())

                    addresses = geocoder.getFromLocation(
                        lati,
                        longi,
                        1
                    ) as ArrayList<Address>; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    var address = addresses.get(0)
                        .getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    // storing the address through current coordinates
                    currentAddress = address
                    tvCurrentAddress.setText(currentAddress)
                }
            } else {
                requestImagePermission()
            }

        }

        linAddress.setOnClickListener()
        {

//            linCurrentLocation.visibility = View.INVISIBLE

            // Set the fields to specify which types of place data to return.
            val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (!sessionManager?.getValue(SessionManager.LATITUDE)
                .equals("") && !sessionManager?.getValue(SessionManager.LONGITUDE).equals(
                ""
            )
        ) {
            // Add a marker in PARIS, FRANCE, and move the camera.
            var lati = sessionManager?.getValue(SessionManager.LATITUDE)?.toDouble()
            var long = sessionManager?.getValue(SessionManager.LONGITUDE)?.toDouble()


            val markerOnPlace = LatLng(lati!!, long!!)
            mMap!!.addMarker(MarkerOptions().position(markerOnPlace).title("My Location"))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOnPlace, 12F));


        } else {
            val markerOnPlace = LatLng(43.3442, 3.2158)
            mMap!!.addMarker(MarkerOptions().position(markerOnPlace).title(""))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(markerOnPlace))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOnPlace, 12F));

        }




        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap?.isMyLocationEnabled = true

            // disabled the location button
            mMap?.getUiSettings()?.setMyLocationButtonEnabled(false);
            mMap!!.setMyLocationEnabled(true);

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                val latLngOfPlace = place.latLng
                if (latLngOfPlace != null) {
                    /* Log.e("GooglePlacelat", "Place found: " + latLngOfPlace.latitude)
                     Log.e("GooglePlacelong", "Place found: " + latLngOfPlace.longitude)
                     Log.e("GooglePlaceaddress", "Place found: " + place.name)*/


                    /*    // savin the selected in session
                        sessionManager.setValues(SessionManager.LATITUDE_SELECTED, latLngOfPlace.latitude.toString());
                        sessionManager.setValues(SessionManager.LONGITUDE_SELECTED, latLngOfPlace.longitude.toString());
                    sessionManager.setValues(SessionManager.PLACE_SELECTED,place.name );*/

                    linCurrentLocation.visibility = View.GONE

                    selectedAddress = place.name
                    selectedLat = latLngOfPlace.latitude.toString()
                    selectedLong = latLngOfPlace.longitude.toString()

                    moveCameraMarker(latLngOfPlace.latitude, latLngOfPlace.longitude)

                    // setting name to text
                    tvAddAddress.setText(place.name)

                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, 12.0f))
                }
                Log.e(
                    "GooglePlaceSDKonAct",
                    "Place Name: " + place.name + "      Place id:" + place.id + "    Place lat:" + place.latLng
                )
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e("GooglePlaceSDKonAct", status.statusMessage.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    fun moveCameraMarker(latitude: Double, longitude: Double) {
        val markerOnPlace = LatLng(latitude, longitude)
        mMap!!.addMarker(MarkerOptions().position(markerOnPlace).title(""))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(markerOnPlace))
    }


    fun setMapAutoComplete() {
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api))
        }
    }

    fun updateCordsOnServer(
        userId: String,
        longitude: String,
        latitude: String
    ) {
        ProgressBarClass.progressBarCalling(this)
        Log.e("coords_resp_radius", "${userId}, long: ${longitude}   lat: ${latitude}")

        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val appVersion: String = BuildConfig.VERSION_NAME
        val osVersion = Build.VERSION.SDK_INT
        val osDevice = Constants.osDevice
        val call = apiInterface.getLocation(
            userId,
            longitude,
            latitude,
            appVersion,
            osVersion.toString(),
            osDevice
        )
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(call: Call<CancelReModel>, response: Response<CancelReModel>) {
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {
                    var model = response.body() as CancelReModel
                    if (model.status == 1) {
                        showSnackBar(model.message)

                    } else {
                        showSnackBar(model.message)
                    }
                } else {
                    showSnackBar(getResources().getString(R.string.error_occured));
                }
            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {
                ProgressBarClass.dialog.dismiss()
                showSnackBar(resources.getString(R.string.error_occured) + "    ${t.message}");
                call.cancel()

            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasAllPermissionsGranted(grantResults)) {
//            getLocation()
        } else {
            //if both are true
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (!showRationale) {

                showSnackBar("Permission denied, so change from phone settings and Retry !")
//                sessionManager?.setValues(SessionManager.LOCATION_STATUS, "0")
                /* val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                 val uri: Uri = Uri.fromParts("package", packageName, null)
                 intent.data = uri
                 startActivity(intent)*/

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

    override fun onSetSettingInfo(result: Boolean, response: Response<SendUserGeneralSetting>) {
        if (result == true) {
            val sendUserGeneralSetting = response.body() as SendUserGeneralSetting
            if (sendUserGeneralSetting.status == 1) {
                showSnackBar(sendUserGeneralSetting.message);
                finish()
            } else {
                showSnackBar(sendUserGeneralSetting.message);
            }
        } else {
            showSnackBar(getResources().getString(R.string.error_occured) + "   ${response.code()}");
        }
    }

}

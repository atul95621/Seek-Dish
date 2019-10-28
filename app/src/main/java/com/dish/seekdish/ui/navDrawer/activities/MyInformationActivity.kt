package com.dish.seekdish.ui.navDrawer.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.CustomListAdapterDialog
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.navDrawer.activities.model.ProfileDataClass
import com.dish.seekdish.ui.navDrawer.activities.presenter.MyInfoPresenter
import com.dish.seekdish.ui.navDrawer.activities.view.IMyInformationView
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LangData
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LanguageData
import com.dish.seekdish.util.SessionManager
import com.facebook.FacebookSdk
import com.myhexaville.smartimagepicker.ImagePicker
import com.myhexaville.smartimagepicker.OnImagePickedListener
import kotlinx.android.synthetic.main.activity_my_information.*
import kotlinx.android.synthetic.main.activity_my_information.edtBio
import kotlinx.android.synthetic.main.activity_my_information.edtLastName
import kotlinx.android.synthetic.main.activity_my_information.edtUsername
import kotlinx.android.synthetic.main.activity_my_information.spinnerGender
import kotlinx.android.synthetic.main.activity_my_information.tvBack
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.ArrayList

class MyInformationActivity : BaseActivity(), IMyInformationView {

    lateinit var myInfoPresenter: MyInfoPresenter
    // lateinit var context  : Context
    var sessionManager: SessionManager? = null;


    val PERMISSION_REQUEST_IMG_CODE = 1
    var imagePicker: ImagePicker? = null
    internal var imagePick = false
    internal var bitmap: Bitmap? = null
    internal var genderArr = ArrayList<String>()
    // path for multipart image upload
    var path: String = ""
    var countryId = ""
    var cityId = ""

    var bio = ""
    var addressLineOne = ""
    var addressLineTwo = ""
    var zipCode = ""
    var weight = ""
    var height = ""
    var bodyFat = ""
    var gender=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_information)


        //make preseneter
        myInfoPresenter = MyInfoPresenter(this, this)
        sessionManager = SessionManager(this)


        hideKeyBoard()

        getProfileDetails()

        //populating the spinner
        setGenderSpinner()

        tvCountry.setOnClickListener()
        {
            myInfoPresenter.getCountriesData(sessionManager!!.getValue(SessionManager.USER_ID))
        }

        tvCity.setOnClickListener()
        {
            if (countryId.equals("") || countryId.equals(null)) {
                showSnackBar("Please select the country first.")
            } else {
                myInfoPresenter.getCitiesData(sessionManager!!.getValue(SessionManager.USER_ID), countryId)

            }
        }


        tvBack.setOnClickListener()
        {
            finish()
        }

        imgProfile.setOnClickListener()
        {
            if (checkImgPermissionIsEnabledOrNot()) {

                chooseImage()

            } else {

                requestImagePermission()
            }

        }


        // saving the data of user...
        tvSave.setOnClickListener()
        {

            if (TextUtils.isEmpty(edtAddressLineOne!!.text.toString().trim { it <= ' ' })) {
                addressLineOne = ""
            } else {
                addressLineOne = edtAddressLineOne.text.toString()
            }
            if (TextUtils.isEmpty(edtBio!!.text.toString().trim { it <= ' ' })) {
                bio = ""
            } else {
                bio = edtBio.text.toString()
            }
            if (TextUtils.isEmpty(edtAddressLinetwo!!.text.toString().trim { it <= ' ' })) {
                addressLineTwo = ""
            } else {
                addressLineTwo = edtAddressLinetwo.text.toString()
            }
            if (TextUtils.isEmpty(edtZipcode!!.text.toString().trim { it <= ' ' })) {
                zipCode = ""
            } else {
                zipCode = edtZipcode.text.toString()
            }

            if (TextUtils.isEmpty(edtBodyFat!!.text.toString().trim { it <= ' ' })) {
                bodyFat = ""
            } else {
                bodyFat = edtBodyFat.text.toString()
            }

            if (TextUtils.isEmpty(edtHeight!!.text.toString().trim { it <= ' ' })) {
                height = ""
            } else {
                height = edtHeight.text.toString()
            }
            if (TextUtils.isEmpty(edtWeight!!.text.toString().trim { it <= ' ' })) {
                weight = ""
            } else {
                weight = edtWeight.text.toString()
            }
            if (spinnerGender!!.selectedItem === "Select Gender") {
                gender = ""
            } else {
                gender = spinnerGender.selectedItem.toString()
            }

            if (TextUtils.isEmpty(edtName!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter First Name")
                edtName!!.requestFocus()
            } else if (TextUtils.isEmpty(edtLastName!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter Last Name")
                edtLastName!!.requestFocus()
            } else if (TextUtils.isEmpty(edtUsername!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter User Name")
                edtUsername!!.requestFocus()
            }
            /*else if (spinnerGender!!.selectedItem === "Select Gender") {
                showSnackBar("Enter Gender")
            } else if (TextUtils.isEmpty(edtAddressLineOne!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter Address Line 1")
                edtAddressLineOne!!.requestFocus()
            } else if (TextUtils.isEmpty(edtAddressLinetwo!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter Address Line 2")
                edtAddressLineOne!!.requestFocus()
            } else if (TextUtils.isEmpty(edtZipcode!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter Zip Code")
                edtZipcode!!.requestFocus()
            } else if (TextUtils.isEmpty(edtBodyFat!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter Body Fat")
                edtBodyFat!!.requestFocus()
            } else if (TextUtils.isEmpty(edtWeight!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter Weight")
                edtWeight!!.requestFocus()
            } else if (TextUtils.isEmpty(edtHeight!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter Height")
                edtHeight!!.requestFocus()
            } else if (TextUtils.isEmpty(edtBio!!.text.toString().trim { it <= ' ' })) {
                showSnackBar("Enter Bio")
                edtBio!!.requestFocus()
            } *//*else if (path.equals("") || path.equals(null)) {
                showSnackBar("Please select an image.")
            }*/ else {

                if (connectionDetector.isConnectingToInternet) {

                    var part: MultipartBody.Part? = null

                    // making path for image
                    val file = File(path)
                    Log.e("resppath", "" + path)
                    Log.e("respbitmap", "" + bitmap)
                    Log.e("respfile", "" + file)

                    // compressing size of the image uploading
                    var finalFile = compressFile(file)
                    if (finalFile != null) {
                        var sizeAfter = finalFile.length().div(1024)
                        var sizeBefore = file.length().div(1024)
                        Log.e("sizeAfter", "" + sizeAfter)
                        Log.e("sizeBefore", "" + sizeBefore)
                    } else {
                        // if finalFile return null then
                        finalFile = file
                    }

                    // Create a request body with file and image media type
                    val fileReqBody = RequestBody.create(MediaType.parse("image/*"), finalFile)
                    // Create MultipartBody.Part using file request-body,file name and part name
                    //photo is the KEY that is to be sent over server
                    part = MultipartBody.Part.createFormData("photo", file.getName(), fileReqBody)

                    if (path.equals("") || path == null || path == "") {
                        Log.e("respUpdatePath", "" + path)
                        val fileReqBody = RequestBody.create(MediaType.parse("image/*"), "")

                        part = MultipartBody.Part.createFormData("photo", "", fileReqBody)
                    }

                    Log.e("respUpdatePart", "" + part)

                    Log.e(
                        "updateInfoSent",
                        " " + edtName.text.toString() + edtLastName.text.toString() + edtUsername.text.toString() +
                                edtAddressLineOne.text.toString() + edtAddressLinetwo.text.toString() + spinnerGender.selectedItem.toString()
                                + edtBio.text.toString() + " citiid  " + cityId.toString() + "country id  " + countryId.toString() + "      " + edtZipcode.text.toString() +
                                edtBodyFat.text.toString() + edtWeight.text.toString() + edtHeight.text.toString() + sessionManager!!.getValue(
                            SessionManager.USER_ID
                        ) + part.toString()
                    )

                    //calling api
                    myInfoPresenter.updateInfo(
                        stringConvertToRequestBody(edtName.text.toString()),
                        stringConvertToRequestBody(edtLastName.text.toString()),
                        stringConvertToRequestBody(edtUsername.text.toString()),
                        stringConvertToRequestBody(edtAddressLineOne.text.toString()),
                        stringConvertToRequestBody(edtAddressLinetwo.text.toString()),
                        stringConvertToRequestBody(spinnerGender.selectedItem.toString()),
                        stringConvertToRequestBody(edtBio.text.toString()),
                        stringConvertToRequestBody(countryId),
                        stringConvertToRequestBody(cityId),
                        stringConvertToRequestBody(edtZipcode.text.toString()),
                        stringConvertToRequestBody(edtBodyFat.text.toString()),
                        stringConvertToRequestBody(edtWeight.text.toString()),
                        stringConvertToRequestBody(edtHeight.text.toString()),
                        stringConvertToRequestBody(sessionManager!!.getValue(SessionManager.USER_ID)),
                        part
                    )
                } else {
                    showSnackBar(getResources().getString(R.string.check_connection));
                }


            }
        }

    }

    private fun getProfileDetails() {

        myInfoPresenter.getProfileInfo(sessionManager!!.getValue(SessionManager.USER_ID))

    }

    //images
    private fun checkImgPermissionIsEnabledOrNot(): Boolean {

        val FirstPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val SecondPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val ThirdPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED


    }

    fun chooseImage() {
        imagePicker = ImagePicker(this /*activity non null*/, null,
            object : OnImagePickedListener {
                override fun onImagePicked(imageUri: Uri) {

                    Log.e("case b", "onactvty")

                    //set flag to true
                    imagePick = true


                    //on image picked
                    //Log.e("uri imagePckr fetch", String.valueOf(imageUri));

                    path = this@MyInformationActivity.getPath(imageUri).toString()
                    Log.e("path", path)

                    //get image path from uri
//                    val path = getPath(imageUri)
                    // Bitmap bmp = uriToBitmap(imageUri);

                    if (path != null && path != "" && path != "null") {

                        //get bitmap from file path
                        val bm = decodeSampledBitmapFromFile(path, 300, 300)

                        try {

                            //rotate bitmap to portrait if bitmap is orientated landscape
                            val ei = ExifInterface(path)
                            val orientation = ei.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED
                            )

                            when (orientation) {

                                ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = TransformationUtils.rotateImage(bm, 90)

                                ExifInterface.ORIENTATION_ROTATE_180 -> bitmap =
                                    TransformationUtils.rotateImage(bm, 180)

                                ExifInterface.ORIENTATION_ROTATE_270 -> bitmap =
                                    TransformationUtils.rotateImage(bm, 270)

                                ExifInterface.ORIENTATION_NORMAL -> bitmap = bm
                                else -> bitmap = bm
                            }


// API TO BE STRIKE HERE FOR SUBMIT
                            //                            profileFragmentPresenter.uploadProfilePic(rotatedBitmap);
                            imgProfile.setImageBitmap(bitmap)


                            //Log.e("updateProfile", String.valueOf(rotatedBitmap));


                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    } else {

                        val options = BitmapFactory.Options()

                        // downsizing image as it throws OutOfMemory Exception for larger
                        // images
                        options.inSampleSize = 8

                        bitmap = BitmapFactory.decodeFile(
                            imageUri.path,
                            options
                        )

                        path = imagePicker?.imageFile?.absoluteFile.toString()
//                        path = getImageUri(this@AnswerActivity, bitmap).toString()
                        Log.e("path by camera", path)

                        if (path != null && path != "" && path != "null") {
                            imgProfile.setImageBitmap(bitmap)
                        }
                    }


                }
            })

        imagePicker?.choosePicture(true)
    }

    //images
    private fun requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), PERMISSION_REQUEST_IMG_CODE
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {

        super.onActivityResult(requestCode, resultCode, result)

        //Log.e("test", "onactivitycalled");

        if (resultCode == RESULT_OK) {

            Log.e("case a", "onactvty")

            //Log.e("uri image", "" + resultCode);
            if (imagePicker != null)
                imagePicker?.handleActivityResult(resultCode, requestCode, result)

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {


            PERMISSION_REQUEST_IMG_CODE -> {

                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val camera = grantResults[2] == PackageManager.PERMISSION_GRANTED

                if (write && read && camera) {
                    chooseImage()
                }
            }

            else ->

                imagePicker?.handlePermission(requestCode, grantResults)
        }
    }


    private fun setGenderSpinner() {
        //        ___________________________________________________________BUSINESS TYPE SPINNER
        //clear list
        genderArr.clear()

        //add states to list
        genderArr.add("Select Gender")
        genderArr.add("M")
        genderArr.add("F")


        // Creating adapter for spinner
        val languageSelectAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderArr)

        // Drop down layout style - list view with radio button
        languageSelectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinnerGender!!.adapter = languageSelectAdapter

    }

    fun showDialog(listData: ArrayList<LangData>) {

        val dialog = Dialog(this);

        val view: View = getLayoutInflater().inflate(R.layout.dialog_language, null);


        // Change MyActivity.this and myListOfItems to your own values
        val clad: CustomListAdapterDialog = CustomListAdapterDialog(this, listData);

        val list = view.findViewById<View>(R.id.custom_list) as ListView


        list.setAdapter(clad);

        list.setOnItemClickListener(AdapterView.OnItemClickListener { adapter, view, position, arg ->
            // TODO Auto-generated method stub
//            val tvLanguage = view.findViewById<View>(R.id.tvLanguage) as TextView

            Toast.makeText(
                FacebookSdk.getApplicationContext(),
                "selected Item Name is " + listData[position].id + "     " + listData[position].name,
                Toast.LENGTH_LONG
            ).show()

            countryId = listData[position].id.toString()
            var countryName = listData[position].name

            tvCountry.setText(countryName)
            // making it null if user change the country again...
            cityId = ""
            tvCity.setText("")

            dialog.dismiss()
        }
        )

        dialog.setContentView(view);

        dialog.show();

    }


    fun showCitiesDialog(listData: ArrayList<LangData>) {

        val dialog = Dialog(this);

        val view: View = getLayoutInflater().inflate(R.layout.dialog_language, null);


        // Change MyActivity.this and myListOfItems to your own values
        val clad: CustomListAdapterDialog = CustomListAdapterDialog(this, listData);

        val list = view.findViewById<View>(R.id.custom_list) as ListView


        list.setAdapter(clad);

        list.setOnItemClickListener(AdapterView.OnItemClickListener { adapter, view, position, arg ->
            // TODO Auto-generated method stub
//            val tvLanguage = view.findViewById<View>(R.id.tvLanguage) as TextView

            Toast.makeText(
                FacebookSdk.getApplicationContext(),
                "selected Item Name is " + listData[position].id + "     " + listData[position].name,
                Toast.LENGTH_LONG
            ).show()

            cityId = listData[position].id.toString()
            val cityName = listData[position].name

            tvCity.setText(cityName)

            dialog.dismiss()
        }
        )

        dialog.setContentView(view);

        dialog.show();

    }

    override fun onSetDataChanged(result: Boolean, response: Response<ProfileDataClass>) {


        if (result == true) {
            val profileDataClass = response.body() as ProfileDataClass

            if (profileDataClass.status == 1) {

                Log.e("respUpdateName", profileDataClass.data.first_name)

                sessionManager?.setValues(SessionManager.USERNAME, profileDataClass.data.username)
                sessionManager?.setValues(SessionManager.FIRST_NAME, profileDataClass.data.first_name)
                sessionManager?.setValues(SessionManager.LAST_NAME, profileDataClass.data.last_name)
//                sessionManager?.setValues(SessionManager.PHONE, profileDataClass.data.phone)
                sessionManager?.setValues(SessionManager.PHOTO_URL, profileDataClass.data.photo)
//            sessionManager?.setValues(SessionManager.FCM_TOKEN, signUpModel.data.fcm_token)
                sessionManager?.setValues(SessionManager.GENDER, profileDataClass.data.gender)
                sessionManager?.setValues(SessionManager.BIO, profileDataClass.data.bio)

                countryId = profileDataClass.data.country_id.toString()
                cityId = profileDataClass.data.city_id

/*                     if (profileDataClass.data.photo != null && profileDataClass.data.photo != "") {
                         GlideApp.with(this)
                             .load(profileDataClass.data.photo)
                             .into(imgProfile)
                     }*/

                showSnackBar("Saved Successfully.")

            }

        } else {
            showSnackBar(this.getResources().getString(R.string.error_occured));

        }

    }


    override fun onGetCountryInfo(result: Boolean, response: Response<LanguageData>) {
        if (result == true) {
            val languageData = response.body() as LanguageData

            if (languageData.status == 1)

                showDialog(languageData.data)

        } else {
            showSnackBar(this.getResources().getString(R.string.error_occured));

        }
    }

    override fun onGetCitiesInfo(result: Boolean, response: Response<LanguageData>) {
        if (result == true) {
            val languageData = response.body() as LanguageData

            if (languageData.status == 1)

                showCitiesDialog(languageData.data)

        } else {
            showSnackBar(this.getResources().getString(R.string.error_occured));

        }
    }

    override fun onGetProfileDetailsData(result: Boolean, response: Response<ProfileDataClass>) {

        if (result == true) {
            val profileDataClass = response.body() as ProfileDataClass

            if (profileDataClass.status == 1) {
                edtName.setText(profileDataClass.data.first_name)
                edtLastName.setText(profileDataClass.data.last_name)
                edtUsername.setText(profileDataClass.data.username)
                edtAddressLineOne.setText(profileDataClass.data.address_line1)
                edtAddressLinetwo.setText(profileDataClass.data.address_line2)
                tvCountry.setText(profileDataClass.data.country)
                tvCity.setText(profileDataClass.data.city)
                edtZipcode.setText(profileDataClass.data.zip_code.toString())
                edtBodyFat.setText(profileDataClass.data.body_fat)
                edtWeight.setText(profileDataClass.data.weight)
                edtHeight.setText(profileDataClass.data.height)
                edtBio.setText(profileDataClass.data.bio)
                var genderValue: String = profileDataClass.data.gender

                if (genderValue.equals("M")) {
                    spinnerGender.setSelection(1)
                }
                if (genderValue.equals("F")) {
                    spinnerGender.setSelection(2)
                } else

                    if (profileDataClass.data.photo != null && profileDataClass.data.photo != "null" && profileDataClass.data.photo != "") {
                        GlideApp.with(this)
                            .load(profileDataClass.data.photo)
                            .placeholder(R.drawable.ic_user)
                            .into(imgProfile)
                    } else {
                        GlideApp.with(this)
                            .load(R.drawable.ic_user)
                            .into(imgProfile)
                    }

                countryId = profileDataClass.data.country_id
                cityId = profileDataClass.data.city_id

                Log.e("checkforId", " " + "COUNTRYiD: " + countryId + "   CitiId:  " + cityId)

            }

        } else {
            showSnackBar(this.getResources().getString(R.string.error_occured));

        }


    }

}


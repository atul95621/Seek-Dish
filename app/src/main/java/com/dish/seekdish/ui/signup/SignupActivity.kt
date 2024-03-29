package com.dish.seekdish.ui.signup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.dish.seekdish.Constants
import com.dish.seekdish.R
import com.dish.seekdish.ui.WebViewActivity
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import com.myhexaville.smartimagepicker.ImagePicker
import com.myhexaville.smartimagepicker.OnImagePickedListener
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.math.sign

class SignupActivity : BaseActivity(), ISignUpView {


    val PERMISSION_REQUEST_IMG_CODE = 1
    var imagePicker: ImagePicker? = null
    internal var imagePick = false
    internal var bitmap: Bitmap? = null

    lateinit var signupPresenter: SignupPresenter
    internal var genderArr = ArrayList<String>()
    lateinit var sessionManager: SessionManager;

    // optional values...
    var bio = ""
    var gender = ""
    var langId = ""

    // path for multipart image upload
    var path: String = ""
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //make preseneter
        signupPresenter = SignupPresenter(this, this@SignupActivity)
        sessionManager = SessionManager(this)

        //hide keyboard to open
        hideKeyBoard()

        //populating the spinner
        setGenderSpinner()
        langId = sessionManager.getValue(SessionManager.LANGUAGE_ID).toString()


// lisners::::

        ccp.setOnClickListener()
        {
            onCountryCodeSelect()
        }

        tvTerms.setOnClickListener()
        {
            val intent = Intent(this@SignupActivity, WebViewActivity::class.java)
            intent.putExtra("url", "https://seekdish.com/use-conditions.html")
            intent.putExtra("from", "SignupTerms")
            startActivity(intent)
        }

        tvPolicy.setOnClickListener()
        {
            val intent = Intent(this@SignupActivity, WebViewActivity::class.java)
            intent.putExtra("url", "https://seekdish.com/privacy.html")
            intent.putExtra("from", "SignupPolicy")
            startActivity(intent)
        }


        tvBack.setOnClickListener()
        {
            finish()
        }

        profile_image.setOnClickListener()
        {
            if (checkImgPermissionIsEnabledOrNot()) {

                chooseImage()

            } else {

                requestImagePermission()
            }

        }

        tvNext.setOnClickListener()
        {

            if (TextUtils.isEmpty(edtBio!!.text.toString().trim { it <= ' ' })) {
                bio = ""
            } else {
                bio = edtBio.text.toString()
            }

            if (spinnerGender!!.selectedItem == resources.getString(R.string.select_gender_optional)) {
                gender = ""
            } else {
                gender = spinnerGender.selectedItem.toString()
            }

            if (TextUtils.isEmpty(edtFirstName!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.fill_first))
                edtFirstName!!.requestFocus()
            } else if (TextUtils.isEmpty(edtLastName!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.fill_last))
                edtLastName!!.requestFocus()
            } else if (TextUtils.isEmpty(edtEmail!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.enter_email))
                edtEmail!!.requestFocus()
            } else if (emailValidator(edtEmail.text.toString()) == false) {
                showSnackBar(getString(R.string.valid_email))
            } else if (TextUtils.isEmpty(edtPhone!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.enter_phone))
                edtPhone!!.requestFocus()
            } else if (TextUtils.isEmpty(edtPassword!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.enter_passss))
                edtPassword!!.requestFocus()
            } else if (TextUtils.isEmpty(edtConfirmPasword!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.enter_cnfrm))
                edtConfirmPasword!!.requestFocus()
            } else if (edtPassword!!.text.toString().trim().length < 5) {
                showSnackBar(getString(R.string.number_length))
                edtPassword!!.requestFocus()
            } else if (edtPassword.text.toString()
                    .compareTo(edtConfirmPasword.text.toString()) != 0
            ) {
                showSnackBar(getString(R.string.pass_not_match))
                edtConfirmPasword!!.requestFocus()
            }
            /*else if (TextUtils.isEmpty(edtUsername!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.enter_user))
                edtUsername!!.requestFocus()
            }*/
            /*else if (path.equals("") || path.equals(null)) {
                showSnackBar("Please select an image.")
            } */ else {

                if (connectionDetector.isConnectingToInternet) {

                    Log.e("gender_value",""+gender)

                    var part: MultipartBody.Part? = null

                    // making path for image
                    val file = File(path)


                    // compressing size of the image uploading
//                    var finalFile = compressFile(file)
                    var finalFile = bitmap?.let { saveBitmap(it, path) }
                    if (finalFile != null) {
                        var sizeAfter = finalFile.length().div(1024)
                        var sizeBefore = file.length().div(1024)
                    } else {
                        // if finalFile return null then
                        finalFile = file
                    }

                    // Create a request body with file and image media type
                    val fileReqBody = RequestBody.create(Constants.mediaType.toMediaTypeOrNull(), finalFile)
                    // Create MultipartBody.Part using file request-body,file name and part name
                    //photo is the KEY that is to be sent over server
                    part = MultipartBody.Part.createFormData("photo", file.getName(), fileReqBody)
/*
                    Log.e(
                        "para", "email:" + edtEmail.text.toString() + "   pass:" +
                                edtPassword.text.toString() + "   confirmpasss:" +
                                edtConfirmPasword.text.toString() + "  firstname: " +
                                edtFirstName.text.toString() + "  lastname: " +
                                edtLastName.text.toString() + "   phone:" +
                                edtPhone.text.toString() + "  username :" +
                                edtUsername.text.toString() + "   gender:" +
                                gender + "   bio:" +
                                bio + "   countrycode:" +
                                ccp.getSelectedCountryCode() + "   fcm:" +
                                sessionManager.getValue(SessionManager.FCM_TOKEN)+"   langid:"+langId
                    )*/


                    if (path.equals("") || path == null || path == "") {
                        //calling api
                        signupPresenter.signUpWithoutImage(
                            stringConvertToRequestBody(edtEmail.text.toString()),
                            stringConvertToRequestBody(edtPassword.text.toString()),
                            stringConvertToRequestBody(edtConfirmPasword.text.toString()),
                            stringConvertToRequestBody(edtFirstName.text.toString()),
                            stringConvertToRequestBody(edtLastName.text.toString()),
                            stringConvertToRequestBody(edtPhone.text.toString()),
                            stringConvertToRequestBody(edtUsername.text.toString()),
                            stringConvertToRequestBody(gender),
                            stringConvertToRequestBody(bio),
                            stringConvertToRequestBody(ccp.getSelectedCountryCode()),
                            stringConvertToRequestBody(sessionManager!!.getValue(SessionManager.FCM_TOKEN)),
                            stringConvertToRequestBody(langId)
                        )

                        // for null image pram...
//                        // Create a request body with file and image media type
//                        val fileReqBody = RequestBody.create(MediaType.parse("image/*"), finalFile)
//                        // Create MultipartBody.Part using file request-body,file name and part name
//                        //photo is the KEY that is to be sent over server
//                        part = MultipartBody.Part.createFormData("photo", "", fileReqBody)

                    } else {
                        //calling api
                        signupPresenter.signUp(
                            stringConvertToRequestBody(edtEmail.text.toString()),
                            stringConvertToRequestBody(edtPassword.text.toString()),
                            stringConvertToRequestBody(edtConfirmPasword.text.toString()),
                            stringConvertToRequestBody(edtFirstName.text.toString()),
                            stringConvertToRequestBody(edtLastName.text.toString()),
                            stringConvertToRequestBody(edtPhone.text.toString()),
                            stringConvertToRequestBody(edtUsername.text.toString()),
                            stringConvertToRequestBody(gender),
                            stringConvertToRequestBody(edtBio.text.toString()),
                            stringConvertToRequestBody(ccp.getSelectedCountryCode()),
                            stringConvertToRequestBody(sessionManager!!.getValue(SessionManager.FCM_TOKEN)),
                            part,
                            stringConvertToRequestBody(langId)
                        )
                    }
                } else {
                    showSnackBar(getResources().getString(R.string.check_connection));
                }


            }
        }


    }

    //images
    private fun checkImgPermissionIsEnabledOrNot(): Boolean {

        val FirstPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val SecondPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val ThirdPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED


    }

    fun chooseImage() {
        imagePicker = ImagePicker(this /*activity non null*/, null,
            object : OnImagePickedListener {
                override fun onImagePicked(imageUri: Uri) {
                    //set flag to true
                    imagePick = true
                    //get image path from uri
//                    val path = getPath(imageUri)
                    path = getRealPathFromURI(imageUri).toString()
//                     Bitmap bmp = uriToBitmap(imageUri);
                    if (path.isNullOrEmpty() == false) {
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

                                ExifInterface.ORIENTATION_ROTATE_90 -> bitmap =
                                    TransformationUtils.rotateImage(bm, 90)

                                ExifInterface.ORIENTATION_ROTATE_180 -> bitmap =
                                    TransformationUtils.rotateImage(bm, 180)

                                ExifInterface.ORIENTATION_ROTATE_270 -> bitmap =
                                    TransformationUtils.rotateImage(bm, 270)

                                ExifInterface.ORIENTATION_NORMAL -> bitmap = bm
                                else -> bitmap = bm
                            }
                            // API TO BE STRIKE HERE FOR SUBMIT
                            // profileFragmentPresenter.uploadProfilePic(rotatedBitmap);
                            profile_image.setImageBitmap(bitmap)
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
                        if (path != null && path != "" && path != "null") {
                            // api hitting to upload the image
                            profile_image.setImageBitmap(bitmap)
                        }
                    }
                }
            })
            .setWithImageCrop(1, 1)
        imagePicker?.choosePicture(true)
    }
    /*fun chooseImage() {
        imagePicker = ImagePicker(this *//*activity non null*//*, null,
            object : OnImagePickedListener {
                override fun onImagePicked(imageUri: Uri) {

                    Log.e("case b", "onactvty")

                    //set flag to true
                    imagePick = true


                    //on image picked

                    //get image path from uri
//                    val path = getPath(imageUri)
                    path = this@SignupActivity.getRealPathFromURI(imageUri).toString()
                    Log.e("path", path)
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

                                ExifInterface.ORIENTATION_ROTATE_90 -> bitmap =
                                    TransformationUtils.rotateImage(bm, 90)

                                ExifInterface.ORIENTATION_ROTATE_180 -> bitmap =
                                    TransformationUtils.rotateImage(bm, 180)

                                ExifInterface.ORIENTATION_ROTATE_270 -> bitmap =
                                    TransformationUtils.rotateImage(bm, 270)

                                ExifInterface.ORIENTATION_NORMAL -> bitmap = bm
                                else -> bitmap = bm
                            }


// API TO BE STRIKE HERE FOR SUBMIT
                            //                            profileFragmentPresenter.uploadProfilePic(rotatedBitmap);
                            profile_image.setImageBitmap(bitmap)


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

                        Log.e("camera bitmap", bitmap.toString())
                        Log.e("tryFile", "" + imagePicker?.imageFile?.absoluteFile)


                        path = imagePicker?.imageFile?.absoluteFile.toString()
//                        path = getImageUri(this@AnswerActivity, bitmap).toString()
                        Log.e("path by camera", path)

                        if (path != null && path != "" && path != "null") {
                            // api hitting to upload the image
                            profile_image.setImageBitmap(bitmap)

                        }


                    }


                }
            })

        imagePicker?.choosePicture(true)
    }*/

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
            //Log.e("uri image", "" + resultCode);
            if (imagePicker != null)
                imagePicker?.handleActivityResult(resultCode, requestCode, result)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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

    fun onCountryCodeSelect() {

        val countryCode = ccp.selectedCountryCode
        val phone = edtPhone.getText().toString()

        if (TextUtils.isEmpty(edtPhone.getText().toString().trim({ it <= ' ' }))) {

            showSnackBar("Enter phone number")

        } else {
            /* if (isConnectingToInternet())
                 loginWithPhone(countryCode + phone)
             else
                 utilities.showSnackBar(getString(R.string.check_connect))*/
        }

    }

    private fun setGenderSpinner() {
        //        ___________________________________________________________BUSINESS TYPE SPINNER
        //clear list
        genderArr.clear()

        //add states to list
        genderArr.add(getString(R.string.select_gender_optional))
        genderArr.add("M")
        genderArr.add("F")


        // Creating adapter for spinner
        val languageSelectAdapter =
            ArrayAdapter(this, R.layout.spinner_item, genderArr)

        // Drop down layout style - list view with radio button
        languageSelectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinnerGender!!.adapter = languageSelectAdapter

        spinnerTextWatcher() // to turn the first "select" text to grey color and other to black
    }

    fun spinnerTextWatcher() {

        spinnerGender.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (spinnerGender.selectedItemPosition == 0) {
                    (spinnerGender.getSelectedView() as TextView).setTextColor(
                        getResources().getColor(
                            R.color.gray_hint
                        )
                    )

                } else {
                    (spinnerGender.getSelectedView() as TextView).setTextColor(
                        getResources().getColor(
                            R.color.black
                        )
                    )
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }


    override fun onSetSignedUp(result: Boolean, response: Response<SignUpModel>) {
        if (result == true) {

            val signUpModel = response.body() as SignUpModel
            if (signUpModel.status == 1) {
                // saving that user is already logged in
                sessionManager?.setValues(SessionManager.LOGGEDIN, "1")

                sessionManager.setValues(SessionManager.USERNAME, signUpModel.data.username)
                sessionManager.setValues(SessionManager.FIRST_NAME, signUpModel.data.first_name)
                sessionManager.setValues(SessionManager.LAST_NAME, signUpModel.data.last_name)
                sessionManager.setValues(SessionManager.EMAIL, signUpModel.data.email)
                sessionManager.setValues(SessionManager.PHONE, signUpModel.data.phone)
                sessionManager.setValues(SessionManager.USER_ID, signUpModel.data.id.toString())
                sessionManager.setValues(
                    SessionManager.COUNTRY_CODE,
                    signUpModel.data.country.toString()
                )
                sessionManager.setValues(SessionManager.PHOTO_URL, signUpModel.data.photo)
//            sessionManager?.setValues(SessionManager.FCM_TOKEN, signUpModel.data.fcm_token)
                sessionManager.setValues(SessionManager.GENDER, signUpModel.data.gender)
                sessionManager.setValues(SessionManager.BIO, signUpModel.data.bio)

                sessionManager.setValues(SessionManager.LOGGEDIN_THROUGH, "0")
                sessionManager.setValues(SessionManager.LOCATION_SAVED,  signUpModel.data.radius_center_location.toString())
                sessionManager.setValues(SessionManager.IS_CURRENT_LOCATION_SELECTED,  signUpModel.data.isCurrentLocation.toString())


                val intent = Intent(this@SignupActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                showSnackBar(signUpModel.message)
            }
        } else {
            showSnackBar(
                this@SignupActivity.getResources()
                    .getString(R.string.error_occured) + "  ${response.code()}"
            );
        }
    }
}

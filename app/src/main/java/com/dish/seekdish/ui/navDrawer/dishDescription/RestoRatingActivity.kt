package com.dish.seekdish.ui.navDrawer.dishDescription

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
import android.os.Handler
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.navDrawer.dishDescription.VM.RatingCommentVM
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.checkInRestro.CheckinRestroActivity
import com.dish.seekdish.util.SessionManager
import com.myhexaville.smartimagepicker.ImagePicker
import com.myhexaville.smartimagepicker.OnImagePickedListener
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_resto_rating.*
import kotlinx.android.synthetic.main.activity_resto_rating.tvNext
import kotlinx.android.synthetic.main.dialog_upload_img_comment.tvBack
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException


class RestoRatingActivity : BaseActivity() {

    val PERMISSION_REQUEST_IMG_CODE = 1
    internal lateinit var imagePicker: ImagePicker
    internal var imagePick = false
    internal var bitmap: Bitmap? = null
    var flag: Boolean = false
    var count: Int = 0
    var sessionManager: SessionManager? = null


    var imgOne: ImageView? = null
    var imgTwo: ImageView? = null

    var path = ""
    var taste: Float = 0.0f
    var presentation: Float = 0.0f
    var texture: Float = 0.0f
    var ordor: Float = 0.0f

    var pathImage1: String = ""
    var pathImage2: String = ""
    var isAnnony: String = ""
    var mealId: String = ""
    var restauId: String = ""
    var fromScreen=""

    var ratingCommentVM: RatingCommentVM? = null

    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resto_rating)

        sessionManager = SessionManager(this)
        ratingCommentVM = ViewModelProviders.of(this).get(RatingCommentVM::class.java)

        getIntents()

        getCommentPostedObserver()

        tvNext.setOnClickListener()
        {
            onUploadImages()
        }

        tvBack.setOnClickListener()
        {
            finish()
        }

    }

    private fun getIntents() {
        taste = intent.getFloatExtra("TASTE", 0F)
        presentation = intent.getFloatExtra("PRESENTATION", 0F)
        texture = intent.getFloatExtra("TEXTURE", 0F)
        ordor = intent.getFloatExtra("ODOR", 0F)
        mealId = intent.getStringExtra("MEAL_ID")
        restauId = intent.getStringExtra("RESTAURANT_ID")
        var bitM = intent.getStringExtra("IMAGE_Bitmap")

        fromScreen = intent.getStringExtra("FROM_SCREEN")


        GlideApp.with(this)
            .load(bitM)
            .into(img_cir_meal_image)

        GlideApp.with(this)
            .load(bitM)
            .into(imgMealImage)
    }


    fun onUploadImages() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_upload_img_comment)

        val edtComment = dialog.findViewById<EditText>(R.id.edtComment)
        val switchAnonyme = dialog.findViewById<Switch>(R.id.switchAnonyme)
        val tvConfirm = dialog.findViewById<TextView>(R.id.tvConfirm)
        val tvBack = dialog.findViewById<TextView>(R.id.tvBack)
        val imgCamera = dialog.findViewById<ImageView>(R.id.imgCamera)
        imgOne = dialog.findViewById<ImageView>(R.id.imgOne)
        imgTwo = dialog.findViewById<ImageView>(R.id.imgTwo)


        tvConfirm.setOnClickListener {

         /*   if(fromScreen.equals("Dish_Description_Activity")) {
                val intent = Intent(this@RestoRatingActivity, DishDescriptionActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            else if(fromScreen.equals("CheckinRestroActivity")) {
                val intent = Intent(this@RestoRatingActivity, DishDescriptionActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }*/
        }

        tvBack.setOnClickListener {
            path = ""
            pathImage1 = ""
            pathImage2 = ""
            count = 0
            dialog.dismiss()
        }

        imgCamera.setOnClickListener()
        {
            if (checkImgPermissionIsEnabledOrNot()) {

                chooseImage()

            } else {

                requestImagePermission()
            }

        }

        dialog.setOnDismissListener()
        {
            pathImage1 = ""
            pathImage2 = ""
            count = 0
            flag = false
            imgOne= null
            imgTwo=null
        }


        tvConfirm.setOnClickListener()
        {
            if (edtComment.text.isEmpty()) {
                showSnackBar("Please enter your comment first")
            } else {

                if (switchAnonyme.isChecked == true) {
                    isAnnony = 1.toString()
                } else {
                    isAnnony = 0.toString()
                }

                var partImage1: MultipartBody.Part? = null
                var partImage2: MultipartBody.Part? = null



                Log.e("imagepath", "image1:  $pathImage1    +    image2: $pathImage2")



                if (pathImage1 != null && pathImage1 != "null" && pathImage1 != "" && pathImage2 != null && pathImage2 != "null" && pathImage2 != "") {
                    // making path for image
                    val file1 = File(pathImage1)
                    val file2 = File(pathImage2)
                    var finalFile1 = compressFile(file1)
                    var finalFile2 = compressFile(file2)

                    if (finalFile1 != null && finalFile2 != null) {
                        val fileReqBody1 =
                            RequestBody.create(MediaType.parse("image/*"), finalFile1)
                        val fileReqBody2 =
                            RequestBody.create(MediaType.parse("image/*"), finalFile2)
                        partImage1 =
                            MultipartBody.Part.createFormData(
                                "image1",
                                finalFile1.getName(),
                                fileReqBody1
                            )
                        partImage2 =
                            MultipartBody.Part.createFormData(
                                "image2",
                                finalFile2.getName(),
                                fileReqBody2
                            )
                    } else {
                        var fileReqBody12 = RequestBody.create(MediaType.parse("image/*"), "")
                        var fileReqBody21 = RequestBody.create(MediaType.parse("image/*"), "")
                        partImage1 = MultipartBody.Part.createFormData("image1", "", fileReqBody12)
                        partImage2 = MultipartBody.Part.createFormData("image2", "", fileReqBody21)
                    }

                } else {
                    if (pathImage1.isEmpty() == false && pathImage2.isEmpty() == true) {
                        // making path for image
                        val file1 = File(pathImage1)
                        var finalFile1 = compressFile(file1)

//                        val file2 = File(pathImage2)
                        var fileReqBody12 =
                            RequestBody.create(MediaType.parse("image/*"), finalFile1)
                        var fileReqBody21 = RequestBody.create(MediaType.parse("image/*"), "")
                        partImage1 = MultipartBody.Part.createFormData(
                            "image1",
                            finalFile1?.getName(),
                            fileReqBody12
                        )
                        partImage2 = MultipartBody.Part.createFormData("image2", "", fileReqBody21)
                    } else if (pathImage1.isEmpty() == true && pathImage2.isEmpty() == false) {
                        // making path for image
//                        val file1 = File(pathImage1)
                        val file2 = File(pathImage2)
                        var finalFile2 = compressFile(file2)

                        var fileReqBody12 = RequestBody.create(MediaType.parse("image/*"), "")
                        var fileReqBody21 =
                            RequestBody.create(MediaType.parse("image/*"), finalFile2)
                        partImage1 = MultipartBody.Part.createFormData("image1", "", fileReqBody12)
                        partImage2 = MultipartBody.Part.createFormData(
                            "image2",
                            finalFile2?.getName(),
                            fileReqBody21
                        )
                    } else if (pathImage1.isEmpty() == true && pathImage2.isEmpty() == true) {
                        /*// making path for image
                        val file1 = File(pathImage1)
                        val file2 = File(pathImage2)*/
                        var fileReqBody12 = RequestBody.create(MediaType.parse("image/*"), "")
                        var fileReqBody21 = RequestBody.create(MediaType.parse("image/*"), "")
                        partImage1 = MultipartBody.Part.createFormData("image1", "", fileReqBody12)
                        partImage2 = MultipartBody.Part.createFormData("image2", "", fileReqBody21)
                    }


                }

                Log.e(
                    "prams rating",
                    "taste:$taste  presentation $presentation  textr $texture ordor$ordor "
                )
                if (partImage1 != null && partImage2 != null) {
                    ratingCommentVM?.postCommentRating(
                        stringConvertToRequestBody(sessionManager?.getValue(SessionManager.USER_ID).toString()),
                        stringConvertToRequestBody(taste.toString()),
                        stringConvertToRequestBody(presentation.toString()),
                        stringConvertToRequestBody(texture.toString()),
                        stringConvertToRequestBody(ordor.toString()),
                        stringConvertToRequestBody(serviceRating.rating.toString()),
                        stringConvertToRequestBody(decorRating.rating.toString()),
                        stringConvertToRequestBody(cleanessRating.rating.toString()),
                        stringConvertToRequestBody(ambienceRating.rating.toString()),
                        stringConvertToRequestBody(edtComment.text.toString()),
                        stringConvertToRequestBody(isAnnony.toString()),
                        partImage1,
                        partImage2,
                        stringConvertToRequestBody(mealId.toString()),
                        stringConvertToRequestBody(restauId.toString())
                    )
                }
            }

        }

        dialog.show()

    }


    fun getCommentPostedObserver() {

        //observe
        ratingCommentVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        ratingCommentVM!!.getRatingLiveData.observe(this, Observer { response ->
            if (response != null) {
                Log.e("rspgetLiked", response.toString())

                Log.e("rspgetLikedStat", response.status.toString())

                if (response.status == 1) {

                    dialog.dismiss()
                    showSnackBar(response.data.message)

                    // flow drive after 2 seconds...
                    Handler().postDelayed({
                        if(fromScreen.equals("Dish_Description_Activity")) {

                            val intent = Intent(this, DishDescriptionActivity::class.java)
                            intent.putExtra("MEAL_ID", mealId)
                            intent.putExtra("RESTAURANT_ID", restauId)
                            intent.putExtra("REFRESH_ACTIVITY", 1)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            startActivity(intent)
                            finish()
                        }
                        else if(fromScreen.equals("CheckinRestroActivity")) {
                            val intent = Intent(this@RestoRatingActivity, CheckinRestroActivity::class.java)
                            intent.putExtra("RESTAURANT_ID", restauId)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                            finish()
                        }
                    }, 800)

                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
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

                    Log.e("case b", "onactvty")

                    //set flag to true
                    imagePick = true


                    //on image picked
                    //Log.e("uri imagePckr fetch", String.valueOf(imageUri));

                    //get image path from uri
//                     path = getPath(imageUri)
                    // Bitmap bmp = uriToBitmap(imageUri);
                    path = this@RestoRatingActivity.getRealPathFromURI(imageUri).toString()
                    Log.e("path", path)

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

//                            Log.e("bitmap1", bitmap.toString())
//                            imgOne?.setImageBitmap(bitmap)


                            if (flag == false && count == 0 && pathImage1 == "") {
                                pathImage1 = path
                                Log.e("bitmap1", bitmap.toString())

                                imgOne?.setImageBitmap(bitmap)
                                flag = true;
                                count++
                            } else if (flag == true && count < 2 && pathImage2 == "") {
                                pathImage2 = path
                                Log.e("bitmap2", bitmap.toString())
                                imgTwo?.setImageBitmap(bitmap)
                                flag = true;
                            } else {
                                showSnackBar("You can choose max 2 Images")
                            }


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


                        if (flag == false && count == 0 && pathImage1 == "") {
                            pathImage1 = path
                            Log.e("bitmap1", bitmap.toString())

                            imgOne?.setImageBitmap(bitmap)
                            flag = true;
                            count++
                        } else if (flag == true && count < 2 && pathImage2 == "") {
                            pathImage2 = path
                            Log.e("bitmap2", bitmap.toString())
                            imgTwo?.setImageBitmap(bitmap)
                            flag = true;
                        } else {
                            showSnackBar("You can choose max 2 Images")
                        }


                        /* if (path != null && path != "" && path != "null") {
                             imgOne.setImageBitmap(bitmap)
                         }*/
//                        Log.e("bitmap1", bitmap.toString())
//                        imgOne?.setImageBitmap(bitmap)

//                        profile_image.setImageBitmap(bitmap)


                    }


                }
            })

        imagePicker.choosePicture(true)
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
                imagePicker.handleActivityResult(resultCode, requestCode, result)

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

                imagePicker.handlePermission(requestCode, grantResults)
        }
    }


}

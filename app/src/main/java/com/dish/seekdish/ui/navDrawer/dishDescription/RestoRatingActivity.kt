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
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.myhexaville.smartimagepicker.ImagePicker
import com.myhexaville.smartimagepicker.OnImagePickedListener
import kotlinx.android.synthetic.main.activity_resto_rating.*
import kotlinx.android.synthetic.main.dialog_upload_img_comment.tvBack
import java.io.IOException


class RestoRatingActivity : BaseActivity() {

    val PERMISSION_REQUEST_IMG_CODE = 1
    internal lateinit var imagePicker: ImagePicker
    internal var imagePick = false
    internal var bitmap: Bitmap? = null
    var flag: Boolean = false
    var count: Int = 0

    var imgOne: ImageView? = null
    var imgTwo: ImageView? = null

    var path = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resto_rating)

        tvNext.setOnClickListener()
        {
            onUploadImages()
        }

        tvBack.setOnClickListener()
        {
            finish()
        }

    }

    fun onUploadImages() {
        val dialog = Dialog(this)
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
            val intent = Intent(this@RestoRatingActivity, DishDescriptionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        tvBack.setOnClickListener {
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

        dialog.show()

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

                    //get image path from uri
//                     path = getPath(imageUri)
                    // Bitmap bmp = uriToBitmap(imageUri);
                    path = this@RestoRatingActivity.getPath(imageUri).toString()
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

//                            Log.e("bitmap1", bitmap.toString())
//                            imgOne?.setImageBitmap(bitmap)

                            if (flag == false && count == 0) {
                                Log.e("bitmap1", bitmap.toString())
                                imgOne?.setImageBitmap(bitmap)
                                flag = true;
                                count++
                            } else if (flag == true && count < 2) {
                                Log.e("bitmap2", bitmap.toString())

                                imgTwo?.setImageBitmap(bitmap)
                                flag = true;
                                count++

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


                        if (flag == false && count == 0) {
                            Log.e("bitmap1", bitmap.toString())

                            imgOne?.setImageBitmap(bitmap)
                            flag = true;
                            count++
                        } else if (flag == true && count < 2) {
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

                imagePicker.handlePermission(requestCode, grantResults)
        }
    }


}

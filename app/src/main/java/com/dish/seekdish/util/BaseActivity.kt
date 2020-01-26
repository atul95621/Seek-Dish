package com.dish.seekdish.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log

import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern


open class BaseActivity : AppCompatActivity() {

    var toolBar: Toolbar? = null
    var tvToolbarTitle: TextView? = null
    var imgLeft: ImageView? = null
    var imgRight: ImageView? = null

    var activity: Activity? = null
    lateinit var conxt: Context


    //utils
    lateinit var connectionDetector: ConnectivityManager
    lateinit var utilities: Utilities
//    lateinit var sessionManager: SessionManager;

    lateinit var progressDialog: Dialog
    lateinit var progressDialogMvvm: Dialog

//    internal lateinit var apiInterface: APIInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        activity = this@BaseActivity
        conxt = this@BaseActivity

        //utils
        connectionDetector = ConnectivityManager(this)
        utilities = Utilities(this, activity as BaseActivity)
//        sessionManager = SessionManager(this);
        progressDialog = Dialog(this);
        progressDialogMvvm = utilities.initProgressDialog(this)

        //api interface intiialization
//        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)


    }


    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }


    fun glideMemoryCacheClear() {

        Glide.get(applicationContext).clearMemory()

        Thread(Runnable { Glide.get(applicationContext).clearDiskCache() }).start()
    }


    fun showSnackBar(text: String) {
        val snackbar = Snackbar.make(activity!!.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        snackbar.show()
    }

/*    fun showSnackBar( text : String) {
        var snackbar = Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);
        val snackbarView = snackbar.view

        snackbarView.setBackgroundColor(ContextCompat.getColor( activity,R.color.colorPrimary))
        snackbar.show();
    }*/

    fun hideKeyBoard() {
        getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    fun showSnackbar(editText: EditText, message: String) {

        val snackbar = Snackbar
            .make(editText, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    companion object {

        private val VIDEO_DIRECTORY = "/seekdish"
    }

    // for converting string to requestbody
    fun stringConvertToRequestBody(pram: String): RequestBody {
        val covertedParam = RequestBody.create(MediaType.parse("text/plain"), pram)
        return covertedParam
    }


    fun decodeSampledBitmapFromFile(
        path: String,
        reqWidth: Int, reqHeight: Int
    ): Bitmap { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        // Calculate inSampleSize
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        options.inPreferredConfig = Bitmap.Config.RGB_565
        var inSampleSize = 1

        if (height > reqHeight) {
            inSampleSize = Math.round(height.toFloat() / reqHeight.toFloat())
        }

        val expectedWidth = width / inSampleSize

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round(width.toFloat() / reqWidth.toFloat())
        }


        options.inSampleSize = inSampleSize

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(path, options)
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }

    fun compressFile(file: File): File? {

        try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image

            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 200        // x............

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)

            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            val outPutFile = File.createTempFile("abc", "image")
            val outputStream = FileOutputStream(outPutFile)
            // y.......
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

            return outPutFile
        } catch (e: Exception) {
            Log.e("compress_exception","${e.message}")
            return null
        }

    }

    fun emailValidator(email: String): Boolean {
        var pattern: Pattern
        var matcher: Matcher
        var EMAIL_PATTERN: String =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    fun setIsLoading(value: Boolean) {
        //hide and show progress bar
        if (value) {
            progressDialogMvvm.show()
            // true
            Log.e("change in dataVal bool", value.toString())
        } else {
            progressDialogMvvm.dismiss()

            // false
            Log.e("change in dataVal bool", value.toString())
        }
    }

    fun getBitmapFromUrl(src: String): Bitmap? {

        try {
            var url = URL(src)
            var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true);
            connection.connect();
            var input: InputStream = connection.getInputStream();
            var myBitmap: Bitmap = BitmapFactory.decodeStream(input);
            return myBitmap
        } catch (e: IOException) {
            e.printStackTrace();
            return null;
        }
    }

    fun datePrase(date: String): String {
        var newDate = ""
        var oldFormat = "yyyy-MM-dd HH:mm:ss";
        var newFormat = "dd-MM-yyyy";

        var sdf1 = SimpleDateFormat(oldFormat);
        var sdf2 = SimpleDateFormat(newFormat);


        try {

            newDate = sdf2.format(sdf1.parse(date))

        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return newDate
    }
}

package com.dish.seekdish.util

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

import android.view.Window
import androidx.core.content.ContextCompat
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.google.android.material.snackbar.Snackbar




public class Utilities(val cntxt: Context, val actvty: Activity) {

    lateinit var activity: Activity
    lateinit var context: Context

    init {
        this.activity = actvty
        this.context = cntxt

    }

    fun initProgressDialog(context: Context): Dialog {
        var progressDialog = Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.parent_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.window!!.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        return progressDialog;
    }

    /* companion object {
            fun initProgressDialog(context: Context): Dialog {
             var progressDialog = Dialog(context);
             progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
             progressDialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
             progressDialog.setContentView(R.layout.progress_dialog_view);
             progressDialog.setCancelable(false);
             return progressDialog;
         }
 */

//    public fun checkEmail( email : String ) :Boolean {
//        return MyApplication.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
//    }

    companion object {

        fun setDrawableSelector(context: Context, normal: Int, selected: Int): Drawable {

            val state_normal = ContextCompat.getDrawable(context, normal)
            val state_pressed = ContextCompat.getDrawable(context, selected)


            val state_normal_bitmap = (state_normal as BitmapDrawable).bitmap

            // Setting alpha directly just didn't work, so we draw a new bitmap!
            val disabledBitmap = Bitmap.createBitmap(
                state_normal.intrinsicWidth,
                state_normal.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(disabledBitmap)

            val paint = Paint()
            val filter =
                PorterDuffColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
            paint.colorFilter = filter as ColorFilter?
            canvas.drawBitmap(state_normal_bitmap, 0f, 0f, paint)

            val state_normal_drawable = BitmapDrawable(context.resources, disabledBitmap)


            val drawable = StateListDrawable()

            drawable.addState(
                intArrayOf(android.R.attr.state_selected),
                state_pressed
            )
            drawable.addState(
                intArrayOf(android.R.attr.state_enabled),
                state_normal_drawable
            )

            return drawable
        }

    }


 /*   fun showSnackBar(text: String) {
        val snackbar = Snackbar.make(activity!!.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        snackbar.show()
    }*/

    fun showSnackBar( text : String) {
        var snackbar = Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor( activity,R.color.colorPrimary))
        snackbar.show();
    }


}
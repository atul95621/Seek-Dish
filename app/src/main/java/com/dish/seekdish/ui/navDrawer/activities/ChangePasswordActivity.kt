package com.dish.seekdish.ui.navDrawer.activities

import android.os.Bundle
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.util.BaseActivity
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_password.edtConfirmPassword
import kotlinx.android.synthetic.main.activity_change_password.edtNewPassword
import kotlinx.android.synthetic.main.activity_my_profile.tvBack
import kotlinx.android.synthetic.main.activity_my_profile.tvContact

class ChangePasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        tvBack.setOnClickListener()
        {
            finish()
        }

        tvContact.setOnClickListener()
        {
            if (edtOldPassword.text.toString().trim().isEmpty()) {
                showSnackBar("Please fill old password.")
            } else if (edtNewPassword.text.toString().trim().isEmpty()) {
                showSnackBar("Please fill new password")
            } else if (edtConfirmPassword.text.toString().trim().isEmpty()) {
                showSnackBar("Please fill confirm password")
            } else if (edtNewPassword.text.toString().compareTo(edtConfirmPassword.text.toString()) != 0) {
                showSnackBar("Password and Confirm Password doesn't match.")
                edtConfirmPassword!!.requestFocus()
            } else {
                ProgressBarClass.progressBarCalling(this)

//                changePass(edtOneTimePassword.text.toString().trim(),edtNewPassword.text.toString().trim(),edtConfirmPassword.text.toString().trim())

            }

        }
    }

    /* fun changePass() {


         val call = apiInterface.changePassword(sessionManager.getValue(SessionManager.EMAIL))
         call.enqueue(object : Callback<JsonObject> {
             override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                 ProgressBarClass.dialog.dismiss()

                 if (response.code().toString().equals("200")) {

                     val jsonObject: JsonObject = response.body()!!

 //                    Log.e("responseforgot", " " + response.body().toString())
                     if (response.code() == 200) {
                         val status: String = jsonObject.get("status").toString()
 //                        Log.e("responseStatus", " " + status)

                         if (status.equals("1")) {
                             val jsonDataObj: JsonObject = jsonObject.getAsJsonObject("data");
                             val message: String = jsonDataObj.get("message").toString()


                         }
                     }

                 } else {
                     showSnackBar(getResources().getString(R.string.error_occured));
                 }


             }

             override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                 showSnackBar(getResources().getString(R.string.error_occured));

                 call.cancel()

                 ProgressBarClass.dialog.dismiss()

             }
         })
     }*/
}

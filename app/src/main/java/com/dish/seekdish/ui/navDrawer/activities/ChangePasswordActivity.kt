package com.dish.seekdish.ui.navDrawer.activities

import android.os.Bundle
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_password.edtConfirmPassword
import kotlinx.android.synthetic.main.activity_change_password.edtNewPassword
import kotlinx.android.synthetic.main.activity_my_profile.tvBack
import kotlinx.android.synthetic.main.activity_my_profile.tvContact
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : BaseActivity() {
    var sessionManager: SessionManager? = null;
    internal lateinit var apiInterface: APIInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        sessionManager = SessionManager(this)

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

                changePass(
                    edtOldPassword.text.toString().trim(),
                    edtNewPassword.text.toString().trim(),
                    edtConfirmPassword.text.toString().trim()
                )

            }

        }
    }

    fun changePass(old: String, new: String, confrmNew: String) {


        val call =
            apiInterface.changePassword(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                old,
                new
            )
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(call: Call<CancelReModel>, response: Response<CancelReModel>) {

                ProgressBarClass.dialog.dismiss()

                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as CancelReModel

                    if (modelObj.status == 1) {
                        showSnackBar(modelObj.data.message)
                    }

                } else {
                    showSnackBar(getResources().getString(R.string.error_occured));
                }


            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {

                showSnackBar(getResources().getString(R.string.error_occured));

                call.cancel()

                ProgressBarClass.dialog.dismiss()

            }
        })
    }
}

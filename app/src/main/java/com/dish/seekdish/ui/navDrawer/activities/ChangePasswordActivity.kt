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
                showSnackBar(getString(R.string.old_pass))
            } else if (edtNewPassword.text.toString().trim().isEmpty()) {
                showSnackBar(getString(R.string.new_pass))
            } else if (edtConfirmPassword.text.toString().trim().isEmpty()) {
                showSnackBar(getString(R.string.confrm_pass))
            } else if (edtNewPassword!!.text.toString().trim().length < 5) {
                showSnackBar(getString(R.string.number_length))
                edtNewPassword!!.requestFocus()
            } else if (edtNewPassword.text.toString()
                    .compareTo(edtConfirmPassword.text.toString()) != 0
            ) {
                showSnackBar(getString(R.string.pass_not_match))
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
                        showSnackBar(modelObj.message)
                    } else {
                        showSnackBar(modelObj.message)
                    }
                } else {
                    showSnackBar(getResources().getString(R.string.error_occured) + "  ${response.code()}");
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

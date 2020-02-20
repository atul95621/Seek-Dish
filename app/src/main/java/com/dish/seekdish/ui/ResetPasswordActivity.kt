package com.dish.seekdish.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.login.LoginActivity
import com.dish.seekdish.util.BaseActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResetPasswordActivity : BaseActivity() {


        internal lateinit var apiInterface: APIInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        tvBack.setOnClickListener()
        {
            finish()
        }

        tvContact.setOnClickListener()
        {
            if (edtEmail.text.toString().trim().isEmpty()) {
                showSnackBar(getString(R.string.please_fill_email))
            } else if (edtRandomPassword.text.toString().trim().isEmpty()) {
                showSnackBar(getString(R.string.fill_email))
            } else if (edtNewPassword.text.toString().trim().isEmpty()) {
                showSnackBar(getString(R.string.fill_new_pass))
            } else if (edtConfirmPassword.text.toString().trim().isEmpty()) {
                showSnackBar(getString(R.string.fill_confirm_pass))
            } else if (edtNewPassword.text.toString().compareTo(edtConfirmPassword.text.toString()) != 0) {
                showSnackBar(getString(R.string.pass_not_match))
                edtConfirmPassword!!.requestFocus()
            } else {
                ProgressBarClass.progressBarCalling(this)

                resetPass(
                    edtEmail.text.toString().trim(),
                    edtRandomPassword.text.toString().trim(),
                    edtNewPassword.text.toString().trim(),
                    edtConfirmPassword.text.toString().trim()
                )

            }

        }
    }

    fun resetPass(email: String, randomPassword: String, newPassword: String, confirmPassword: String) {

        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)


        val call = apiInterface.resetPassword(email, randomPassword, newPassword,confirmPassword)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                ProgressBarClass.dialog.dismiss()


                Log.e("respResetSignupCode", response.code().toString() + "")

                if (response.code().toString().equals("200")) {

                    val jsonObject: JsonObject = response.body()!!

                    if (response.code() == 200) {
                        val status: String = jsonObject.get("status").toString()

                        if (status.equals("1")) {

                            val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                         /*   val jsonDataObj: JsonObject = jsonObject.getAsJsonObject("data");
                            val message: String = jsonDataObj.get("message").toString()*/


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
    }
}

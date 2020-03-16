package com.dish.seekdish.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.login.LoginActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import com.dish.seekdish.walkthrough.WalkThroughActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.activity_forgot.tvBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotActivity : BaseActivity() {

    internal lateinit var apiInterface: APIInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        tvSend.setOnClickListener()
        {

            if (edtForgotEmail.text.toString().trim().isEmpty()) {
                showSnackBar("Please fill email.")
            } else {
                ProgressBarClass.progressBarCalling(this)

                forgotPassword(edtForgotEmail.text.toString().trim())
//                onSendClick()

            }
        }

        tvBack.setOnClickListener()
        {
            finish()
        }


    }

    fun forgotPassword(email: String) {

        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)

        val call = apiInterface.forgotPassword(email)
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(call: Call<CancelReModel>, response: Response<CancelReModel>) {

                ProgressBarClass.dialog.dismiss()

                if (response.code().toString().equals("200")) {


//                    Log.e("responseforgot", " " + response.body().toString())
                    var model = response.body() as CancelReModel
                    if (model.status == 1) {
                        //dialog open
                        onSendClick(model.message)
                    } else {
                        showSnackBar(model.message)
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured) + "  ${response.code()}");
                }


            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {

                showSnackBar(resources.getString(R.string.error_occured) + "  ${t.message}");
                call.cancel()
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    private fun onSendClick(message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_email_verify)

        val textViewDescrp = dialog.findViewById<TextView>(R.id.textViewDescrp)
        val btnAccept = dialog.findViewById<Button>(R.id.btnAccept)
//        textViewDescrp.setText(message)
        textViewDescrp.setText(message)
        // button_yes clk
        btnAccept.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@ForgotActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
//            finish()
        }

        dialog.show()

    }

}

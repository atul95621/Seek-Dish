package com.dish.seekdish.ui.navDrawer.activities

import android.os.Bundle
import android.util.Log
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.activities.model.ContactModel
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.activity_contact.tvBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContactActivity : BaseActivity() {
    var sessionManager: SessionManager? = null;
    internal lateinit var apiInterface: APIInterface
    var user_id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)


        sessionManager = SessionManager(this)

        hideKeyBoard()

        if (connectionDetector.isConnectingToInternet) {
            // api hit
            user_id = intent.getStringExtra("USER_ID").toString()

            if (user_id.isNullOrEmpty() == false) {
                reqApiHit(user_id)
            }
        } else {
            showSnackBar(resources.getString(R.string.check_connection))
        }


        tvBack.setOnClickListener()
        {
            finish()
        }

        tvSave.setOnClickListener()
        {
            if (connectionDetector.isConnectingToInternet) {
                // api hit
                saveContactApi()
            } else {
                showSnackBar(resources.getString(R.string.check_connection))
            }
        }
    }

    private fun saveContactApi() {
        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call = apiInterface.postContactDeatails(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            edtEmail.text.toString(),
            ccpContact.selectedCountryCode,
            edtPhone.text.toString(),
            edtSkype.text.toString(),
            edtFacebook.text.toString(),
            edtTwitter.text.toString()
        )

        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(
                call: Call<CancelReModel>,
                response: Response<CancelReModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                Log.e("respStr", " " + response.body().toString())
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as CancelReModel
                    if (modelObj.status == 1) {
                        showSnackBar(modelObj.data.message)
                    }

                } else {
//                    iSignUpView.onSetLoggedin(false, response)
                    showSnackBar(resources.getString(R.string.error_occured));
                }
            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {

//                Log.e("responseFailure", " " + t.toString())
                showSnackBar(resources.getString(R.string.error_occured));
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
            }
        })
    }


    fun reqApiHit(userId: String) {
        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call =
            apiInterface.getContactDeatails(userId)
//        val call = apiInterface.getContactDeatails("129")
        call.enqueue(object : Callback<ContactModel> {
            override fun onResponse(
                call: Call<ContactModel>,
                response: Response<ContactModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

                Log.e("respStr", " " + response.body().toString())

                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as ContactModel
                    if (modelObj.status == 1) {
                        edtTwitter.setText(modelObj.data[0].twitter_id)
                        edtFacebook.setText(modelObj.data[0].facebook_id)
                        edtEmail.setText(modelObj.data[0].email)
                        edtPhone.setText(modelObj.data[0].phone)
                        edtSkype.setText(modelObj.data[0].skype_id)
                        ccpContact.setCountryForPhoneCode(modelObj.data[0].country)
                    }

                } else {
//                    iSignUpView.onSetLoggedin(false, response)
                    showSnackBar(resources.getString(R.string.error_occured));

                }


            }

            override fun onFailure(call: Call<ContactModel>, t: Throwable) {

                Log.e("responseFailure", " " + t.message)
                showSnackBar(resources.getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

}

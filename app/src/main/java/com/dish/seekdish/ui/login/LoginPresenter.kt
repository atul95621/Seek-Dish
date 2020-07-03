package com.dish.seekdish.ui.login

import android.util.Log
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(private val iSignUpView: ILoginView, val loginActivity: LoginActivity) {

    //activity
    internal lateinit var apiInterface: APIInterface
    fun login(
        email: String,
        password: String,
        languageId: String,
        fcm: String
    ) {
        ProgressBarClass.progressBarCalling(loginActivity)
        apiInterface = APIClient.getClient(loginActivity).create(APIInterface::class.java)

        val call = apiInterface.doLogIn(email, password,languageId,fcm)
        call.enqueue(object : Callback<LoginDataClass> {
            override fun onResponse(call: Call<LoginDataClass>, response: Response<LoginDataClass>) {

                Log.e("responseCode", response.code().toString() + "")
                Log.e("responseStatus", " " + response.body()?.status)
                Log.e("responseString", " " + response.body().toString())
                Log.e("responseerror", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
                    iSignUpView.onSetLoggedin(true, response)

                } else {
                    iSignUpView.onSetLoggedin(false, response)
                }
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
            }

            override fun onFailure(call: Call<LoginDataClass>, t: Throwable) {
//                Log.e("responseFailure", " " + t.toString())
                loginActivity.showSnackBar(loginActivity.getResources().getString(R.string.error_occured)  +"   ${t.message}");
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    fun checkUpdate(
    ) {
//        ProgressBarClass.progressBarCalling(loginActivity)
        apiInterface = APIClient.getClient(loginActivity).create(APIInterface::class.java)
        val call = apiInterface.checkUpdate()
        call.enqueue(object : Callback<CheckUpdateModel> {
            override fun onResponse(call: Call<CheckUpdateModel>, response: Response<CheckUpdateModel>) {

                if (response.code().toString().equals("200")) {
                    iSignUpView.checkUpdate(true, response)
                } else {
                    iSignUpView.checkUpdate(false, response)
                }
                // canceling the progress bar
//                ProgressBarClass.dialog.dismiss()
            }

            override fun onFailure(call: Call<CheckUpdateModel>, t: Throwable) {
                loginActivity.showSnackBar(loginActivity.getResources().getString(R.string.error_occured)  +"   ${t.message}");
                call.cancel()
                // canceling the progress bar
//                ProgressBarClass.dialog.dismiss()
            }
        })
    }

}
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


    fun login(email: String, password: String, languageId: String) {

        ProgressBarClass.progressBarCalling(loginActivity)

        apiInterface = APIClient.getClient(loginActivity).create(APIInterface::class.java)


        val call = apiInterface.doLogIn(email, password,languageId)
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

                loginActivity.showSnackBar(loginActivity.getResources().getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


}
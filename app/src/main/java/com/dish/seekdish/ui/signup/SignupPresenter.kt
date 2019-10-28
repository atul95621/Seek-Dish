package com.dish.seekdish.ui.signup

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.HomeActivity
import com.facebook.FacebookSdk
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupPresenter(private val iSignUpView: ISignUpView, val context: Context) {

    //activity
    var signupActivity = SignupActivity()
    internal lateinit var apiInterface: APIInterface


    fun signUp(
        email: RequestBody,
        password: RequestBody,
        confirmPassword: RequestBody,
        firstname: RequestBody,
        lastName: RequestBody,
        phone: RequestBody,
        userName: RequestBody,
        gender: RequestBody,
        bio: RequestBody,
        countryCode: RequestBody,
        fcmToken: RequestBody,
        part: MultipartBody.Part
    ) {

        ProgressBarClass.progressBarCalling(context)

        apiInterface = APIClient.getClient(context).create(APIInterface::class.java)


        val call = apiInterface.doSignUp(
            email,
            password,
            confirmPassword,
            firstname,
            lastName,
            phone,
            userName,
            gender,
            bio,
            countryCode,
            fcmToken,
            part
        )
        call.enqueue(object : Callback<SignUpModel> {
            override fun onResponse(call: Call<SignUpModel>, response: Response<SignUpModel>) {

                Log.e("responseSignupCode", response.code().toString() + "")
                Log.e("responseSignupStatus", " " + response.body()?.status)
                Log.e("responseSignupString", " " + response.body().toString())
                Log.e("responseSignuperror", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
//                    Log.e("respSignupCode", response.code().toString() + "")
                    iSignUpView.onSetSignedUp(true, response)


                } else {
                    iSignUpView.onSetSignedUp(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<SignUpModel>, t: Throwable) {

                Log.e("responseSignupFailure", " " + t.toString())


                Toast.makeText(
                    FacebookSdk.getApplicationContext(),
                    context.resources.getString(R.string.error_occured),
                    Toast.LENGTH_LONG
                ).show()

//                signupActivity.utilities.showSnackBar(context.resources.getString(R.string.error_occured))

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    fun signUpWithoutImage(
        email: RequestBody,
        password: RequestBody,
        confirmPassword: RequestBody,
        firstname: RequestBody,
        lastName: RequestBody,
        phone: RequestBody,
        userName: RequestBody,
        gender: RequestBody,
        bio: RequestBody,
        countryCode: RequestBody,
        fcmToken: RequestBody
    ) {

        ProgressBarClass.progressBarCalling(context)

        apiInterface = APIClient.getClient(context).create(APIInterface::class.java)


        val call = apiInterface.doSignUpWithoutImage(
            email,
            password,
            confirmPassword,
            firstname,
            lastName,
            phone,
            userName,
            gender,
            bio,
            countryCode,
            fcmToken
        )
        call.enqueue(object : Callback<SignUpModel> {
            override fun onResponse(call: Call<SignUpModel>, response: Response<SignUpModel>) {

                Log.e("responseSignupCode", response.code().toString() + "")
                Log.e("responseSignupStatus", " " + response.body()?.status)
                Log.e("responseSignupString", " " + response.body().toString())
                Log.e("responseSignuperror", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
//                    Log.e("respSignupCode", response.code().toString() + "")
                    iSignUpView.onSetSignedUp(true, response)


                } else {
                    iSignUpView.onSetSignedUp(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<SignUpModel>, t: Throwable) {

                Log.e("responseSignupFailure", " " + t.toString())


                Toast.makeText(
                    FacebookSdk.getApplicationContext(),
                    context.resources.getString(R.string.error_occured),
                    Toast.LENGTH_LONG
                ).show()

//                signupActivity.utilities.showSnackBar(context.resources.getString(R.string.error_occured))

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

}
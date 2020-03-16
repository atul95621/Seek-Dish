package com.dish.seekdish.ui.signup

import android.util.Log
import android.widget.Toast
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.facebook.FacebookSdk
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupPresenter(private val iSignUpView: ISignUpView, val signupActivity: SignupActivity) {

    //activity
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
        part: MultipartBody.Part,
        langId: RequestBody
    ) {

        ProgressBarClass.progressBarCalling(signupActivity)

        apiInterface = APIClient.getClient(signupActivity).create(APIInterface::class.java)


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
            part,
            langId
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


            /*    Toast.makeText(
                    FacebookSdk.getApplicationContext(),
                    signupActivity.resources.getString(R.string.error_occured),
                    Toast.LENGTH_LONG
                ).show()
*/
                signupActivity.utilities.showSnackBar(signupActivity.resources.getString(R.string.error_occured)  +"  ${t.message}")

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
        fcmToken: RequestBody,
        langId: RequestBody
    ) {

        ProgressBarClass.progressBarCalling(signupActivity)

        apiInterface = APIClient.getClient(signupActivity).create(APIInterface::class.java)


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
            fcmToken,
            langId
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


              /*  Toast.makeText(
                    FacebookSdk.getApplicationContext(),
                    signupActivity.resources.getString(R.string.error_occured),
                    Toast.LENGTH_LONG
                ).show()*/

                signupActivity.utilities.showSnackBar(signupActivity.resources.getString(R.string.error_occured)  +"  ${t.message}")

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

}
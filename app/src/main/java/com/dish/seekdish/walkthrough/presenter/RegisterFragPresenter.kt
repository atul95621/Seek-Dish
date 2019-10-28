package com.dish.seekdish.walkthrough.presenter

import android.content.Context
import android.content.Intent
import android.util.Log
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.signup.ISignUpView
import com.dish.seekdish.ui.signup.SignUpModel
import com.dish.seekdish.ui.signup.SignupActivity
import com.dish.seekdish.walkthrough.view.IRegisterFragView
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragPresenter(private val iRegisterFragView: IRegisterFragView, val context: Context) {

    internal lateinit var apiInterface: APIInterface

// facebook api
    fun facebookSigin(
        email: String,
        firstname: String,
        lastName: String,
        photoUrl: String,
        fcmToken: String,
        facebookUserId: String
    ) {


        apiInterface = APIClient.getClient(context).create(APIInterface::class.java)


        val call = apiInterface.doFacebookSignup(
            email,
            firstname,
            lastName,
            photoUrl,
            fcmToken,
            facebookUserId
        )
        call.enqueue(object : Callback<SignUpModel> {
            override fun onResponse(call: Call<SignUpModel>, response: Response<SignUpModel>) {

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

                Log.e("pramsFacebook","EMAIL: "+ email +" FIRSTNAME  "+firstname+" LASTNAME  "+ lastName +"  PHOTO URL  "+photoUrl+ "   FCM TOKEN "+fcmToken+" FACEBOOK ID   "+facebookUserId)


                Log.e("responseFbSignupCode", response.code().toString() + "")
                Log.e("responseFbSignupStatus", " " + response.body()?.status)
                Log.e("responseFbSignupString", " " + response.body().toString())
                Log.e("responseFbSignuperror", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
                    Log.e("c", response.code().toString() + "")
                    iRegisterFragView.onFacebookSiginDetails(true, response)

                } else {
                    iRegisterFragView.onFacebookSiginDetails(false, response)
                }


            }

            override fun onFailure(call: Call<SignUpModel>, t: Throwable) {

                Log.e("respFacebookFailure", " " + t.toString())

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    //twitter api
    fun twitterSigin(
        email: String,
        firstname: String,
        photoUrl: String,
        fcmToken: String,
        twitter_id: String
    ) {


        apiInterface = APIClient.getClient(context).create(APIInterface::class.java)

        Log.e("pramsTwitter"," EMAIL:"+ email +" FIRSTNAME: "+firstname+" PHOTO URL: "+photoUrl+ " FCM TOKEN:"+fcmToken+" TWITTER ID:  "+twitter_id)


        val call = apiInterface.doTwitterSignup(
            email,
            firstname,
            photoUrl,
            fcmToken,
            twitter_id
        )
        call.enqueue(object : Callback<SignUpModel> {
            override fun onResponse(call: Call<SignUpModel>, response: Response<SignUpModel>) {

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

                Log.e("responseTwitterSignCode", response.code().toString() + "")
                Log.e("responseTwitterSignSt", " " + response.body()?.status)
                Log.e("responseTwitterSignStr", " " + response.body().toString())
                Log.e("responseTwitterSigner", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
                    Log.e("respTwitter", response.code().toString() + "")
                    iRegisterFragView.onTwitterSiginDetails(true, response)

                } else {
                    iRegisterFragView.onTwitterSiginDetails(false, response)
                }


            }

            override fun onFailure(call: Call<SignUpModel>, t: Throwable) {

                Log.e("respTwitterFailure", " " + t.toString())

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

}
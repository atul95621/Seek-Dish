package com.dish.seekdish.walkthrough.presenter

import android.util.Log
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.signup.SignUpModel
import com.dish.seekdish.walkthrough.WalkThroughActivity
import com.dish.seekdish.walkthrough.view.IRegisterFragView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragPresenter(
    private val iRegisterFragView: IRegisterFragView,
    val context: WalkThroughActivity
) {

    internal lateinit var apiInterface: APIInterface

    // facebook api
    fun facebookSigin(
        email: String,
        firstname: String,
        lastName: String,
        photoUrl: String,
        fcmToken: String,
        facebookUserId: String,
        langId: String
    ) {
        apiInterface = APIClient.getClient(context).create(APIInterface::class.java)
        val call = apiInterface.doFacebookSignup(
            email,
            firstname,
            lastName,
            photoUrl,
            fcmToken,
            facebookUserId,
            langId
        )
        call.enqueue(object : Callback<SignUpModel> {
            override fun onResponse(call: Call<SignUpModel>, response: Response<SignUpModel>) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {
                    Log.e("c", response.code().toString() + "")
                    iRegisterFragView.onFacebookSiginDetails(true, response)

                } else {
                    iRegisterFragView.onFacebookSiginDetails(false, response)
                }


            }

            override fun onFailure(call: Call<SignUpModel>, t: Throwable) {
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

                context.showSnackBar(context.getResources().getString(R.string.error_occured)   +"   ${t.message}");


            }
        })
    }


    //twitter api
    fun twitterSigin(
        email: String,
        firstname: String,
        photoUrl: String,
        fcmToken: String,
        twitter_id: String,
        langId: String
    ) {


        apiInterface = APIClient.getClient(context).create(APIInterface::class.java)

        Log.e(
            "pramsTwitter",
            " EMAIL:" + email + " FIRSTNAME: " + firstname + " PHOTO URL: " + photoUrl + " FCM TOKEN:" + fcmToken + " TWITTER ID:  " + twitter_id
        )


        val call = apiInterface.doTwitterSignup(
            email,
            firstname,
            "",
            photoUrl,
            fcmToken,
            twitter_id,
            langId
        )
        call.enqueue(object : Callback<SignUpModel> {
            override fun onResponse(call: Call<SignUpModel>, response: Response<SignUpModel>) {

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()



                if (response.code().toString().equals("200")) {
                    iRegisterFragView.onTwitterSiginDetails(true, response)

                } else {
                    iRegisterFragView.onTwitterSiginDetails(false, response)
                }


            }

            override fun onFailure(call: Call<SignUpModel>, t: Throwable) {
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

                context.showSnackBar(context.getResources().getString(R.string.error_occured)  +"    ${t.message}");


            }
        })
    }

}
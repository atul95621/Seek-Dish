package com.dish.seekdish.walkthrough.presenter

import com.dish.seekdish.R
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.login.CheckUpdateModel
import com.dish.seekdish.walkthrough.WalkThroughActivity
import com.dish.seekdish.walkthrough.view.IRegisterFragView
import com.dish.seekdish.walkthrough.view.IWalkThrough
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalkThroughPresenter(private val iWalkThrough: IWalkThrough,
                           val activity: WalkThroughActivity) {
    fun checkUpdate(
    ) {
         lateinit var apiInterface: APIInterface

//        ProgressBarClass.progressBarCalling(loginActivity)
        apiInterface = APIClient.getClient(activity).create(APIInterface::class.java)
        val call = apiInterface.checkUpdate()
        call.enqueue(object : Callback<CheckUpdateModel> {
            override fun onResponse(call: Call<CheckUpdateModel>, response: Response<CheckUpdateModel>) {

                if (response.code().toString().equals("200")) {
                    iWalkThrough.checkUpdate(true, response)
                } else {
                    iWalkThrough.checkUpdate(false, response)
                }
                // canceling the progress bar
//                ProgressBarClass.dialog.dismiss()
            }

            override fun onFailure(call: Call<CheckUpdateModel>, t: Throwable) {
                activity.showSnackBar(activity.getResources().getString(R.string.error_occured)  +"   ${t.message}");
                call.cancel()
                // canceling the progress bar
//                ProgressBarClass.dialog.dismiss()
            }
        })
    }
}
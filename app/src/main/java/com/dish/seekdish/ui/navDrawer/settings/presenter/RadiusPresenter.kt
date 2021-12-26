package com.dish.seekdish.ui.navDrawer.settings.presenter

import android.util.Log
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.RadiusCenterActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SendUserGeneralSetting
import com.dish.seekdish.ui.navDrawer.settings.view.IRadiusView
import com.dish.seekdish.ui.navDrawer.settings.view.ISettingView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RadiusPresenter(
    private val iRadiusView: IRadiusView,
    val radiusCenterActivity: RadiusCenterActivity
) {

    internal lateinit var apiInterface: APIInterface

    fun setGeneralSettingInfo(
        userId: String,
        geo: String,
        push: String,
        checkboxPrivate: String,
        radius: String,
        radius_center_location: String
    ) {
        ProgressBarClass.progressBarCalling(radiusCenterActivity)
        apiInterface = APIClient.getClient(radiusCenterActivity).create(APIInterface::class.java)


        val call = apiInterface.setGeneralSetting(
            userId,
            geo,
            push,
            checkboxPrivate,
            radius,
            radius_center_location
        )
        call.enqueue(object : Callback<SendUserGeneralSetting> {
            override fun onResponse(
                call: Call<SendUserGeneralSetting>,
                response: Response<SendUserGeneralSetting>
            ) {
                if (response.code().toString().equals("200")) {
                    iRadiusView.onSetSettingInfo(true, response)

                } else {
                    iRadiusView.onSetSettingInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }

            override fun onFailure(call: Call<SendUserGeneralSetting>, t: Throwable) {
                radiusCenterActivity.showSnackBar(
                    radiusCenterActivity.getResources()
                        .getString(R.string.error_occured) + "  ${t.message}"
                );

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }
}





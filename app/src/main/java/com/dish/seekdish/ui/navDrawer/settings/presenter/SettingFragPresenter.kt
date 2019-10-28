package com.dish.seekdish.ui.navDrawer.settings.presenter

import android.util.Log
import android.widget.Switch
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LanguageData
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SaveLanguageModel
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SendUserGeneralSetting
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SettingDataClass
import com.dish.seekdish.ui.navDrawer.settings.view.ISettingView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragPresenter(private val iSettingView: ISettingView, val homeActivity: HomeActivity) {

    //activity
    internal lateinit var apiInterface: APIInterface


    fun getGeneralSettingInfo(userId: String) {

        ProgressBarClass.progressBarCalling(homeActivity)

        apiInterface = APIClient.getClient(homeActivity).create(APIInterface::class.java)


        val call = apiInterface.getGeneralSetting(userId)
        call.enqueue(object : Callback<SettingDataClass> {
            override fun onResponse(call: Call<SettingDataClass>, response: Response<SettingDataClass>) {

                Log.e("respSettingGeneralCode", response.code().toString() + "")
//                Log.e("respLocationStatus", " " + response.body()?.status)
                Log.e("respSettingGeneralSt", " " + response.body().toString())
                Log.e("respSettingGeneralerror", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
                    iSettingView.onGetSettingInfo(true, response)

                } else {
                    iSettingView.onGetSettingInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<SettingDataClass>, t: Throwable) {

                Log.e("responseFailure", " " + t.toString())


                homeActivity.showSnackBar(homeActivity.getResources().getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    fun setGeneralSettingInfo(
        userId: String,
        geo: String,
        push: String,
        checkboxPrivate: String,
        radius: Int
    ) {

        ProgressBarClass.progressBarCalling(homeActivity)

        apiInterface = APIClient.getClient(homeActivity).create(APIInterface::class.java)


        val call = apiInterface.setGeneralSetting(userId, geo, push, checkboxPrivate, radius.toString())
        call.enqueue(object : Callback<SendUserGeneralSetting> {
            override fun onResponse(call: Call<SendUserGeneralSetting>, response: Response<SendUserGeneralSetting>) {


                Log.e(
                    "settingSetPrams",
                    " " + userId + "     " + geo + "    " + push + "    " + checkboxPrivate + "    " + radius
                )
                Log.e("respSendSettCode", response.code().toString() + "")
//                Log.e("respLocationStatus", " " + response.body()?.status)
                Log.e("respSendSettSt", " " + response.body().toString())
                Log.e("respSendSetterror", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
                    iSettingView.onSetSettingInfo(true, response)

                } else {
                    iSettingView.onSetSettingInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<SendUserGeneralSetting>, t: Throwable) {

                Log.e("responseFailure", " " + t.toString())

                homeActivity.showSnackBar(homeActivity.getResources().getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    fun getLanguagesInfo(
        userId: String

    ) {

        ProgressBarClass.progressBarCalling(homeActivity)

        apiInterface = APIClient.getClient(homeActivity).create(APIInterface::class.java)


        val call = apiInterface.getLanguagesData(userId)
        call.enqueue(object : Callback<LanguageData> {
            override fun onResponse(call: Call<LanguageData>, response: Response<LanguageData>) {


                Log.e("settingSetPrams", " " + userId)
                Log.e("respSendSettCode", response.code().toString() + "")
//                Log.e("respLocationStatus", " " + response.body()?.status)
                Log.e("respSendSettSt", " " + response.body().toString())
                Log.e("respSendSetterror", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
                    iSettingView.onGetLanguageInfo(true, response)

                } else {
                    iSettingView.onGetLanguageInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<LanguageData>, t: Throwable) {

                Log.e("responseFailure", " " + t.toString())

                homeActivity.showSnackBar(homeActivity.getResources().getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    fun setLanguageSelected(
        userId: String, languageId: String
    ) {

        ProgressBarClass.progressBarCalling(homeActivity)

        apiInterface = APIClient.getClient(homeActivity).create(APIInterface::class.java)


        val call = apiInterface.saveLanguagesData(userId,languageId)
        call.enqueue(object : Callback<SaveLanguageModel> {
            override fun onResponse(call: Call<SaveLanguageModel>, response: Response<SaveLanguageModel>) {


                Log.e("saveLangPrams", " " + userId)
                Log.e("respsaveLangCode", response.code().toString() + "")
//                Log.e("respLocationStatus", " " + response.body()?.status)
                Log.e("respsaveLangSt", " " + response.body().toString())
                Log.e("respsaveLangerror", " " + response.errorBody().toString())

                if (response.code().toString().equals("200")) {
                    iSettingView.onSaveLanguageInfo(true, response)

                } else {
                    iSettingView.onSaveLanguageInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<SaveLanguageModel>, t: Throwable) {

                Log.e("responseFailure", " " + t.toString())

                homeActivity.showSnackBar(homeActivity.getResources().getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


}
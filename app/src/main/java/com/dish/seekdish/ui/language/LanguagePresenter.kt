package com.dish.seekdish.ui.language

import android.util.Log
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LanguageData

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LanguagePresenter(private val iLanguageView: ILanguageView, val languageActivity: LanguageActivity) {

    //activity
    internal lateinit var apiInterface: APIInterface
    fun getLanguagesInfo(
        userId: String

    ) {

        ProgressBarClass.progressBarCalling(languageActivity)

        apiInterface = APIClient.getClient(languageActivity).create(APIInterface::class.java)


        val call = apiInterface.getLanguagesData("")
        call.enqueue(object : Callback<LanguageData> {
            override fun onResponse(call: Call<LanguageData>, response: Response<LanguageData>) {
                if (response.code().toString().equals("200")) {
                    iLanguageView.onGetLanguageInfo(true, response)

                } else {
                    iLanguageView.onGetLanguageInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
            }

            override fun onFailure(call: Call<LanguageData>, t: Throwable) {
                languageActivity.showSnackBar(languageActivity.getResources().getString(R.string.error_occured));
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


}
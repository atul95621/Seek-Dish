package com.dish.seekdish.ui.navDrawer.activities.presenter

import android.content.Context
import android.util.Log
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.activities.MyInformationActivity
import com.dish.seekdish.ui.navDrawer.activities.model.ProfileDataClass
import com.dish.seekdish.ui.navDrawer.activities.view.IMyInformationView
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LanguageData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoPresenter(private val iMyInformationView: IMyInformationView, val contxt: Context) {

    //activity
    var informationActivity = MyInformationActivity()

    internal lateinit var apiInterface: APIInterface


    fun updateInfo(
        firstName: RequestBody,
        lastName: RequestBody,
        username: RequestBody,
        addressLineOne: RequestBody,
        addressLineTwo: RequestBody,
        gender: RequestBody,
        bio: RequestBody,
        cityId: RequestBody,
        countryId: RequestBody,
        zipcode: RequestBody,
        bodyFat: RequestBody,
        weight: RequestBody,
        height: RequestBody,
        userId: RequestBody,
        profession_id: RequestBody,
        dob: RequestBody,
        part: MultipartBody.Part
    ) {

        ProgressBarClass.progressBarCalling(contxt)

        apiInterface = APIClient.getClient(contxt).create(APIInterface::class.java)


        val call = apiInterface.doUpdateProfileDetails(
            firstName,
            lastName,
            username,
            gender,
            addressLineOne,
            addressLineTwo,
            bio,
            cityId,
            countryId,
            zipcode,
            bodyFat,
            weight,
            height,
            userId,
            profession_id,
            dob,
            part
        )
        call.enqueue(object : Callback<ProfileDataClass> {
            override fun onResponse(
                call: Call<ProfileDataClass>,
                response: Response<ProfileDataClass>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {
                    iMyInformationView.onSetDataChanged(true, response)
                } else {
                    iMyInformationView.onSetDataChanged(false, response)
                }
            }

            override fun onFailure(call: Call<ProfileDataClass>, t: Throwable) {
//                informationActivity.showSnackBar(contxt.getResources().getString(R.string.error_occured));
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }



    fun updateInfoWithoutImage(
        firstName: RequestBody,
        lastName: RequestBody,
        username: RequestBody,
        addressLineOne: RequestBody,
        addressLineTwo: RequestBody,
        gender: RequestBody,
        bio: RequestBody,
        cityId: RequestBody,
        countryId: RequestBody,
        zipcode: RequestBody,
        bodyFat: RequestBody,
        weight: RequestBody,
        height: RequestBody,
        userId: RequestBody,
        profession_id: RequestBody,
        dob: RequestBody
    ) {
        ProgressBarClass.progressBarCalling(contxt)
        apiInterface = APIClient.getClient(contxt).create(APIInterface::class.java)
        val call = apiInterface.doUpdateProfileDetails(
            firstName,
            lastName,
            username,
            gender,
            addressLineOne,
            addressLineTwo,
            bio,
            cityId,
            countryId,
            zipcode,
            bodyFat,
            weight,
            height,
            userId,
            profession_id,
            dob
        )
        call.enqueue(object : Callback<ProfileDataClass> {
            override fun onResponse(
                call: Call<ProfileDataClass>,
                response: Response<ProfileDataClass>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {
                    iMyInformationView.onSetDataChanged(true, response)
                } else {
                    iMyInformationView.onSetDataChanged(false, response)
                }
            }

            override fun onFailure(call: Call<ProfileDataClass>, t: Throwable) {
//                informationActivity.showSnackBar(contxt.getResources().getString(R.string.error_occured));
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }
    fun getProfileInfo(
        userId: String
    ) {
        ProgressBarClass.progressBarCalling(contxt)
        apiInterface = APIClient.getClient(contxt).create(APIInterface::class.java)
        val call = apiInterface.getProfileData(userId)
        call.enqueue(object : Callback<ProfileDataClass> {
            override fun onResponse(
                call: Call<ProfileDataClass>,
                response: Response<ProfileDataClass>
            ) {
                 // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {
                    iMyInformationView.onGetProfileDetailsData(true, response)
                } else {
                    iMyInformationView.onGetProfileDetailsData(false, response)
                }
            }

            override fun onFailure(call: Call<ProfileDataClass>, t: Throwable) {
                informationActivity.utilities.showSnackBar(
                    contxt.getResources().getString(R.string.error_occured)
                );

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    fun getCountriesData(
        userId: String

    ) {

        ProgressBarClass.progressBarCalling(contxt)

        apiInterface = APIClient.getClient(contxt).create(APIInterface::class.java)


        val call = apiInterface.getCountriesData(userId)
        call.enqueue(object : Callback<LanguageData> {
            override fun onResponse(call: Call<LanguageData>, response: Response<LanguageData>) {

                if (response.code().toString().equals("200")) {
                    iMyInformationView.onGetCountryInfo(true, response)

                } else {
                    iMyInformationView.onGetCountryInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<LanguageData>, t: Throwable) {
                informationActivity.showSnackBar(
                    contxt.getResources().getString(R.string.error_occured)
                );

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    fun getCitiesData(
        userId: String, countryId: String
    ) {

        ProgressBarClass.progressBarCalling(contxt)

        apiInterface = APIClient.getClient(contxt).create(APIInterface::class.java)


        val call = apiInterface.getCitiesData(userId, countryId)
        call.enqueue(object : Callback<LanguageData> {
            override fun onResponse(call: Call<LanguageData>, response: Response<LanguageData>) {


                if (response.code().toString().equals("200")) {
                    iMyInformationView.onGetCitiesInfo(true, response)

                } else {
                    iMyInformationView.onGetCitiesInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<LanguageData>, t: Throwable) {
                informationActivity.showSnackBar(
                    contxt.getResources().getString(R.string.error_occured)
                );

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }
}
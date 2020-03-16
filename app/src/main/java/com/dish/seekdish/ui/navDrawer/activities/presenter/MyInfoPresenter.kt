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
        part: MultipartBody.Part
    ) {

        ProgressBarClass.progressBarCalling(contxt)

        apiInterface = APIClient.getClient(contxt).create(APIInterface::class.java)

        Log.e("updateInfoPrams", " " + firstName.toString() + lastName.toString() + username.toString()+ gender.toString() + addressLineOne.toString() + addressLineTwo.toString() + bio.toString() + cityId.toString() + countryId.toString() + zipcode.toString() + bodyFat.toString() + weight.toString() + height.toString() + userId.toString() + part.toString())

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
            part
        )
        call.enqueue(object : Callback<ProfileDataClass> {
            override fun onResponse(call: Call<ProfileDataClass>, response: Response<ProfileDataClass>) {

                Log.e("respUpdateMyInfoCode", response.code().toString() + "")
                Log.e("respUpdateMyInfoStatus", " " + response.body()?.status)
                Log.e("respUpdateMyInfoString", " " + response.body().toString())
                Log.e("respUpdateMyInfoperror", " " + response.code().toString())

                if (response.code().toString().equals("200")) {
//                    Log.e("respSignupCode", response.code().toString() + "")
                    iMyInformationView.onSetDataChanged(true, response)


                } else {
                    iMyInformationView.onSetDataChanged(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<ProfileDataClass>, t: Throwable) {

                Log.e("responUpdateFailure", " " + t.toString())

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

        Log.e("savedUserId", " " + userId);


        val call = apiInterface.getProfileData(userId)
        call.enqueue(object : Callback<ProfileDataClass> {
            override fun onResponse(call: Call<ProfileDataClass>, response: Response<ProfileDataClass>) {

                Log.e("respgetProfileCode", response.code().toString() + "")
                Log.e("respgetProfileStatus", " " + response.body()?.status)
                Log.e("respgetProfiletring", " " + response.body().toString())
                Log.e("respgetProfileerror", " " + response.code().toString())

                if (response.code().toString().equals("200")) {
                    Log.e("respSettPill",  "inside 200")
                    iMyInformationView.onGetProfileDetailsData(true, response)


                } else {
                    Log.e("respSettingPill",  "false 200")

                    iMyInformationView.onGetProfileDetailsData(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<ProfileDataClass>, t: Throwable) {

                Log.e("respgetProfileFailure", " " + t.toString())

                informationActivity.utilities.showSnackBar(contxt.getResources().getString(R.string.error_occured));

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


                Log.e("countryPrams", " " + userId)
                Log.e("respcountryCode", response.code().toString() + "")
//                Log.e("respLocationStatus", " " + response.body()?.status)
                Log.e("respcountrySt", " " + response.body().toString())
                Log.e("respcountryerror", " " + response.code().toString())

                if (response.code().toString().equals("200")) {
                    iMyInformationView.onGetCountryInfo(true, response)

                } else {
                    iMyInformationView.onGetCountryInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<LanguageData>, t: Throwable) {

                Log.e("responseFailure", " " + t.toString())

                informationActivity.showSnackBar(contxt.getResources().getString(R.string.error_occured));

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


                Log.e("CitiesPrams", " " + userId)
                Log.e("respCitiesCode", response.code().toString() + "")
//                Log.e("respLocationStatus", " " + response.body()?.status)
                Log.e("respCitiesSt", " " + response.body().toString())
                Log.e("respCitieserror", " " + response.code().toString())

                if (response.code().toString().equals("200")) {
                    iMyInformationView.onGetCitiesInfo(true, response)

                } else {
                    iMyInformationView.onGetCitiesInfo(false, response)
                }

                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


            }

            override fun onFailure(call: Call<LanguageData>, t: Throwable) {

                Log.e("responseFailure", " " + t.toString())

                informationActivity.showSnackBar(contxt.getResources().getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }
}
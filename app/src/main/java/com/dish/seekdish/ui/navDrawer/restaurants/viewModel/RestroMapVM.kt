package com.dish.seekdish.ui.navDrawer.restaurants.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.RestroMapModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestroMapVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getMapData: MutableLiveData<RestroMapModel> = MutableLiveData<RestroMapModel>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetRestroMapData(
        userId: String,
        longitude: String,
        latitude: String,
        radius: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getRestroMapData(
            userId,
            Constants.Latitude,
            Constants.Longitude,
            Constants.type,
            radius,
            Constants.device_token,
            Constants.deviceType,
            Constants.homePage,
            Constants.noOfItems
        )

        Log.e(
            "pramsGetTasteMeal",
            " " + userId + "    " + Constants.homePage + "lati   " + latitude + "    longi   " + longitude + "     radius   " + radius
        )

        call.enqueue(object : Callback<RestroMapModel> {
            override fun onResponse(call: Call<RestroMapModel>, response: Response<RestroMapModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getMapData.postValue(response.body())

                getMapData.value = response.body()
                Log.e("respoGetTasteMeal", response.body().toString())

            }

            override fun onFailure(call: Call<RestroMapModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetTasteMealFail", "failure")

                getMapData.postValue(null)


            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

package com.dish.seekdish.ui.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.dataModel.Location
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.ProximityDataClass
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TimeVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getTasteLiveData: MutableLiveData<ProximityDataClass> = MutableLiveData<ProximityDataClass>()
    var timeSearchData: MutableLiveData<ProximityDataClass> = MutableLiveData<ProximityDataClass>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()

    //This method is using Retrofit to get the JSON data from URL
    fun doGetTimeMealData(
        userId: String,
        pageNumber: String,
        longitude: String,
        latitude: String,
        radius: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getTimeMeal(
            userId,
           latitude,
            longitude,
            Constants.type,
            radius,
            Constants.device_token,
            Constants.deviceType,
            pageNumber,
            Constants.noOfMeals
        )

        Log.e(
            "pramsGetTasteMeal",
            " " + userId + "    " + pageNumber + "lati   " + latitude + "    longi   " + longitude + "     radius   " + radius
        )

        call.enqueue(object : Callback<ProximityDataClass> {
            override fun onResponse(call: Call<ProximityDataClass>, response: Response<ProximityDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getTasteLiveData.postValue(response.body())

                getTasteLiveData.value = response.body()

            }

            override fun onFailure(call: Call<ProximityDataClass>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                getTasteLiveData.postValue(null)


            }
        })
    }

    fun getHomeMealSearched(
        userId: String,
        pageNumber: String,
        longitude: String,
        latitude: String,
        radius: String,
        search_text:String
    ) {
        // making progress bar visible
//        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.getTimeSearch(
            userId,
            latitude,
            longitude,
            "1",// 2 is for time meal search,, NEW-> after time tab became menu, Michel changed api and type to 1
            radius,
            Constants.deviceType,
            pageNumber,
            Constants.noOfMeals,
            search_text
        )
        /*   Log.e(
               "pramsGetTasteMeal",
               " " + userId + "    " + pageNumber + "lati   " + latitude + "    longi   " + longitude + "     radius   " + radius
           )*/
        call.enqueue(object : Callback<ProximityDataClass> {
            override fun onResponse(call: Call<ProximityDataClass>, response: Response<ProximityDataClass>) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                timeSearchData.postValue(response.body())
                timeSearchData.value = response.body()
            }

            override fun onFailure(call: Call<ProximityDataClass>, t: Throwable) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                timeSearchData.postValue(null)
            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

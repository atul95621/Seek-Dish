package com.dish.seekdish.ui.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.dataModel.Location
import com.dish.seekdish.ui.home.dataModel.TimeFragDataClass
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TimeVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getTasteLiveData: MutableLiveData<TimeFragDataClass> = MutableLiveData<TimeFragDataClass>()
    var timeSearchData: MutableLiveData<TimeFragDataClass> = MutableLiveData<TimeFragDataClass>()


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
            Constants.Latitude,
            Constants.Longitude,
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

        call.enqueue(object : Callback<TimeFragDataClass> {
            override fun onResponse(call: Call<TimeFragDataClass>, response: Response<TimeFragDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getTasteLiveData.postValue(response.body())

                getTasteLiveData.value = response.body()
                Log.e("respoGetTasteMeal", response.body().toString())

            }

            override fun onFailure(call: Call<TimeFragDataClass>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetTasteMealFail", "failure")

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
            Constants.Latitude,
            Constants.Longitude,
            "2",// 2 is for time meal search
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
        call.enqueue(object : Callback<TimeFragDataClass> {
            override fun onResponse(call: Call<TimeFragDataClass>, response: Response<TimeFragDataClass>) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                timeSearchData.postValue(response.body())
                timeSearchData.value = response.body()
                Log.e("rspSearch", response.body().toString())
            }

            override fun onFailure(call: Call<TimeFragDataClass>, t: Throwable) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                Log.e("rspSearch fail", "failure")
                timeSearchData.postValue(null)
            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

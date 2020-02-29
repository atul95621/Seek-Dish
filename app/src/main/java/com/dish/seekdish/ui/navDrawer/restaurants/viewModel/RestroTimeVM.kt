package com.dish.seekdish.ui.navDrawer.restaurants.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.TimeRestroDataClass
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestroTimeVM :ViewModel(){


    //this is the data that we will fetch asynchronously
    var getTimeRestroLiveData: MutableLiveData<TimeRestroDataClass> = MutableLiveData<TimeRestroDataClass>()
    var searchTimeRestro: MutableLiveData<TimeRestroDataClass> = MutableLiveData<TimeRestroDataClass>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetTimeRestroData(
        userId: String,
        pageNumber: String,
        longitude: String,
        latitude: String,
        radius: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getTimeRest(
            userId,
            latitude,longitude,
            Constants.type,
            radius,
            Constants.device_token,
            Constants.deviceType,
            pageNumber,
            Constants.noOfMeals
        )
        call.enqueue(object : Callback<TimeRestroDataClass> {
            override fun onResponse(call: Call<TimeRestroDataClass>, response: Response<TimeRestroDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getTimeRestroLiveData.postValue(response.body())
                getTimeRestroLiveData.value = response.body()
                Log.e("respoGetTimeRestro", response.body().toString())

            }

            override fun onFailure(call: Call<TimeRestroDataClass>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetTimeRestFail", "failure")
                getTimeRestroLiveData.postValue(null)
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
        val call = api.getTimeRestSearch(
            userId,
            latitude, longitude,
            "2",// 2 is for time restro search
            radius,
            pageNumber,
            Constants.noOfMeals,
            search_text
        )
        call.enqueue(object : Callback<TimeRestroDataClass> {
            override fun onResponse(call: Call<TimeRestroDataClass>, response: Response<TimeRestroDataClass>) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                searchTimeRestro.postValue(response.body())
                searchTimeRestro.value = response.body()
                Log.e("rspSearch", response.body().toString())
            }

            override fun onFailure(call: Call<TimeRestroDataClass>, t: Throwable) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                Log.e("rspSearch fail", "failure")
                searchTimeRestro.postValue(null)
            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }
}
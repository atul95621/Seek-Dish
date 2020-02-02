package com.dish.seekdish.ui.navDrawer.restaurants.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.ProximityDataClass
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProximityVM :ViewModel(){


    //this is the data that we will fetch asynchronously
    var getProxiRestroLiveData: MutableLiveData<ProximityDataClass> = MutableLiveData<ProximityDataClass>()
    var getProxiSearchData: MutableLiveData<ProximityDataClass> = MutableLiveData<ProximityDataClass>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetProxiRestroData(
        userId: String,
        pageNumber: String,
        longitude: String,
        latitude: String,
        radius: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getProxiRest(
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
            "pramsGetProxiyRestro",
            " " + userId + "    " + pageNumber + "lati   " + latitude + "    longi   " + longitude + "     radius   " + radius
        )

        call.enqueue(object : Callback<ProximityDataClass> {
            override fun onResponse(call: Call<ProximityDataClass>, response: Response<ProximityDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getProxiRestroLiveData.postValue(response.body())

                getProxiRestroLiveData.value = response.body()
                Log.e("respoGetProxiyRestro", response.body().toString())

            }

            override fun onFailure(call: Call<ProximityDataClass>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetProxiyRestFail", "failure")

                getProxiRestroLiveData.postValue(null)


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
        val call = api.getRestaurantSearch(
            userId,
            Constants.Latitude,
            Constants.Longitude,
            "1",// 1 is for proximity restro search
            radius,
            pageNumber,
            Constants.noOfMeals,
            search_text
        )
        call.enqueue(object : Callback<ProximityDataClass> {
            override fun onResponse(call: Call<ProximityDataClass>, response: Response<ProximityDataClass>) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getProxiSearchData.postValue(response.body())
                getProxiSearchData.value = response.body()
                Log.e("rspSearch", response.body().toString())
            }

            override fun onFailure(call: Call<ProximityDataClass>, t: Throwable) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                Log.e("rspSearch fail", "failure")
                getProxiSearchData.postValue(null)
            }
        })
    }



    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }
}
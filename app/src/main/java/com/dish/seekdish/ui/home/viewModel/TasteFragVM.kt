package com.dish.seekdish.ui.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.dataModel.Location
import com.dish.seekdish.ui.home.dataModel.TasteFragDataClass
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasteFragVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getTasteLiveData: MutableLiveData<TasteFragDataClass> = MutableLiveData<TasteFragDataClass>()
    var getLiveLocation: MutableLiveData<Location> = MutableLiveData<Location>()
    var tasteSearchData: MutableLiveData<TasteFragDataClass> = MutableLiveData<TasteFragDataClass>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetTasteMealData(
        userId: String,
        pageNumber: String,
        longitude: String,
        latitude: String,
        radius: String
    ) {
        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.getTasteMealData(
            userId,
            Constants.Latitude,
            Constants.Longitude,
            "1",
            radius,
            Constants.device_token,
            Constants.deviceType,
            pageNumber,
            Constants.noOfMeals
        )
     /*   Log.e(
            "pramsGetTasteMeal",
            " " + userId + "    " + pageNumber + "lati   " + latitude + "    longi   " + longitude + "     radius   " + radius
        )*/
        call.enqueue(object : Callback<TasteFragDataClass> {
            override fun onResponse(call: Call<TasteFragDataClass>, response: Response<TasteFragDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getTasteLiveData.postValue(response.body())
                getTasteLiveData.value = response.body()
                Log.e("respoGetTasteMeal", response.body().toString())
            }

            override fun onFailure(call: Call<TasteFragDataClass>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetTasteMealFail", "failure")
                getTasteLiveData.postValue(null)
            }
        })
    }


    fun getCurrentLocation(userId: String, longitude: String, latitude: String) {
        // making progress bar visible
//        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getLocation(userId, longitude,latitude)

        Log.e(
            "pramsLiveLocation",
            " " + userId + "  "+"lati   " + latitude + "    longi   " + longitude
        )

        call.enqueue(object : Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getLiveLocation.postValue(response.body())

                getLiveLocation.value = response.body()
                Log.e("respoLiveLocation", response.body().toString())

            }

            override fun onFailure(call: Call<Location>, t: Throwable) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                Log.e("respoLiveLocation", "failure")

                getLiveLocation.postValue(null)


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
        val call = api.getTasteSearch(
            userId,
            Constants.Latitude,
            Constants.Longitude,
            "1",  // 1 is for taste meal search
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
        call.enqueue(object : Callback<TasteFragDataClass> {
            override fun onResponse(call: Call<TasteFragDataClass>, response: Response<TasteFragDataClass>) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                tasteSearchData.postValue(response.body())
                tasteSearchData.value = response.body()
                Log.e("rspSearch", response.body().toString())
            }

            override fun onFailure(call: Call<TasteFragDataClass>, t: Throwable) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                Log.e("rspSearch fail", "failure")
                tasteSearchData.postValue(null)
            }
        })
    }

    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

package com.dish.seekdish.ui.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.dataModel.Location
import com.dish.seekdish.ui.home.dataModel.TasteFragDataClass
import com.dish.seekdish.ui.home.fragments.TasteFragment
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasteFragVM() : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getTasteLiveData: MutableLiveData<TasteFragDataClass> = MutableLiveData<TasteFragDataClass>()
    var getLiveLocation: MutableLiveData<CancelReModel> = MutableLiveData<CancelReModel>()
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
            latitude,
            longitude,
            "1",
            radius,
            Constants.device_token,
            Constants.deviceType,
            pageNumber,
            Constants.noOfMeals
        )
        call.enqueue(object : Callback<TasteFragDataClass> {
            override fun onResponse(call: Call<TasteFragDataClass>, response: Response<TasteFragDataClass>) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getTasteLiveData.postValue(response.body())
                getTasteLiveData.value = response.body()
            }

            override fun onFailure(call: Call<TasteFragDataClass>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getTasteLiveData.postValue(null)
            }
        })
    }


    fun getCurrentLocation(userId: String, longitude: String, latitude: String) {
        // making progress bar visible
//        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.getLocation(userId, longitude,latitude)
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(call: Call<CancelReModel>, response: Response<CancelReModel>) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getLiveLocation.postValue(response.body())
                getLiveLocation.value = response.body()
            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
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
           latitude,
           longitude,
            "1",  // 1 is for taste meal search
            radius,
            Constants.deviceType,
            pageNumber,
            Constants.noOfMeals,
            search_text
            )
        call.enqueue(object : Callback<TasteFragDataClass> {
            override fun onResponse(call: Call<TasteFragDataClass>, response: Response<TasteFragDataClass>) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                tasteSearchData.postValue(response.body())
                tasteSearchData.value = response.body()
            }

            override fun onFailure(call: Call<TasteFragDataClass>, t: Throwable) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                tasteSearchData.postValue(null)
            }
        })
    }

    fun isLoadingObservable(): Observable<Boolean> {
        return isLoadingSubject
    }

}

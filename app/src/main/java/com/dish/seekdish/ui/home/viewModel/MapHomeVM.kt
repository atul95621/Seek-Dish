package com.dish.seekdish.ui.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.dataModel.MapHomeModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapHomeVM : ViewModel() {
    //this is the data that we will fetch asynchronously
    var getMapData: MutableLiveData<MapHomeModel> = MutableLiveData<MapHomeModel>()
    val isLoadingSubject = BehaviorSubject.create<Boolean>()

    //This method is using Retrofit to get the JSON data from URL
    fun doGetMapData(
        userId: String,
        longitude: String,
        latitude: String,
        radius: String
    ) {
        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.getHomeMapData(
            userId,
            latitude,
            longitude,
            Constants.type,
            radius,
            Constants.device_token,
            Constants.deviceType,
            Constants.homePage,
            Constants.noOfMapItems
        )
        call.enqueue(object : Callback<MapHomeModel> {
            override fun onResponse(call: Call<MapHomeModel>, response: Response<MapHomeModel>) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getMapData.postValue(response.body())
                getMapData.value = response.body()
            }

            override fun onFailure(call: Call<MapHomeModel>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getMapData.postValue(null)
            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

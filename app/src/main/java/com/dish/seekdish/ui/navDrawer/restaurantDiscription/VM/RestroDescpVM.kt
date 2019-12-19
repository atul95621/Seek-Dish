package com.dish.seekdish.ui.navDrawer.restaurantDiscription.VM

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescpModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestroDescpVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var geRestroDetailLiveData: MutableLiveData<RestroDescpModel> = MutableLiveData<RestroDescpModel>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetRestroDetails(
        userId: String,
        restaurant_id: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getRestroDetails(userId,restaurant_id)

        call.enqueue(object : Callback<RestroDescpModel> {
            override fun onResponse(call: Call<RestroDescpModel>, response: Response<RestroDescpModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                geRestroDetailLiveData.postValue(response.body())

                geRestroDetailLiveData.value = response.body()
                Log.e("respGetDetails", response.body().toString())

            }

            override fun onFailure(call: Call<RestroDescpModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respGetDetailsFail", "failure")

                geRestroDetailLiveData.postValue(null)
            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {
        return isLoadingSubject
    }

}

package com.dish.seekdish.ui.navDrawer.restaurantDiscription.VM

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.dishDescription.model.CallCountModel
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescpModel
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestroDescpVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var geRestroDetailLiveData: MutableLiveData<RestroDescpModel> = MutableLiveData<RestroDescpModel>()
    var getAddAlertLiveData: MutableLiveData<CancelReModel> = MutableLiveData<CancelReModel>()
    var getCallCountLiveData: MutableLiveData<CallCountModel> = MutableLiveData<CallCountModel>()


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


    fun addAlertApi(
        userId: String,
        restaurant_id: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.doAddAlert(userId,restaurant_id)

        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(call: Call<CancelReModel>, response: Response<CancelReModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
//                getAddAlertLiveData.postValue(response.body())

                getAddAlertLiveData.value = response.body()
                Log.e("respGetDetails", response.body().toString())

            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respGetDetailsFail", "failure")

                getAddAlertLiveData.postValue(null)
            }
        })
    }

    fun postCallCount(
        userId: String,
        restaurantId: String,
        date: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.postCountCall(userId,restaurantId,date)
        call.enqueue(object : Callback<CallCountModel> {
            override fun onResponse(call: Call<CallCountModel>, response: Response<CallCountModel>) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getCallCountLiveData.postValue(response.body())
//                getFollowingReqLiveData.value = response.body()
                Log.e("respGetDetails", response.body().toString())
            }

            override fun onFailure(call: Call<CallCountModel>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respGetDetailsFail", "failure")
                getCallCountLiveData.postValue(null)
            }
        })
    }

    fun isLoadingObservable(): Observable<Boolean> {
        return isLoadingSubject
    }

}

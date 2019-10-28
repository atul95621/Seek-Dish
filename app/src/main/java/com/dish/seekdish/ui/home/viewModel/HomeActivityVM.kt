package com.dish.seekdish.ui.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.dataModel.FilterDataModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivityVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getFilterLiveData: MutableLiveData<FilterDataModel> = MutableLiveData<FilterDataModel>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetFilterData(userId: String, languageId: String) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getFilterData(userId,languageId)

//        Log.e("pramsGetLiked"," "+userId+"    "+questionId)

        call.enqueue(object : Callback<FilterDataModel> {
            override fun onResponse(call: Call<FilterDataModel>, response: Response<FilterDataModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getFilterLiveData.postValue(response.body())

                getFilterLiveData.value = response.body()
                Log.e("respFilter", response.body().toString())

            }

            override fun onFailure(call: Call<FilterDataModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respFilterFail", "failure")

                getFilterLiveData.postValue(null)


            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}
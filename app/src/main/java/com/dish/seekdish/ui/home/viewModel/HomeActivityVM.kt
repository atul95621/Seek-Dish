package com.dish.seekdish.ui.home.viewModel

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.BuildConfig
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.dataModel.FilterDataModel
import com.dish.seekdish.ui.home.dataModel.SaveFilterModel
import com.dish.seekdish.ui.login.CheckUpdateModel
import com.dish.seekdish.ui.login.NotificationQtyModel
import com.dish.seekdish.ui.navDrawer.dishDescription.model.AddTodoModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivityVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getFilterLiveData: MutableLiveData<FilterDataModel> = MutableLiveData<FilterDataModel>()
    var getLogoutLiveData: MutableLiveData<AddTodoModel> = MutableLiveData<AddTodoModel>()
    var saveFilterLiveData: MutableLiveData<SaveFilterModel> = MutableLiveData<SaveFilterModel>()
    var getUpdateLiveData: MutableLiveData<CheckUpdateModel> = MutableLiveData<CheckUpdateModel>()
    var getNotificationQtyLiveData: MutableLiveData<NotificationQtyModel> = MutableLiveData<NotificationQtyModel>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetFilterData(userId: String, languageId: String) {
        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.getFilterData(userId, languageId)
//        Log.e("pramsGetLiked"," "+userId+"    "+questionId)
        call.enqueue(object : Callback<FilterDataModel> {
            override fun onResponse(
                call: Call<FilterDataModel>,
                response: Response<FilterDataModel>
            ) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getFilterLiveData.postValue(response.body())
                getFilterLiveData.value = response.body()
            }

            override fun onFailure(call: Call<FilterDataModel>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getFilterLiveData.postValue(null)
            }
        })
    }

    fun doSaveFilterData(
        budgetItems: String,
        serviceSpeedItems: String,
        mealItems: String,
        compatIntolerItems: String,
        restroAmbianceItems: String,
        restroSpecialItems: String,
        compAmbianceItems: String,
        additonalItems: String,
        seasonlityItems: String,
        userId: String,
        consider_my_profile:String
    ) {
        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.saveFilterData(
            userId,
            budgetItems,
            serviceSpeedItems,
            mealItems,
            compatIntolerItems,
            restroSpecialItems,
            restroAmbianceItems,
            compAmbianceItems,
            additonalItems,
            seasonlityItems,
            consider_my_profile
        )
        call.enqueue(object : Callback<SaveFilterModel> {
            override fun onResponse(
                call: Call<SaveFilterModel>,
                response: Response<SaveFilterModel>
            ) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                saveFilterLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<SaveFilterModel>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                saveFilterLiveData.postValue(null)
            }
        })
    }


    //This method is using Retrofit to get the JSON data from URL
    fun doLogout(userId: String) {
        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.logout(userId)
        call.enqueue(object : Callback<AddTodoModel> {
            override fun onResponse(call: Call<AddTodoModel>, response: Response<AddTodoModel>) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getLogoutLiveData.postValue(response.body())
                getLogoutLiveData.value = response.body()
            }
            override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getLogoutLiveData.postValue(null)
            }
        })
    }

    //This method is using Retrofit to get the JSON data from URL
    fun checkUpdate() {
        // making progress bar visible
//        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.checkUpdate()
        call.enqueue(object : Callback<CheckUpdateModel> {
            override fun onResponse(call: Call<CheckUpdateModel>, response: Response<CheckUpdateModel>) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getUpdateLiveData.postValue(response.body())
                getUpdateLiveData.value = response.body()
            }
            override fun onFailure(call: Call<CheckUpdateModel>, t: Throwable) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                getUpdateLiveData.postValue(null)
            }
        })
    }


    //ntoification count
    fun notificationCount(userId: String) {
        // making progress bar visible
//        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.notificationCount(userId)
        call.enqueue(object : Callback<NotificationQtyModel> {
            override fun onResponse(call: Call<NotificationQtyModel>, response: Response<NotificationQtyModel>) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getNotificationQtyLiveData.postValue(response.body())
                getNotificationQtyLiveData.value = response.body()
            }
            override fun onFailure(call: Call<NotificationQtyModel>, t: Throwable) {
                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                getNotificationQtyLiveData.postValue(null)
            }
        })
    }

    fun isLoadingObservable(): Observable<Boolean> {
        return isLoadingSubject
    }

}
package com.dish.seekdish.ui.navDrawer.myFavourite.VM

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.toDo.list.DeleteTodoList
import com.dish.seekdish.ui.navDrawer.toDo.list.ListTodoDataClass
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getFavoriteLiveData: MutableLiveData<ListTodoDataClass> = MutableLiveData<ListTodoDataClass>()
    var getFavDeleteLiveData: MutableLiveData<DeleteTodoList> = MutableLiveData<DeleteTodoList>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetFavList(
        userId: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getFavoriteList(
            userId
        )

        call.enqueue(object : Callback<ListTodoDataClass> {
            override fun onResponse(call: Call<ListTodoDataClass>, response: Response<ListTodoDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getFavoriteLiveData.postValue(response.body())

                getFavoriteLiveData.value = response.body()
                Log.e("respoGetFav", response.body().toString())

            }

            override fun onFailure(call: Call<ListTodoDataClass>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetFavFail", "failure")

                getFavoriteLiveData.postValue(null)


            }
        })
    }


    //This method is using Retrofit to get the JSON data from URL
    fun deleteFavList(
        userId: String,
        meal_id: String,
        restro_id: String

    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.deleteFavorite(
            userId,
            meal_id,
            restro_id
        )

        call.enqueue(object : Callback<DeleteTodoList> {
            override fun onResponse(call: Call<DeleteTodoList>, response: Response<DeleteTodoList>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getFavDeleteLiveData.postValue(response.body())

                getFavDeleteLiveData.value = response.body()

                Log.e("respoDeleteMealPrams", meal_id + "    " + restro_id + "    user" + userId)

                Log.e("respoGetDel", response.body().toString())

            }

            override fun onFailure(call: Call<DeleteTodoList>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetDelFail", "failure")

                getFavDeleteLiveData.postValue(null)


            }
        })
    }

    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

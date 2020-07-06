package com.dish.seekdish.ui.navDrawer.dishDescription.VM

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.dishDescription.model.AddTodoModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatingCommentVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getRatingLiveData: MutableLiveData<AddTodoModel> = MutableLiveData<AddTodoModel>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()

    //This method is using Retrofit to get the JSON data from URL
    fun postCommentRating(
        userId: RequestBody,
        taste: RequestBody,
        presentation: RequestBody,
        texture: RequestBody,
        ordor: RequestBody,
        service: RequestBody,
        decor: RequestBody,
        cleaniness: RequestBody,
        ambience: RequestBody,
        comment: RequestBody,
        annony: RequestBody,
        pathImage1: MultipartBody.Part,
        pathImage2: MultipartBody.Part,
        mealId: RequestBody,
        restaurantId: RequestBody
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.doPostComment(
            userId,
            mealId,
            taste,
            presentation,
            texture,
            ordor,
            comment,
            annony,
            restaurantId,
            service,
            decor,
            cleaniness,
            ambience,
            pathImage1,
            pathImage2

        )

        call.enqueue(object : Callback<AddTodoModel> {
            override fun onResponse(call: Call<AddTodoModel>, response: Response<AddTodoModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getRatingLiveData.postValue(response.body())

//                getRatingLiveData.value = response.body()

            }

            override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getRatingLiveData.postValue(null)
            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {
        return isLoadingSubject
    }

}

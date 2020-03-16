package com.dish.seekdish.ui.navDrawer.settings.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.settings.dataModel.DisLikeDataClass
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LikeDataClass
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LikedIngredientsSaved
import com.google.android.gms.common.api.Api
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisLikeVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getDisLikedLiveData: MutableLiveData<DisLikeDataClass> = MutableLiveData<DisLikeDataClass>()
    var saveDisLikedLiveData: MutableLiveData<LikedIngredientsSaved> = MutableLiveData<LikedIngredientsSaved>()
    var searchDislIngreLiveData: MutableLiveData<DisLikeDataClass> = MutableLiveData<DisLikeDataClass>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetDisLikedIngredients(userId: String,pageNumber: String) {

        // making progress bar visible
        isLoadingSubject.onNext(true)



        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getDisLikedIngredients(userId,pageNumber,"400")

//        Log.e("pramsGetLiked"," "+userId+"    "+questionId)

        call.enqueue(object : Callback<DisLikeDataClass> {
            override fun onResponse(call: Call<DisLikeDataClass>, response: Response<DisLikeDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getDisLikedLiveData.postValue(response.body())

                getDisLikedLiveData.value = response.body()
                Log.e("respoGetsDisLike", response.body().toString())

            }

            override fun onFailure(call: Call<DisLikeDataClass>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetsDisLikeFail", "failure")

                getDisLikedLiveData.postValue(null)


            }
        })
    }



    fun doSaveDisLikedIngredients(userId: String,ingredients: String) {

        // making progress bar visible
        isLoadingSubject.onNext(true)



        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.postSavedDisLikeIngre(userId,ingredients)

//        Log.e("pramsGetLiked"," "+userId+"    "+questionId)

        call.enqueue(object : Callback<LikedIngredientsSaved> {
            override fun onResponse(call: Call<LikedIngredientsSaved>, response: Response<LikedIngredientsSaved>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                saveDisLikedLiveData.postValue(response.body())

//                saveDisLikedLiveData.value = response.body()
                Log.e("respoGetDisLiked", response.body().toString())

            }

            override fun onFailure(call: Call<LikedIngredientsSaved>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetDisLikedFail", "failure")

                saveDisLikedLiveData.postValue(null)


            }
        })
    }

    //to searched like ingredient
    fun doGetSearchIngred(userId: String, searchedTerm: String) {

        // making progress bar visible
//        isLoadingSubject.onNext(true)

        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getSearchDisLikedIngredients(userId,"1","1000",searchedTerm)

//        Log.e("pramsGetLiked"," "+userId+"    "+questionId)

        call.enqueue(object : Callback<DisLikeDataClass> {
            override fun onResponse(call: Call<DisLikeDataClass>, response: Response<DisLikeDataClass>) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                searchDislIngreLiveData.postValue(response.body())

                searchDislIngreLiveData.value = response.body()
                Log.e("respoGetssearchDislike", response.body().toString())

            }

            override fun onFailure(call: Call<DisLikeDataClass>, t: Throwable) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                Log.e("respoGetsearchDisLiFail", "failure")

                searchDislIngreLiveData.postValue(null)


            }
        })
    }



    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

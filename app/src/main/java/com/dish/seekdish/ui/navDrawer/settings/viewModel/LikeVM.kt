package com.dish.seekdish.ui.navDrawer.settings.viewModel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LikeDataClass
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LikedIngredientsSaved
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getLikedLiveData: MutableLiveData<LikeDataClass> = MutableLiveData<LikeDataClass>()
    var saveLikedLiveData: MutableLiveData<LikedIngredientsSaved> = MutableLiveData<LikedIngredientsSaved>()
    var getSearchLikedLiveData: MutableLiveData<LikeDataClass> = MutableLiveData<LikeDataClass>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetLikedIngredients(userId: String,pageNumber: String) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getLikedIngredients(userId,pageNumber,"400")

        Log.e("pramsGetLiked"," "+userId+"    "+pageNumber)

        call.enqueue(object : Callback<LikeDataClass> {
            override fun onResponse(call: Call<LikeDataClass>, response: Response<LikeDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getLikedLiveData.postValue(response.body())

                getLikedLiveData.value = response.body()
                Log.e("respoGetsLike", response.body().toString())

            }

            override fun onFailure(call: Call<LikeDataClass>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetsLikeFail", "failure")

                getLikedLiveData.postValue(null)


            }
        })
    }

    //to searched like ingredient
    fun doGetSearchedIngredients(userId: String, searchedTerm: String) {

        // making progress bar visible
//        isLoadingSubject.onNext(true)



        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getSearchLikedIngredients(userId,"1","1000",searchedTerm)

//        Log.e("pramsGetLiked"," "+userId+"    "+questionId)

        call.enqueue(object : Callback<LikeDataClass> {
            override fun onResponse(call: Call<LikeDataClass>, response: Response<LikeDataClass>) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getSearchLikedLiveData.postValue(response.body())

                getSearchLikedLiveData.value = response.body()
                Log.e("respoGetssearchLike", response.body().toString())

            }

            override fun onFailure(call: Call<LikeDataClass>, t: Throwable) {

                // making progress bar invisible
//                isLoadingSubject.onNext(false)
                Log.e("respoGetssearchLikeFail", "failure")

                getSearchLikedLiveData.postValue(null)


            }
        })
    }


    fun doSaveLikedIngredients(userId: String,ingredients: String) {

        // making progress bar visible
        isLoadingSubject.onNext(true)



        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.postSavedLikeIngre(userId,ingredients)

        Log.e("pramsSaveLiked"," "+userId+"    "+ingredients)

        call.enqueue(object : Callback<LikedIngredientsSaved> {
            override fun onResponse(call: Call<LikedIngredientsSaved>, response: Response<LikedIngredientsSaved>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                saveLikedLiveData.postValue(response.body())

                saveLikedLiveData.value = response.body()
                Log.e("respoGetLiked", response.body().toString())

            }

            override fun onFailure(call: Call<LikedIngredientsSaved>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetLikedFail", "failure")

                saveLikedLiveData.postValue(null)


            }
        })
    }


    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

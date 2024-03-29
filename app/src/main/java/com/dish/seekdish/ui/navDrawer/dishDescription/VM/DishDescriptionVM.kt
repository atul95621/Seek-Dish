package com.dish.seekdish.ui.navDrawer.dishDescription.VM

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.dishDescription.model.AddTodoModel
import com.dish.seekdish.ui.navDrawer.dishDescription.model.CallCountModel
import com.dish.seekdish.ui.navDrawer.dishDescription.model.DishDescpModel
import com.dish.seekdish.ui.navDrawer.dishDescription.opinion.CommentDetailModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DishDescriptionVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getDishDetailLiveData: MutableLiveData<DishDescpModel> = MutableLiveData<DishDescpModel>()
    var getAddTodoLiveData: MutableLiveData<AddTodoModel> = MutableLiveData<AddTodoModel>()
    var getAddFavouriteLiveData: MutableLiveData<AddTodoModel> = MutableLiveData<AddTodoModel>()
    var getFriendReqLiveData: MutableLiveData<AddTodoModel> = MutableLiveData<AddTodoModel>()
    var getFollowingReqLiveData: MutableLiveData<AddTodoModel> = MutableLiveData<AddTodoModel>()

    var getCommentLiveData: MutableLiveData<CommentDetailModel> = MutableLiveData<CommentDetailModel>()
    var getCallCountLiveData: MutableLiveData<CallCountModel> = MutableLiveData<CallCountModel>()

    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetMealDetails(
        userId: String,
        meal_id: String,
        restaurant_id: String,
        longitude: String,
        latitude: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.getMealDetails(userId, meal_id, restaurant_id, longitude, latitude)
        call.enqueue(object : Callback<DishDescpModel> {
            override fun onResponse(call: Call<DishDescpModel>, response: Response<DishDescpModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getDishDetailLiveData.postValue(response.body())
                getDishDetailLiveData.value = response.body()

            }

            override fun onFailure(call: Call<DishDescpModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getDishDetailLiveData.postValue(null)
            }
        })
    }


    //This method is using Retrofit to get the JSON data from URL
    fun getAddTodostat(
        userId: String,
        meal_id: String,
        restaurant_id: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.addtoTodo(userId, meal_id, restaurant_id)

        call.enqueue(object : Callback<AddTodoModel> {
            override fun onResponse(call: Call<AddTodoModel>, response: Response<AddTodoModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getAddTodoLiveData.postValue(response.body())
//                getAddTodoLiveData.value = response.body()
            }

            override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                getAddTodoLiveData.postValue(null)
            }
        })
    }


    //This method is using Retrofit to get the JSON data from URL
    fun getAddFavouritestat(
        userId: String,
        meal_id: String,
        restaurant_id: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.addFavorite(userId, meal_id, restaurant_id)

        call.enqueue(object : Callback<AddTodoModel> {
            override fun onResponse(call: Call<AddTodoModel>, response: Response<AddTodoModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getAddFavouriteLiveData.postValue(response.body())
//                getAddFavouriteLiveData.value = response.body()

            }

            override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getAddFavouriteLiveData.postValue(null)
            }
        })
    }


    //This method is using Retrofit to get the JSON data from URL
    fun doSendFriendRequest(
        senderId: String,
        receiverId: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.sendFriendRequest(senderId, receiverId)

        call.enqueue(object : Callback<AddTodoModel> {
            override fun onResponse(call: Call<AddTodoModel>, response: Response<AddTodoModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getFriendReqLiveData.postValue(response.body())
                getFriendReqLiveData.value = response.body()

            }

            override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getFriendReqLiveData.postValue(null)
            }
        })
    }


    fun doSendFollwoingRequest(
        senderId: String,
        receiverId: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.sendFollowingRequest(senderId, receiverId)
        call.enqueue(object : Callback<AddTodoModel> {
            override fun onResponse(call: Call<AddTodoModel>, response: Response<AddTodoModel>) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getFollowingReqLiveData.postValue(response.body())
//                getFollowingReqLiveData.value = response.body()
            }

            override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getFollowingReqLiveData.postValue(null)
            }
        })
    }


    fun getComments(
        userId: String,
        mealId: String,
        restaurantId: String,
        commentId: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)
        val call = api.getcomment(userId,mealId,restaurantId,commentId)
        call.enqueue(object : Callback<CommentDetailModel> {
            override fun onResponse(call: Call<CommentDetailModel>, response: Response<CommentDetailModel>) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                //finally we are setting the list to our MutableLiveData
                getCommentLiveData.postValue(response.body())
//                getFollowingReqLiveData.value = response.body()
            }

            override fun onFailure(call: Call<CommentDetailModel>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getCommentLiveData.postValue(null)
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
            }

            override fun onFailure(call: Call<CallCountModel>, t: Throwable) {
                // making progress bar invisible
                isLoadingSubject.onNext(false)
                getCallCountLiveData.postValue(null)
            }
        })
    }
    fun isLoadingObservable(): Observable<Boolean> {
        return isLoadingSubject
    }

}

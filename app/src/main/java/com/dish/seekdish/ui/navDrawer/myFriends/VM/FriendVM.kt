package com.dish.seekdish.ui.navDrawer.myFriends.VM

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.dishDescription.model.AddTodoModel
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.FriendDataModel
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getFriendLiveData: MutableLiveData<FriendDataModel> = MutableLiveData<FriendDataModel>()
    var getRemoveFrndLiveData: MutableLiveData<CancelReModel> = MutableLiveData<CancelReModel>()
    var getRemoveFollwLiveData: MutableLiveData<CancelReModel> = MutableLiveData<CancelReModel>()
    var getFollowingReqLiveData: MutableLiveData<AddTodoModel> = MutableLiveData<AddTodoModel>()
    var getFriendReqLiveData: MutableLiveData<AddTodoModel> = MutableLiveData<AddTodoModel>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetFriends(
        userId: String
    ) {
        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getFriendList(
            userId
        )

        call.enqueue(object : Callback<FriendDataModel> {
            override fun onResponse(call: Call<FriendDataModel>, response: Response<FriendDataModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getFriendLiveData.postValue(response.body())
                getFriendLiveData.value = response.body()
                Log.e("respoGetFav", response.body().toString())

            }

            override fun onFailure(call: Call<FriendDataModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetFavFail", "failure")
                getFriendLiveData.postValue(null)


            }
        })
    }

    fun doGetMutualFriends(
        myUserId: String,
        friendUserId:String
    ) {
        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getMutualFriendList(
            myUserId,
            friendUserId

        )

        call.enqueue(object : Callback<FriendDataModel> {
            override fun onResponse(call: Call<FriendDataModel>, response: Response<FriendDataModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getFriendLiveData.postValue(response.body())
                getFriendLiveData.value = response.body()
                Log.e("respoGetFav", response.body().toString())

            }

            override fun onFailure(call: Call<FriendDataModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetFavFail", "failure")
                getFriendLiveData.postValue(null)


            }
        })
    }

    fun doRemoveFriend(
        userId: String,
        toBeRemovedUserId: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.doDeleteFriend(
            userId,toBeRemovedUserId
        )

        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(call: Call<CancelReModel>, response: Response<CancelReModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getRemoveFrndLiveData.postValue(response.body())
//                getRemoveFrndLiveData.value = response.body()
                Log.e("respoGetFav", response.body().toString())

            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetFavFail", "failure")
                getRemoveFrndLiveData.postValue(null)
            }
        })
    }


    fun doRemoveFollowing(
        userId: String,
        toBeRemovedUserId: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)
        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.doDeleteFollower(
            userId,toBeRemovedUserId
        )

        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(call: Call<CancelReModel>, response: Response<CancelReModel>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getRemoveFollwLiveData.postValue(response.body())
//                getRemoveFollwLiveData.value = response.body()
                Log.e("respoGetFav", response.body().toString())

            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetFavFail", "failure")
                getRemoveFollwLiveData.postValue(null)
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
                Log.e("respGetDetails", response.body().toString())

            }

            override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respGetDetailsFail", "failure")

                getFollowingReqLiveData.postValue(null)
            }
        })
    }

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

//                getFriendReqLiveData.value = response.body()
                Log.e("respGetDetails", response.body().toString())

            }

            override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respGetDetailsFail", "failure")

                getFriendReqLiveData.postValue(null)
            }
        })
    }

    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

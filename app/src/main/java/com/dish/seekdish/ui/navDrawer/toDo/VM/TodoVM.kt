package com.dish.seekdish.ui.navDrawer.toDo.VM

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dish.seekdish.Constants
import com.dish.seekdish.retrofit.APIClientMvvm
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.toDo.list.DeleteTodoList
import com.dish.seekdish.ui.navDrawer.toDo.list.ListTodoDataClass
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoVM : ViewModel() {

    //this is the data that we will fetch asynchronously
    var getTodoLiveData: MutableLiveData<ListTodoDataClass> = MutableLiveData<ListTodoDataClass>()
    var getTodoDeleteLiveData: MutableLiveData<DeleteTodoList> = MutableLiveData<DeleteTodoList>()


    val isLoadingSubject = BehaviorSubject.create<Boolean>()


    //This method is using Retrofit to get the JSON data from URL
    fun doGetTodoList(
        userId: String
    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.getTodoList(
            userId)

        call.enqueue(object : Callback<ListTodoDataClass> {
            override fun onResponse(call: Call<ListTodoDataClass>, response: Response<ListTodoDataClass>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getTodoLiveData.postValue(response.body())

                getTodoLiveData.value = response.body()
                Log.e("respoGetTasteMeal", response.body().toString())

            }

            override fun onFailure(call: Call<ListTodoDataClass>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoGetTasteMealFail", "failure")

                getTodoLiveData.postValue(null)


            }
        })
    }



    //This method is using Retrofit to get the JSON data from URL
    fun deleteTodoList(
        userId: String,
        meal_id: String,
        restro_id: String

    ) {

        // making progress bar visible
        isLoadingSubject.onNext(true)


        var api = APIClientMvvm.client.create(APIInterface::class.java)

        val call = api.deleteTodo(
            userId,
            meal_id,
            restro_id)

        call.enqueue(object : Callback<DeleteTodoList> {
            override fun onResponse(call: Call<DeleteTodoList>, response: Response<DeleteTodoList>) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)

                //finally we are setting the list to our MutableLiveData
                getTodoDeleteLiveData.postValue(response.body())

//                getTodoDeleteLiveData.value = response.body()

                Log.e("respoDeleteMealPrams", meal_id+"    "+restro_id+"    user"+userId)

                Log.e("respoDeleteMeal", response.body().toString())

            }

            override fun onFailure(call: Call<DeleteTodoList>, t: Throwable) {

                // making progress bar invisible
                isLoadingSubject.onNext(false)
                Log.e("respoDeleteMealFail", "failure")

                getTodoDeleteLiveData.postValue(null)


            }
        })
    }
    fun isLoadingObservable(): Observable<Boolean> {

        return isLoadingSubject

    }

}

package com.dish.seekdish.ui.navDrawer.restaurantDiscription.checkInRestro

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.checkin.adapter.CheckinAdapter
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_checkin_restro.*
import kotlinx.android.synthetic.main.activity_checkin_restro.tvBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckinRestroActivity : BaseActivity() {

    private var rvRegisteredMeal: RecyclerView? = null
    private var rvTodoMeals: RecyclerView? = null
    private var regisMealAdapter: CheckinRestroAdapter? = null
    private var mealAdapter: CheckinMealAdapter? = null

    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal lateinit var layoutManagerMeals: RecyclerView.LayoutManager

    internal var arrayListMealToDO = ArrayList<TodoMeal>()
    internal var arrayListRegis = ArrayList<RegisteredMeal>()

    var sessionManager: SessionManager? = null;
    internal lateinit var apiInterface: APIInterface
    var restro_id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin_restro)

        sessionManager = SessionManager(this)
        rvRegisteredMeal = findViewById(R.id.rvRegistered) as RecyclerView
        rvTodoMeals = findViewById(R.id.rvMeals) as RecyclerView

        initRecyclerView()


        getIntents()

        checkinListApiHit()

        searchTextListner()

        tvBack.setOnClickListener()
        {
            finish()
        }
    }

    private fun getIntents() {
        restro_id = intent.getStringExtra("RESTAURANT_ID").toString()
    }

    private fun initRecyclerView() {
        rvRegisteredMeal!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        rvRegisteredMeal!!.setLayoutManager(layoutManager)

        rvTodoMeals!!.setHasFixedSize(true)
        layoutManagerMeals = LinearLayoutManager(this)
        rvTodoMeals!!.setLayoutManager(layoutManagerMeals)
    }

    fun checkinListApiHit() {

        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)

        val call =
            apiInterface.getCheckinRestro(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                restro_id
            )
        call.enqueue(object : Callback<CheckinRestroModel> {
            override fun onResponse(
                call: Call<CheckinRestroModel>,
                response: Response<CheckinRestroModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {
                    var modelObj = response.body() as CheckinRestroModel

                    if (modelObj.status == 1) {

                        if (modelObj.data.registered_meals.size == 0) {
                            tvRegisAlert.visibility = View.VISIBLE
                        } else {
                            tvRegisAlert.visibility = View.GONE
                            arrayListRegis = modelObj.data.registered_meals
                            regisMealAdapter =
                                CheckinRestroAdapter(arrayListRegis, this@CheckinRestroActivity)
                            rvRegisteredMeal!!.setAdapter(regisMealAdapter)
                        }

                        if (modelObj.data.todo_meals.size == 0) {
                            tvCheckinMeal.visibility = View.VISIBLE
                        } else {
                            tvCheckinMeal.visibility = View.GONE
                            arrayListMealToDO = modelObj.data.todo_meals
                            mealAdapter =
                                CheckinMealAdapter(arrayListMealToDO, this@CheckinRestroActivity)
                            rvTodoMeals!!.setAdapter(mealAdapter)
                        }

                    } else {
                        showSnackBar(modelObj.message);
                    }

                } else {
//                    iSignUpView.onSetLoggedin(false, response)
                    showSnackBar(resources.getString(R.string.error_occured) + "  ${response.code()}");
                }
            }

            override fun onFailure(call: Call<CheckinRestroModel>, t: Throwable) {

//                Log.e("responseFailure", " " + t.toString())

                showSnackBar(resources.getString(R.string.error_occured) + "  ${t.message}");

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    private fun searchTextListner() {
        edtSearchMeal.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // filter your list from your input

                if (edtSearchMeal.text.isNullOrEmpty() == false) {
                    filter(s.toString())
                } else {
                    checkinListApiHit()
                }
            }
        })
    }

    fun filter(text: String?) {
        val filteredRegMeal = java.util.ArrayList<RegisteredMeal>()
        val filteredItemsMeal = java.util.ArrayList<TodoMeal>()

        for (d in arrayListMealToDO) {
            if (d.meal_name.contains(text.toString(), ignoreCase = true)) {
                filteredItemsMeal.add(d)
            }
        }

        for (d in arrayListRegis) {
            if (d.meal_name.contains(text.toString(), ignoreCase = true)) {
                filteredRegMeal.add(d)
            }
        }
        //update recyclerview
        mealAdapter?.updateList(filteredItemsMeal)
        regisMealAdapter?.updateList(filteredRegMeal)

    }
}

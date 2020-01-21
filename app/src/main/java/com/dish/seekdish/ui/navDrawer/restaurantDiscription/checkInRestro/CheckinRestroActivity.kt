package com.dish.seekdish.ui.navDrawer.restaurantDiscription.checkInRestro

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.checkin.adapter.CheckinAdapter
import com.dish.seekdish.ui.navDrawer.checkin.data.CheckinModel
import com.dish.seekdish.ui.navDrawer.checkin.data.Data_Checkin
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.activity_checkin_restro.*
import kotlinx.android.synthetic.main.activity_checkin_restro.tvBack

class CheckinRestroActivity : BaseActivity() {

    private var rvRegisteredMeal: RecyclerView? = null
    private  var rvRegisteredCheckMeals:RecyclerView?=null
    private var regisMealAdapter: CheckinAdapter? = null
    private var mealAdapter: CheckinMealAdapter? = null

    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal lateinit var layoutManagerMeals: RecyclerView.LayoutManager

    internal var arrayList = ArrayList<Data_Checkin>()

    var sessionManager: SessionManager? = null;
    internal lateinit var apiInterface: APIInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin_restro)

        sessionManager= SessionManager(this)
        rvRegisteredMeal = findViewById(R.id.rvRegistered) as RecyclerView
        rvRegisteredCheckMeals = findViewById(R.id.rvMeals) as RecyclerView

        initRecyclerView()


//        checkinListApiHit()

        tvBack.setOnClickListener()
        {
            finish()
        }
    }

    private fun initRecyclerView() {
        rvRegisteredMeal!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        rvRegisteredMeal!!.setLayoutManager(layoutManager)

        rvRegisteredCheckMeals!!.setHasFixedSize(true)
        layoutManagerMeals = LinearLayoutManager(this)
        rvRegisteredCheckMeals!!.setLayoutManager(layoutManagerMeals)
    }

  /*  fun checkinListApiHit() {

        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)

        val call =
            apiInterface.getCheckinData(sessionManager?.getValue(SessionManager.USER_ID).toString())
        call.enqueue(object : Callback<CheckinModel> {
            override fun onResponse(
                call: Call<CheckinModel>,
                response: Response<CheckinModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

                Log.e("respStr", " " + response.body().toString())

                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as CheckinModel
                    if (modelObj.status == 1) {

                        if (modelObj.data.size == 0) {
                            tvCheckinAlert.visibility = View.VISIBLE
                        } else {
                            arrayList = modelObj.data
                            adapter = CheckinAdapter(arrayList, this@CheckinActivity)
                            recyclerView!!.setAdapter(adapter)
                        }

                    }

                } else {
//                    iSignUpView.onSetLoggedin(false, response)
                    showSnackBar(resources.getString(R.string.error_occured));
                }
            }

            override fun onFailure(call: Call<CheckinModel>, t: Throwable) {

//                Log.e("responseFailure", " " + t.toString())

                showSnackBar(resources.getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }*/
}

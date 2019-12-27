package com.dish.seekdish.ui.navDrawer.settings.myAlerts

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_my_alerts.*
import kotlinx.android.synthetic.main.activity_my_alerts.tvBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MyAlertsActivity : BaseActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: MyAlertAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal lateinit var apiInterface: APIInterface
    var sessionManager: SessionManager? = null
    internal var arrayList = ArrayList<Data_Alert>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_alerts)

        hideKeyBoard()
        sessionManager = SessionManager(this)

        if (connectionDetector.isConnectingToInternet) {
            //hitting api
            alertApiHit()
        } else {
            showSnackBar(getString(R.string.check_connection))
        }

        recyclerView = findViewById(R.id.rvAlertsFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)

        tvBack.setOnClickListener()
        {
            finish()
        }
    }

    fun deleteAlert(restroid: Int) {
        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call =
            apiInterface.getAlertDlete(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                restroid.toString()
            )
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(
                call: Call<CancelReModel>,
                response: Response<CancelReModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                Log.e("respStr", " " + response.body().toString())
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as CancelReModel

                    if (modelObj.status == 1) {
                        showSnackBar(modelObj.data.message)
                        alertApiHit()
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured));
                }
            }
            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    fun alertApiHit() {
        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call =
            apiInterface.getAlertListData(sessionManager?.getValue(SessionManager.USER_ID).toString())
        call.enqueue(object : Callback<MyAlertDataClass> {
            override fun onResponse(
                call: Call<MyAlertDataClass>,
                response: Response<MyAlertDataClass>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                Log.e("respStr", " " + response.body().toString())
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as MyAlertDataClass

                    if (modelObj.data.size == 0) {
                        tvAlert.visibility = View.VISIBLE
                        recyclerView?.visibility=View.INVISIBLE
                    } else {
                        adapter = MyAlertAdapter(modelObj.data, this@MyAlertsActivity)
                        recyclerView!!.setAdapter(adapter)
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured));
                }
            }

            override fun onFailure(call: Call<MyAlertDataClass>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

}

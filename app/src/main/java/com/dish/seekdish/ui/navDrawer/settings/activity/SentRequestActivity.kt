package com.dish.seekdish.ui.navDrawer.settings.activity

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
import com.dish.seekdish.ui.navDrawer.settings.adapter.SentRequestAdapter
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Req
import com.dish.seekdish.ui.navDrawer.settings.dataModel.ReceivedRequestDataClass
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_sent_request.*
import kotlinx.android.synthetic.main.activity_sent_request.tvBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.ArrayList

class SentRequestActivity : BaseActivity() {


    private var recyclerView: RecyclerView? = null
    private var adapter: SentRequestAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<Data_Req>()

    internal lateinit var apiInterface: APIInterface
    var sessionManager: SessionManager? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sent_request)

        sessionManager = SessionManager(this)

        // hiding keyboard
        hideKeyBoard()

        clickListners()

        recyclerView = findViewById(R.id.rvSentRequest) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        //check connection
        if (connectionDetector.isConnectingToInternet) {

            //hitting api
            reqApiHit()

        } else {
            showSnackBar(getString(R.string.check_connection))
        }



      /*  for (i in 0..6) {
            val tasteData = SentRequestDataClass("imageUrl", "Cocatre Chansophao");
            arrayList.add(tasteData)
        }
*/


    }

    private fun clickListners() {

        tvBack.setOnClickListener()
        {
            finish()
        }

    }

    fun reqApiHit() {

        ProgressBarClass.progressBarCalling(this)

        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)


        val call = apiInterface.recievedReq(sessionManager?.getValue(SessionManager.USER_ID).toString())
        call.enqueue(object : Callback<ReceivedRequestDataClass> {
            override fun onResponse(
                call: Call<ReceivedRequestDataClass>,
                response: Response<ReceivedRequestDataClass>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()


                Log.e("respStr", " " + response.body().toString())

                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as ReceivedRequestDataClass

                    if (modelObj.data.size == 0) {
                        tvAlert.visibility = View.VISIBLE
                    } else {
                        setRecyclerView(modelObj.data)
                    }
//                    iSignUpView.onSetLoggedin(true, response)

                } else {
//                    iSignUpView.onSetLoggedin(false, response)
                    showSnackBar(resources.getString(R.string.error_occured));

                }


            }

            override fun onFailure(call: Call<ReceivedRequestDataClass>, t: Throwable) {

//                Log.e("responseFailure", " " + t.toString())

                showSnackBar(resources.getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    fun setRecyclerView(data: ArrayList<Data_Req>) {
        arrayList = data
        adapter = SentRequestAdapter(arrayList,this)
        recyclerView!!.setAdapter(adapter)
    }
}

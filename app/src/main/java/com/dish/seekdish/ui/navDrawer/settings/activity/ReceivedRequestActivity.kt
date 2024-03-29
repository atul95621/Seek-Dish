package com.dish.seekdish.ui.navDrawer.settings.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface

import com.dish.seekdish.ui.navDrawer.settings.adapter.ReceivedRequestAdapter
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Req
import com.dish.seekdish.ui.navDrawer.settings.dataModel.ReceivedRequestDataClass
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_received_request.*
import kotlinx.android.synthetic.main.activity_received_request.tvAlert
import kotlinx.android.synthetic.main.activity_received_request.tvBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ReceivedRequestActivity : BaseActivity() {


    private var recyclerView: RecyclerView? = null
    private var adapter: ReceivedRequestAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<Data_Req>()
    internal lateinit var apiInterface: APIInterface
    var sessionManager: SessionManager? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_request)

        // hiding keyboard
        hideKeyBoard()
        sessionManager = SessionManager(this)


        //check connection
        if (connectionDetector.isConnectingToInternet) {
            //hitting api
            reqApiHit()
        } else {
            showSnackBar(getString(R.string.check_connection))
        }

        clickListners()
        searchTextListner()


        recyclerView = findViewById(R.id.rvRecievedRequest) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

    }


    fun reqApiHit() {

        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call =
            apiInterface.recievedReq(sessionManager?.getValue(SessionManager.USER_ID).toString())
        call.enqueue(object : Callback<ReceivedRequestDataClass> {
            override fun onResponse(
                call: Call<ReceivedRequestDataClass>,
                response: Response<ReceivedRequestDataClass>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                arrayList.clear()

                if (response.code().toString().equals("200")) {
                    var modelObj = response.body() as ReceivedRequestDataClass
                    if(modelObj.status==1) {
                        if (modelObj.data.size == 0) {
                            tvAlert.visibility = View.VISIBLE
                            recyclerView?.visibility = View.GONE
                        } else {
                            recyclerView?.visibility = View.VISIBLE
                            arrayList = modelObj.data
                            adapter =
                                ReceivedRequestAdapter(arrayList, this@ReceivedRequestActivity)
                            recyclerView!!.setAdapter(adapter)
                        }
//                    iSignUpView.onSetLoggedin(true, response)
                    }
                    else
                    {
                        showSnackBar(modelObj.message)
                    }
                } else {
//                    iSignUpView.onSetLoggedin(false, response)
                    showSnackBar(resources.getString(R.string.error_occured) + "    ${response.code()}");
                }
            }

            override fun onFailure(call: Call<ReceivedRequestDataClass>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured) + "    ${t.message}");
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    fun apiHit(userId: Int) {
        declineReqHit(userId)
    }


    fun acceptReqApi(userId: Int) {
        acceptFrndReqHit(userId)
    }


    fun acceptFrndReqHit(SenderuserId: Int) {

        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call = apiInterface.doAcceptReq(
            SenderuserId.toString(),sessionManager?.getValue(SessionManager.USER_ID).toString()
        )
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(
                call: Call<CancelReModel>,
                response: Response<CancelReModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                var model = response.body() as CancelReModel
                if (response.code().toString().equals("200")) {
                    if (model.status == 1) {
                        showSnackBar(model.message)
                        reqApiHit()
                    } else {
                        showSnackBar(model.message)
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured)  +"   ${response.code()}");
                }
            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {

                showSnackBar(resources.getString(R.string.error_occured)+"   ${t.message}");
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    fun declineReqHit(SenderuserId: Int) {

        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call = apiInterface.declineFrndReqSent(
            SenderuserId.toString(),
            sessionManager?.getValue(SessionManager.USER_ID).toString()
        )
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(
                call: Call<CancelReModel>,
                response: Response<CancelReModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {
                    var model = response.body() as CancelReModel
                    if (model.status == 1) {
                        showSnackBar(model.message)
                        reqApiHit()
                    } else {
                        showSnackBar(model.message)
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured) + "    ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured) + "    ${t.message}")
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
            }
        })
    }

    private fun clickListners() {
        tvBack.setOnClickListener()
        {
            finish()
        }
    }

    private fun searchTextListner() {

        edtSearchFriends.setOnClickListener()
        {
            edtSearchFriends.isCursorVisible=true
        }

        edtSearchFriends.addTextChangedListener(object : TextWatcher {
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

                if (edtSearchFriends.text.isNullOrEmpty() == false) {
                    filter(s.toString())
                } else {
                    reqApiHit()
                }
            }
        })
    }

    fun filter(text: String?) {
        val filteredItems = ArrayList<Data_Req>()
        for (d in arrayList) {
            if (d.username.contains(text.toString(), ignoreCase = true)) {
                filteredItems.add(d)
            }
        }
        //update recyclerview
        adapter?.updateList(filteredItems)
    }


}

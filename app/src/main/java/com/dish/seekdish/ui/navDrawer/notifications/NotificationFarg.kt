package com.dish.seekdish.ui.navDrawer.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.util.BaseFragment
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.fragment_notification_farg.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationFarg : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    private var adapter: NotificationAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<Data_Notify>()
    lateinit var homeActivity: HomeActivity
    internal lateinit var apiInterface: APIInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_notification_farg, container, false)

        homeActivity = activity as HomeActivity
        sessionManager=SessionManager(context)
//        friendVM = ViewModelProvider(this).get(FriendVM::class.java)

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view?.findViewById(R.id.rvNotifyFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView!!.context,
            (layoutManager as LinearLayoutManager).getOrientation()
        )
        recyclerView!!.addItemDecoration(dividerItemDecoration)

        arrayList.clear()
        hitApi()

        return view
    }

    fun hitApi() {
        ProgressBarClass.progressBarCalling(homeActivity)
        apiInterface = APIClient.getClient(homeActivity).create(APIInterface::class.java)
       /* val call =
            apiInterface.getNotificationList(
                sessionManager.getValue(SessionManager.USER_ID)
            )*/
        val call =
            apiInterface.getNotificationList(
                sessionManager.getValue(SessionManager.USER_ID)
            )
        call.enqueue(object : Callback<NotifyModel> {
            override fun onResponse(
                call: Call<NotifyModel>,
                response: Response<NotifyModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as NotifyModel
                    if (modelObj.status == 1) {
                        arrayList = modelObj.data
                        if (arrayList.isEmpty()) {
                            recyclerView?.visibility = View.INVISIBLE
                            tvNotifiAlert.visibility = View.VISIBLE

                        } else {
                            adapter = NotificationAdapter(arrayList, homeActivity,this@NotificationFarg)
                            recyclerView!!.setAdapter(adapter)
                        }
                    } else {
                        showSnackBar(modelObj.message);
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured)+"  ${response.code()}");
                }
            }

            override fun onFailure(call: Call<NotifyModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured)+"  ${t.message}");

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    fun deleteNotificationApi( notificationId: String) {
        ProgressBarClass.progressBarCalling(homeActivity)
        apiInterface = APIClient.getClient(homeActivity).create(APIInterface::class.java)
        val call =
            apiInterface.deleteNotification(
                sessionManager.getValue(SessionManager.USER_ID), notificationId
            )
        call.enqueue(object : Callback<NotifyDeleteModel> {
            override fun onResponse(
                call: Call<NotifyDeleteModel>,
                response: Response<NotifyDeleteModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as NotifyDeleteModel
                    if (modelObj.status == 1) {
                        showSnackBar(modelObj.message);
                        arrayList.clear()
                        hitApi()

                    } else {
                        showSnackBar(modelObj.message);
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured)+"  ${response.code()}");
                }
            }

            override fun onFailure(call: Call<NotifyDeleteModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured)+"  ${t.message}");

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


    fun deleteItemFromTodoList( notificationId: String, position: Int) {
        deleteNotificationApi( notificationId)
    }

}

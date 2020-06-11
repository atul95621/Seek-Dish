package com.dish.seekdish.ui.navDrawer.invitation.includeFriendDataModels.fragments.selected

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.IncludeFriendsActivity
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.selected.SelectedInclAdapter
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Friend

import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.FriendDataModel
import com.dish.seekdish.util.Global
import com.dish.seekdish.util.SessionManager
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_invitation.*
import kotlinx.android.synthetic.main.fragment_selected_include.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.ArrayList


class SelectedInclFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: SelectedInclAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<Friend>()
    private lateinit var context: IncludeFriendsActivity
    internal lateinit var apiInterface: APIInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_selected_include, container, false)

        context = activity as IncludeFriendsActivity

        sessionManager = SessionManager(context)
        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvSelectedInclFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        arrayList.clear()

        hitApi()

        return view
    }


    private fun hitApi() {
//        FriendDataModelVM?.doGetFriendDataModels(sessionManager.getValue(SessionManager.USER_ID))
//        FriendDataModelVM?.doGetFriendDataModels("129")
        getInvitationApiHit()

    }

/*
    fun getFavListObserver() {

        //observe
        FriendDataModelVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        FriendDataModelVM!!.getFriendDataModelLiveData.observe(this, Observer { response ->
            if (response != null) {
                Log.e("rspFavList", response.toString())
                if (response.status == 1) {


             
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }
*/

    fun getInvitationApiHit() {
        ProgressBarClass.progressBarCalling(context)
        apiInterface = APIClient.getClient(context).create(APIInterface::class.java)
        val call =
            apiInterface.getFriendList(
                sessionManager?.getValue(SessionManager.USER_ID).toString()
            )
        call.enqueue(object : Callback<FriendDataModel> {
            override fun onResponse(
                call: Call<FriendDataModel>,
                response: Response<FriendDataModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                Log.e("respStr", " " + response.body().toString())
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as FriendDataModel
                    if (modelObj.status == 1) {


                        arrayList = modelObj.data.friends
                        if (arrayList.isEmpty()) {
                            recyclerView?.visibility = View.INVISIBLE
                            tvAlertSlectd.visibility = View.VISIBLE

                        } else {
                            adapter = SelectedInclAdapter(arrayList, context)
                            recyclerView!!.setAdapter(adapter)
                        }
                    } else {
                        showSnackBar(modelObj.message);

                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured)+"  ${response.code()}");
                }
            }

            override fun onFailure(call: Call<FriendDataModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured)+"  ${t.message}");

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }


}

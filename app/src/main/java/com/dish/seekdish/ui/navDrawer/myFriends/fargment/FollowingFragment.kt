package com.dish.seekdish.ui.navDrawer.myFriends.fargment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.myFriends.VM.FriendVM
import com.dish.seekdish.ui.navDrawer.myFriends.adapter.FollowingFragAdapter
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Following
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.ArrayList


class FollowingFragment : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    private var adapter: FollowingFragAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<Following>()

    lateinit var homeActivity: HomeActivity
    var friendVM: FriendVM? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_following, container, false)

        homeActivity = activity as HomeActivity
        friendVM = ViewModelProviders.of(this).get(FriendVM::class.java)

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvFollowingFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        arrayList.clear()

        hitApi()
        getFavListObserver()
        getDeleteFollwerObserver()


        return view
    }


    private fun hitApi() {
        friendVM?.doGetFriends(sessionManager.getValue(SessionManager.USER_ID))
    }

    fun removeFriend(toBeRemovedUserId: Int) {
        friendVM?.doRemoveFollowing(
            sessionManager.getValue(SessionManager.USER_ID),
            toBeRemovedUserId.toString()
        )
    }


    fun getFavListObserver() {

        //observe
        friendVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        friendVM!!.getFriendLiveData.observe(this, Observer { response ->
            if (response != null) {
                Log.e("rspFavList", response.toString())
                if (response.status == 1) {
                    arrayList = response.data.followings

                    if (arrayList.isEmpty()) {
                        recyclerView?.visibility = View.INVISIBLE
                        tvFavAlert.visibility = View.VISIBLE

                    } else {
                        adapter = FollowingFragAdapter(arrayList, homeActivity, this)
                        recyclerView!!.setAdapter(adapter)
                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }

    fun getDeleteFollwerObserver() {
        //observe
        friendVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        friendVM!!.getRemoveFollwLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspFavList", response.toString())
                if (response.status == 1) {
                    hitApi()
                    showSnackBar(response.data.message)
                }

            } else {
                showSnackBar("OOps! Error Occured.")
                Log.e("rspSnak", "else error")
            }
        })
    }


}

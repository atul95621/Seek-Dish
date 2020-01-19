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
import com.dish.seekdish.ui.navDrawer.myFriends.adapter.FriendFragAdapter
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Friend
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.ArrayList


class FriendsFragment(var  userId: String) : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: FriendFragAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<Friend>()

    lateinit var homeActivity: HomeActivity
    var friendVM: FriendVM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        homeActivity = activity as HomeActivity
        friendVM = ViewModelProviders.of(this).get(FriendVM::class.java)
        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvMyFriendsFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        arrayList.clear()

        hitApi()
        getFavListObserver()
        getDeleteFriendObserver()

        return view
    }

    private fun hitApi() {
        friendVM?.doGetFriends(userId)
    }


    fun removeFriend(toBeRemovedUserId: Int) {
        friendVM?.doRemoveFriend(
            userId,
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

                    arrayList = response.data.friends

                    if (arrayList.isEmpty()) {
                        recyclerView?.visibility = View.INVISIBLE
                        tvFavAlert.visibility = View.VISIBLE

                    } else {
                        adapter = FriendFragAdapter(arrayList, homeActivity, this,userId)
                        recyclerView!!.setAdapter(adapter)
                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }


    fun getDeleteFriendObserver() {
        //observe
        friendVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        friendVM!!.getRemoveFrndLiveData.observe(this, Observer { response ->
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

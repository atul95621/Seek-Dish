package com.dish.seekdish.ui.navDrawer.myFriends.fargment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.myFriends.VM.FriendVM
import com.dish.seekdish.ui.navDrawer.myFriends.adapter.FriendFragAdapter
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Friend
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_friends.*
import kotlinx.android.synthetic.main.fragment_friends.view.*
import java.util.ArrayList


class FriendsFragment(var userId: String) : BaseFragment() {

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
        friendVM = ViewModelProvider(this).get(FriendVM::class.java)

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
        getFriendReqObserver()
        getFollowingReqObserver()

        searchTextListner(view)

        return view
    }

    private fun hitApi() {
        friendVM?.doGetFriends(userId)

        /*  if (userId.equals(sessionManager.getValue(SessionManager.USER_ID))) {
              friendVM?.doGetFriends(userId)
          } else {
            *//*  homeActivity.imgFilters.visibility = View.GONE
            homeActivity.hideHamburgerIcon()*//*
            friendVM?.doGetMutualFriends(sessionManager.getValue(SessionManager.USER_ID), userId)
        }*/
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
        friendVM!!.getFriendLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    arrayList = response.data.friends
                    if (arrayList.isEmpty()) {
                        recyclerView?.visibility = View.INVISIBLE
                        tvFavAlert.visibility = View.VISIBLE
                    } else {
                        adapter = FriendFragAdapter(arrayList, homeActivity, this, userId)
                        recyclerView!!.setAdapter(adapter)
                    }
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    $response"
                );
            }
        })
    }

    fun followFriend(userIdToFollow: Int) {
        friendVM?.doSendFollwoingRequest(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            userIdToFollow.toString()
        )
        var myId = sessionManager?.getValue(SessionManager.USER_ID).toString()
    }

    fun addFriend(userIdToAddFriend: Int) {
        friendVM?.doSendFriendRequest(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            userIdToAddFriend.toString()
        )
    }

    fun getDeleteFriendObserver() {
        //observe
        friendVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        friendVM!!.getRemoveFrndLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    hitApi()
                    showSnackBar(response.message)
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    $response"
                );
            }
        })
    }

    private fun getFriendReqObserver() {
        //observe
        friendVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        friendVM!!.getFriendReqLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    hitApi()
                    showSnackBar(response.message)
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    ${response}"
                );
            }
        })
    }

    private fun getFollowingReqObserver() {
        //observe
        friendVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        friendVM!!.getFollowingReqLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    hitApi()
                    showSnackBar(response.message)
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    ${response}"
                );
            }
        })
    }

    private fun searchTextListner(view: View) {

        view.edtSearchFriends.setOnClickListener()
        {
            view.edtSearchFriends.isCursorVisible = true
        }
        view.edtSearchFriends.addTextChangedListener(object : TextWatcher {
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

                if (view.edtSearchFriends.text.isNullOrEmpty() == false) {
                    filter(s.toString())
                } else {
                    rvMyFriendsFrag.visibility = View.VISIBLE
                    tvFavAlert.visibility = View.GONE
                    hitApi()
                }
            }
        })
    }

    fun filter(text: String?) {
        val filteredItems = java.util.ArrayList<Friend>()
        for (d in arrayList) {
            if (d.username.contains(text.toString(), ignoreCase = true)) {
                filteredItems.add(d)
            }
            if (filteredItems.size == 0) {
                rvMyFriendsFrag.visibility = View.GONE
                tvFavAlert.visibility = View.VISIBLE
                tvFavAlert.text = getResources().getString(R.string.no_todo)
            } else {
                rvMyFriendsFrag.visibility = View.VISIBLE
                tvFavAlert.visibility = View.GONE
            }
        }
        //update recyclerview
        adapter?.updateList(filteredItems)
    }

}

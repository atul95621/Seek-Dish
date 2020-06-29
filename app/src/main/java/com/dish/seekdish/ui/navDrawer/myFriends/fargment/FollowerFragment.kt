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
import com.dish.seekdish.ui.navDrawer.myFriends.adapter.FollowersFragAdapter
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Follower
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.fragment_follower.view.*
import java.util.ArrayList

class FollowerFragment(var userId: String) : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    private var adapter: FollowersFragAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<Follower>()
    var friendVM: FriendVM? = null
    lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_follower, container, false)

        homeActivity = activity as HomeActivity
        friendVM = ViewModelProvider(this).get(FriendVM::class.java)

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvFollowerFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        arrayList.clear()
        hitApi()
        getFavListObserver()
        getFriendReqObserver()
        getFollowingReqObserver()
        searchTextListner(view)

        return view

    }

    private fun hitApi() {
        if (userId.equals(sessionManager.getValue(SessionManager.USER_ID))) {
            friendVM?.doGetFriends(userId)
        } else {
//            homeActivity.imgFilters.visibility=View.GONE
            friendVM?.doGetMutualFriends(sessionManager.getValue(SessionManager.USER_ID), userId)
        }
    }

    fun getFavListObserver() {
        //observe
        friendVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        friendVM!!.getFriendLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {

                Log.e("rspFavList", response.toString())
                if (response.status == 1) {

                    arrayList = response.data.followers

                    if (arrayList.isEmpty()) {
                        recyclerView?.visibility = View.INVISIBLE
                        tvFavAlert.visibility = View.VISIBLE

                    } else {
                        adapter = FollowersFragAdapter(arrayList,homeActivity,this,userId)
                        recyclerView!!.setAdapter(adapter)
                    }
                }
            } else {
                showSnackBar("OOps! Error Occured.")
                Log.e("rspSnak", "else error")
            }
        })
    }
    fun followFriend(userIdToFollow: Int) {
        friendVM?.doSendFollwoingRequest(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            userIdToFollow.toString()
        )
        var myId = sessionManager?.getValue(SessionManager.USER_ID).toString()
        Log.e("uee", "myid: $myId  , friendId:$userIdToFollow  ")
    }

    fun addFriend(userIdToAddFriend: Int) {
        friendVM?.doSendFriendRequest(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            userIdToAddFriend.toString()
        )
    }

    private fun searchTextListner(view: View) {

        view.edtSearchFollower.setOnClickListener()
        {
            view.edtSearchFollower.isCursorVisible=true
        }
        view.edtSearchFollower.addTextChangedListener(object : TextWatcher {
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

                if (view.edtSearchFollower.text.isNullOrEmpty() == false) {
                    filter(s.toString())
                } else {
                    rvFollowerFrag.visibility=View.VISIBLE
                    tvFavAlert.visibility=View.GONE
                    hitApi()
                }
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
                Log.e("rspGetaddtodoDetails", response.status.toString())
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
                Log.e("rspGetaddtodoFail", "else error")
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
                Log.e("rspGetaddtodoDetails", response.status.toString())
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
                Log.e("rspGetaddtodoFail", "else error")
            }
        })
    }

    fun filter(text: String?) {
        val filteredItems = java.util.ArrayList<Follower>()
        for (d in arrayList) {
            if (d.username.contains(text.toString(), ignoreCase = true)) {
                filteredItems.add(d)
            }
        }
        if(filteredItems.size==0)
        {
            rvFollowerFrag.visibility=View.GONE
            tvFavAlert.visibility=View.VISIBLE
            tvFavAlert.text=getResources().getString(R.string.no_todo)
        }
        else
        {
            rvFollowerFrag.visibility=View.VISIBLE
            tvFavAlert.visibility=View.GONE
        }
        //update recyclerview
        adapter?.updateList(filteredItems)
    }


}

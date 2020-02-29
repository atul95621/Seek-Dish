package com.dish.seekdish.ui.navDrawer.myFriends.fargment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.fragment_following.view.*
import java.util.ArrayList


class FollowingFragment(var userId: String) : BaseFragment() {

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
        getFriendReqObserver()
        getFollowingReqObserver()

        searchTextListner(view)


        return view
    }


    private fun hitApi() {
        friendVM?.doGetFriends(userId)
    }

    fun removeFriend(toBeRemovedUserId: Int) {
        friendVM?.doRemoveFollowing(
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
                    arrayList = response.data.followings
                    if (arrayList.isEmpty()) {
                        recyclerView?.visibility = View.INVISIBLE
                        tvFavAlert.visibility = View.VISIBLE

                    } else {
                        adapter = FollowingFragAdapter(arrayList, homeActivity, this, userId)
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
                else {
                    showSnackBar(response.data.message)
                }

            } else {
                showSnackBar("OOps! Error Occured.")
                Log.e("rspSnak", "else error")
            }
        })
    }

    private fun getFriendReqObserver() {
        //observe
        friendVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        friendVM!!.getFriendReqLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspGetaddtodoDetails", response.status.toString())

                if (response.status == 1) {
                    showSnackBar(response.data.message)
                } else {
                    showSnackBar(response.data.message)
                }
            } else {
                showSnackBar("OOps! Error Occured.")
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

        friendVM!!.getFollowingReqLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspGetaddtodoDetails", response.status.toString())

                if (response.status == 1) {
                    showSnackBar(response.data.message)
                } else {
                    showSnackBar(response.data.message)
                }
            } else {
                showSnackBar("OOps! Error Occured.")
                Log.e("rspGetaddtodoFail", "else error")
            }
        })
    }

    fun followFriend(userIdToFollow: Int) {
        friendVM?.doSendFollwoingRequest(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            userIdToFollow.toString()
        )
    }

    fun addFriend(userIdToAddFriend: Int) {
        friendVM?.doSendFriendRequest(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            userIdToAddFriend.toString()
        )
    }

    private fun searchTextListner(view: View) {

        view.edtSearchFollowing.setOnClickListener()
        {
            view.edtSearchFollowing.isCursorVisible=true
        }
        view.edtSearchFollowing.addTextChangedListener(object : TextWatcher {
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

                if (view.edtSearchFollowing.text.isNullOrEmpty() == false) {
                    filter(s.toString())
                } else {
                    rvFollowingFrag.visibility=View.VISIBLE
                    tvFavAlert.visibility=View.GONE
                    hitApi()
                }
            }
        })
    }

    fun filter(text: String?) {
        val filteredItems = java.util.ArrayList<Following>()
        for (d in arrayList) {
            if (d.username.contains(text.toString(), ignoreCase = true)) {
                filteredItems.add(d)
            }
        }
        if (filteredItems.size == 0) {
            rvFollowingFrag.visibility = View.GONE
            tvFavAlert.visibility = View.VISIBLE
            tvFavAlert.text = getResources().getString(R.string.no_todo)
        } else {
            rvFollowingFrag.visibility = View.VISIBLE
            tvFavAlert.visibility = View.GONE
        }
        //update recyclerview
        adapter?.updateList(filteredItems)
    }


}

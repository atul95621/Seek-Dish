package com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.friends

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
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.IncludeFriendsActivity
import com.dish.seekdish.ui.navDrawer.myFriends.VM.FriendVM
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Friend
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_friends_include.*
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.ArrayList


class FriendsInclFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: FriendsInclAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<Friend>()
    private lateinit var context: IncludeFriendsActivity

    var friendVM: FriendVM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends_include, container, false)

        context = activity as IncludeFriendsActivity
        friendVM = ViewModelProvider(this).get(FriendVM::class.java)
        sessionManager = SessionManager(context)
        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvFriendsInclFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        hitApi()
        getFavListObserver()

        return view
    }

    private fun hitApi() {
        friendVM?.doGetFriends(sessionManager.getValue(SessionManager.USER_ID))
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
                        tvAlert.visibility = View.VISIBLE

                    } else {
                        adapter = FriendsInclAdapter(arrayList, context)
                        recyclerView!!.setAdapter(adapter)
                    }
                }

            } else {
                showSnackBar("OOps! Error Occured.")
            }
        })
    }


}

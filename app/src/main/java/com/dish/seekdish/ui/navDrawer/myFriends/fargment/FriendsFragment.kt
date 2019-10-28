package com.dish.seekdish.ui.navDrawer.myFriends.fargment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.myFriends.adapter.FriendFragAdapter
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.FriendFragDataClass
import java.util.ArrayList


class FriendsFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: FriendFragAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<FriendFragDataClass>()

    lateinit var homeActivity: HomeActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        homeActivity = activity as HomeActivity

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvMyFriendsFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val tasteData = FriendFragDataClass(
                "imageUrl",
                "Cocatre Chansophao"
            );
            arrayList.add(tasteData)
        }

        adapter = FriendFragAdapter(arrayList,homeActivity)
        recyclerView!!.setAdapter(adapter)


        return view
    }


}

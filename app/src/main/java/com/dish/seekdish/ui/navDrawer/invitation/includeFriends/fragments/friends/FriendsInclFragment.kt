package com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.IncludeFriendsActivity
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.FollowersFragDataClass
import java.util.ArrayList


class FriendsInclFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: FriendsInclAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<FollowersFragDataClass>()
    private lateinit var context: IncludeFriendsActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_friends_include, container, false)



        context = activity as IncludeFriendsActivity

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvFriendsInclFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val tasteData = FollowersFragDataClass(
                "imageUrl",
                "Cocatre Chansophao"
            );
            arrayList.add(tasteData)
        }

        adapter = FriendsInclAdapter(arrayList,context)
        recyclerView!!.setAdapter(adapter)



        return view
    }


}

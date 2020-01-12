package com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.selected

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.IncludeFriendsActivity

import java.util.ArrayList


class SelectedInclFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: SelectedInclAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<SelectedInclDataClass>()
    private lateinit var context: IncludeFriendsActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_selected_include, container, false)


        context = activity as IncludeFriendsActivity
Log.e("SelectedInclFragment","onCreateView")
        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvSelectedInclFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val tasteData = SelectedInclDataClass(
                "imageUrl",
                "Cocatre Chansophao"
            );
            arrayList.add(tasteData)
        }

        adapter = SelectedInclAdapter(arrayList,context)
        recyclerView!!.setAdapter(adapter)

        if (isAdded() && isVisible() && getUserVisibleHint()) {
            // ... do your thing
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.e("SelectedInclFragment","onResume")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("SelectedInclFragment","onViewCreated")

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e("SelectedInclFragment","onViewStateRestored")
    }



}

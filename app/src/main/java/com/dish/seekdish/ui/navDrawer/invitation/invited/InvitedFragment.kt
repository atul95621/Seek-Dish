package com.dish.seekdish.ui.navDrawer.invitation.invited

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dish.seekdish.R

import com.dish.seekdish.ui.navDrawer.invitation.InvitationActivity
import com.dish.seekdish.ui.navDrawer.settings.myAlerts.InvitationModel
import kotlinx.android.synthetic.main.fragment_invited.*
import java.util.ArrayList


class InvitedFragment(var invitationModel: InvitationModel) : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: InvitedAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var mcontext: InvitationActivity
    internal var arrayList = ArrayList<InvitedDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_invited, container, false)
        mcontext = activity as InvitationActivity

        recyclerView = view.findViewById(R.id.rvInvitedFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.setLayoutManager(layoutManager)

        /* for (i in 0..6) {
             val InvitedDataClass = InvitedDataClass("Caille Grallie", "Pending");
             arrayList.add(InvitedDataClass)
         }*/

        if (invitationModel.data.invited_tab_array.size != 0) {
            adapter = InvitedAdapter(arrayList, mcontext, invitationModel.data.invited_tab_array)
            recyclerView!!.setAdapter(adapter)
        } else {
            tvAlert.visibility = View.VISIBLE
        }
        return view
    }


}

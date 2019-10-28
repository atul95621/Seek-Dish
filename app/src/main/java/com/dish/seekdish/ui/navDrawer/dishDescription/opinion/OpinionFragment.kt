package com.dish.seekdish.ui.navDrawer.dishDescription.opinion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.dataModel.TasteFragDataClass
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity

import java.util.ArrayList


class OpinionFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: OpinionAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var mcontext: DishDescriptionActivity
    internal var arrayList = ArrayList<OpinionDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_opinion, container, false)

        mcontext = activity as DishDescriptionActivity

        recyclerView = view.findViewById(R.id.rvOpinionFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val OpinionDataClass = OpinionDataClass("Caille Grallie", "14/06/2019");
            arrayList.add(OpinionDataClass)
        }


        adapter = OpinionAdapter(arrayList, mcontext)
        recyclerView!!.setAdapter(adapter)
        
        return view
    }

}

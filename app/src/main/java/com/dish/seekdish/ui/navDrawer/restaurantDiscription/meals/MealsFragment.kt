package com.dish.seekdish.ui.navDrawer.restaurantDiscription.meals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import java.util.ArrayList

class MealsFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: MealsAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<MealsDataClass>()

    private lateinit var dishActivity: RestroDescrpActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_meals, container, false)

        dishActivity = activity as RestroDescrpActivity

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvMealsFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val MealsDataClass = MealsDataClass("Manager", "Brochette De Boeuf", "1.85 Km", "3", "14", "4");
            arrayList.add(MealsDataClass)
        }

        adapter = MealsAdapter(arrayList,dishActivity)
        recyclerView!!.setAdapter(adapter)


        return  view

    }


}

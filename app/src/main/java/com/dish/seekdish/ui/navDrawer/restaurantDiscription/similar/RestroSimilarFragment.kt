package com.dish.seekdish.ui.navDrawer.restaurantDiscription.similar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescpModel
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity

import java.util.ArrayList


class RestroSimilarFragment(var response: RestroDescpModel) : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: RestroSimilarAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var mcontext: RestroDescrpActivity
    internal var arrayList = ArrayList<RestroSimilarDataClass>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_restro_similar, container, false)

        mcontext = activity as RestroDescrpActivity

        recyclerView = view.findViewById(R.id.rvRestroSimilarFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0 until response.data.restaurant.similar_restaurants.size) {

            var title=response.data.restaurant.similar_restaurants[i].name
            var address=response.data.restaurant.similar_restaurants[i].street
            var image=response.data.restaurant.similar_restaurants[i].restaurant_image

            val restroSimilarDataClass = RestroSimilarDataClass(title,address,image);
            arrayList.add(restroSimilarDataClass)
        }


        adapter = RestroSimilarAdapter(arrayList, mcontext)
        recyclerView!!.setAdapter(adapter)

        return  view

    }


}

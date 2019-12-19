package com.dish.seekdish.ui.navDrawer.restaurantDiscription.meals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescpModel
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import java.util.ArrayList

class MealsFragment( var response: RestroDescpModel) : BaseFragment() {
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

        for (i in 0 until  response.data.restaurant.meals.size) {

            var image=response.data.restaurant.meals[i].meal_image
            var dishName=response.data.restaurant.meals[i].meal_name
            var distance=response.data.restaurant.meals[i].distance
            var star=response.data.restaurant.meals[i].meal_avg_rating
            var review=response.data.restaurant.meals[i].no_of_reviews
            var euroRating=response.data.restaurant.meals[i].budget

            val MealsDataClass = MealsDataClass(image, dishName, distance.toString(), star,review.toString(), euroRating.toString());
            arrayList.add(MealsDataClass)
        }

        adapter = MealsAdapter(arrayList,dishActivity)
        recyclerView!!.setAdapter(adapter)


        return  view

    }


}

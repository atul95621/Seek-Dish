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
import kotlinx.android.synthetic.main.fragment_meals.view.*
import java.util.ArrayList

class MealsFragment(var response: RestroDescpModel) : BaseFragment() {
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
        val view = inflater.inflate(R.layout.fragment_meals, container, false)

        dishActivity = activity as RestroDescrpActivity

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvMealsFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0 until response.data.restaurant.meals.size) {

            var image = response.data.restaurant.meals[i].meal_image
            var dishName = response.data.restaurant.meals[i].meal_name
            var distance = response.data.restaurant.meals[i].distance
            var star = response.data.restaurant.meals[i].meal_avg_rating
            var review = response.data.restaurant.meals[i].no_of_reviews
            var euroRating = response.data.restaurant.meals[i].budget
            var mealId = response.data.restaurant.meals[i].meal_id
            var restroId = response.data.restaurant.meals[i].restro_id
            var price = response.data.restaurant.meals[i].meal_price
            var symbol = response.data.restaurant.meals[i].meal_symbol


            val MealsDataClass = MealsDataClass(
                image,
                dishName,
                distance.toString(),
                star,
                review.toString(),
                euroRating.toString(),
                mealId.toString(),
                restroId.toString(),
                price,
                symbol
            );
            arrayList.add(MealsDataClass)
        }
        if (arrayList.size > 0) {
            view.tvMealAlertRest.visibility = View.GONE
            adapter = MealsAdapter(arrayList, dishActivity)
            recyclerView!!.setAdapter(adapter)
        } else {
            view.tvMealAlertRest.visibility = View.VISIBLE
        }

        return view
    }


}

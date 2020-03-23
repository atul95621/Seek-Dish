package com.dish.seekdish.ui.navDrawer.dishDescription.similar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.model.DishDescpModel
import kotlinx.android.synthetic.main.fragment_similar.view.*
import java.util.ArrayList


class SimilarFragment(var objDishModel: DishDescpModel) : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: SimilarAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<SimilarDataClass>()

    private lateinit var dishActivity: DishDescriptionActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_similar, container, false)


        dishActivity = activity as DishDescriptionActivity

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvSimilarFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)


        var size = objDishModel.data.similar_meals.size
        Log.e("sizeTry",""+size)


        if (size == null || size.equals("null") || size.equals("") || size== 0) {

            view.tvMainDish.visibility=View.GONE
            view.tvSimilarAlert.visibility=View.VISIBLE
        }
        else
        {
            for (item in objDishModel.data.similar_meals) {
                val similarDataClass = SimilarDataClass(
                    item.meal_image,
                    item.name,
                    item.distance,
                    item.meal_avg_rating,
                    item.no_of_reviews.toString(),
                    item.budget_rating.toString(),
                    item.meal_id.toString(),
                    item.restro_id.toString()
                );
                arrayList.add(similarDataClass)
            }

            adapter = SimilarAdapter(arrayList, dishActivity)
            recyclerView!!.setAdapter(adapter)
        }

        return view
    }


}

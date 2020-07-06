package com.dish.seekdish.ui.navDrawer.dishDescription.opinion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.model.DishDescpModel
import com.dish.seekdish.util.BaseFragment
import kotlinx.android.synthetic.main.fragment_opinion.view.*
import java.io.Serializable

import java.util.ArrayList


class OpinionFragment(var objDishModel: DishDescpModel) : BaseFragment(), Serializable {
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
        val view = inflater.inflate(R.layout.fragment_opinion, container, false)

        mcontext = activity as DishDescriptionActivity

        arrayList.clear()

        recyclerView = view.findViewById(R.id.rvOpinionFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.setLayoutManager(layoutManager)

        var opinionDetails = objDishModel.data.user_meal_comments

        var sizeComment = objDishModel.data.user_meal_comments.size


        if (sizeComment == null || sizeComment.equals("null") || sizeComment.equals("") || sizeComment == 0) {

            view.tvOpinionHeader.visibility = View.GONE
            view.tvOpinionAlert.visibility = View.VISIBLE
        } else {
            view.tvOpinionHeader.setText(resources.getString(R.string.opinion)+ "("+ sizeComment + ")")

            for (item in objDishModel.data.user_meal_comments) {

                var newDate = datePrase(item.published_on)
                var image: String = ""
                if (objDishModel.data.user_meal_comments.size > 0) {
                    image = item.rating_image1
                }

                val OpinionDataClass = OpinionDataClass(item.username, newDate, image, item.comment, item.anonymous);
                arrayList.add(OpinionDataClass)
            }


            adapter = OpinionAdapter(arrayList, mcontext, opinionDetails,objDishModel.data.user_meal_comments)
            recyclerView!!.setAdapter(adapter)
        }



        return view
    }

}

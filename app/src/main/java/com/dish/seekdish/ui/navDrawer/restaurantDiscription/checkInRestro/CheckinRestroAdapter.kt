package com.dish.seekdish.ui.navDrawer.restaurantDiscription.checkInRestro

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.navDrawer.checkin.CheckinActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.MealRatingActivity

class CheckinRestroAdapter(
    arrayList: ArrayList<RegisteredMeal>,
    var context: CheckinRestroActivity
) :
    RecyclerView.Adapter<CheckinRestroAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<RegisteredMeal>()

    init {
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_check_restro, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val restroMeal = arrayList[position]

        holder.tvCheckinName.text = restroMeal.meal_name
        GlideApp.with(context)
            .load(restroMeal.meal_image)
            .placeholder(R.drawable.ic_applogo_small)
            .into(holder.imgplace)

        holder.linLayoutMeal.setOnClickListener()
        {
            val intent = Intent(context, MealRatingActivity::class.java)
            intent.putExtra("MEALID", restroMeal.meal_id.toString())
            intent.putExtra("RESTROID", restroMeal.restro_id.toString())
            intent.putExtra("IMAGE", restroMeal.meal_image.toString())
            intent.putExtra("FROM_SCREEN", "CheckinRestroActivity")

            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvCheckinName: TextView
        internal var imgplace: ImageView
        internal  var linLayoutMeal:LinearLayout

        init {
            linLayoutMeal = view.findViewById(R.id.linLayoutMeal) as LinearLayout
            tvCheckinName = view.findViewById(R.id.tvCheckinName) as TextView
            imgplace = view.findViewById(R.id.imgplace) as ImageView
        }
    }

    fun updateList(list: ArrayList<RegisteredMeal>) {
        arrayList = list
        notifyDataSetChanged()
    }

}

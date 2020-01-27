package com.dish.seekdish.ui.navDrawer.restaurantDiscription.checkInRestro

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.navDrawer.dishDescription.MealRatingActivity

class CheckinMealAdapter(
    arrayList: ArrayList<TodoMeal>,
    var conxt: CheckinRestroActivity
    ) :
    RecyclerView.Adapter<CheckinMealAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<TodoMeal>()

    init {
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_check_restro, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val todoMeal = arrayList[position]

        holder.tvCheckinName.text = todoMeal.restro_name
        GlideApp.with(conxt)
            .load(todoMeal.meal_image)
            .placeholder(R.drawable.ic_applogo_small)
            .into(holder.imgplace)

        holder.linLayoutMeal.setOnClickListener()
        {
            val intent = Intent(conxt, MealRatingActivity::class.java)
            intent.putExtra("MEALID", todoMeal.meal_id.toString())
            intent.putExtra("RESTROID", todoMeal.restro_id.toString())
            intent.putExtra("IMAGE", todoMeal.meal_image.toString())
            intent.putExtra("FROM_SCREEN", "CheckinRestroActivity")

            conxt.startActivity(intent)
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

    fun updateList(list: ArrayList<TodoMeal>) {
        arrayList = list
        notifyDataSetChanged()
    }


}

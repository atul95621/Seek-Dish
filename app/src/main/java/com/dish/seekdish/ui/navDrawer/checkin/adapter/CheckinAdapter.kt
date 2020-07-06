package com.dish.seekdish.ui.navDrawer.checkin.adapter

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
import com.dish.seekdish.ui.navDrawer.checkin.data.Data_Checkin
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import java.util.ArrayList


class CheckinAdapter(
    arrayList: ArrayList<Data_Checkin>,
    var context: CheckinActivity
) :
    RecyclerView.Adapter<CheckinAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Checkin>()

    init {
        this.arrayList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_check_in, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val Data_Checkin = arrayList[position]

        holder.tvCheckinName.text = Data_Checkin.meal_name + "  (" + Data_Checkin.restro_name + ")"
        holder.tvCheckinDate.text = context.datePrase(Data_Checkin.date)
        GlideApp.with(context)
            .load(Data_Checkin.meal_image)
            .placeholder(R.drawable.ic_applogo_small)
            .into(holder.imgplace)

        holder.linlayout.setOnClickListener()
        {
            val intent = Intent(context, DishDescriptionActivity::class.java)
            intent.putExtra("MEAL_ID", Data_Checkin.meal.toString())
            intent.putExtra("RESTAURANT_ID", Data_Checkin.restro_id.toString())
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvCheckinName: TextView
        internal var tvCheckinDate: TextView
        internal var imgplace: ImageView
        internal var linlayout: LinearLayout

        init {
            tvCheckinName = view.findViewById(R.id.tvCheckinName) as TextView
            tvCheckinDate = view.findViewById(R.id.tvCheckinDate) as TextView
            imgplace = view.findViewById(R.id.imgplace) as ImageView
            linlayout = view.findViewById(R.id.linlayout) as LinearLayout

        }
    }


}

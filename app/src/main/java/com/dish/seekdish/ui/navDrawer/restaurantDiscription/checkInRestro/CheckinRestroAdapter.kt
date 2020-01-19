package com.dish.seekdish.ui.navDrawer.restaurantDiscription.checkInRestro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.navDrawer.checkin.CheckinActivity
import com.dish.seekdish.ui.navDrawer.checkin.data.Data_Checkin

class CheckinRestroAdapter(
    arrayList: ArrayList<Data_Checkin>,
    var context: CheckinActivity
) :
    RecyclerView.Adapter<CheckinRestroAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Checkin>()

    init {
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_check_restro, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val Data_Checkin = arrayList[position]

        holder.tvCheckinName.text = Data_Checkin.restro_name
        GlideApp.with(context)
            .load(Data_Checkin.user_image)
            .placeholder(R.drawable.ic_applogo_small)
            .into(holder.imgplace)
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvCheckinName: TextView
        internal var imgplace: ImageView

        init {
            tvCheckinName = view.findViewById(R.id.tvCheckinName) as TextView
            imgplace = view.findViewById(R.id.imgplace) as ImageView
        }
    }


}

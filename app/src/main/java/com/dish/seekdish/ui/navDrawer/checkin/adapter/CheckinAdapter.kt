package com.dish.seekdish.ui.navDrawer.checkin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.checkin.data.CheckinDataClass
import java.util.ArrayList


class CheckinAdapter(arrayList: ArrayList<CheckinDataClass>, var context: Context) :
    RecyclerView.Adapter<CheckinAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<CheckinDataClass>()

    init {
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_check_in, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val checkinDataClass = arrayList[position]

        // getting all vales and storing in val...
        // now setted to the textview
        holder.tvCheckinName.text = checkinDataClass.checkinTitle
        holder.tvCheckinDate.text = checkinDataClass.checkinDate
//        holder.imgplace.text = checkinDataClass.imageUrl

    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvCheckinName: TextView
        internal var tvCheckinDate: TextView
        internal var imgplace: ImageView

        init {
            tvCheckinName = view.findViewById(R.id.tvCheckinName) as TextView
            tvCheckinDate = view.findViewById(R.id.tvCheckinDate) as TextView
            imgplace = view.findViewById(R.id.imgplace) as ImageView
        }
    }


}

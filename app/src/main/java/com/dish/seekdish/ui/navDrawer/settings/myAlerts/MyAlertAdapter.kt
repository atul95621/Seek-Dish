package com.dish.seekdish.ui.navDrawer.settings.myAlerts

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
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import com.google.android.gms.common.data.DataHolder
import java.util.*
import kotlin.collections.ArrayList


class MyAlertAdapter(arrayList: ArrayList<Data_Alert>, mcontext: MyAlertsActivity) :
    RecyclerView.Adapter<MyAlertAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Alert>()

    var mcontext: MyAlertsActivity

    init {
        this.arrayList = arrayList
        this.mcontext = mcontext
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_my_alerts, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val MyAlertDataClass = arrayList[position]

        // getting all vales and storing in val...
        // now setted to the textview
        holder.tvCheckinName.text = MyAlertDataClass.name
//        holder.tvCheckinDate.text = MyAlertDataClass.opinionDate
        holder.tvAddress.text =
            MyAlertDataClass.street + "," + MyAlertDataClass.city + "," + MyAlertDataClass.zipcode
        GlideApp.with(mcontext)
            .load(MyAlertDataClass.restaurant_image).placeholder(R.drawable.app_logo)
            .into(holder.imgplace)
        holder.imgplace.setOnClickListener()
        {
            val intent = Intent(mcontext, RestroDescrpActivity::class.java)
            intent.putExtra("RESTAURANT_ID", MyAlertDataClass.id.toString())
            mcontext.startActivity(intent)
        }

        holder.imgDelete.setOnClickListener()
        {
            mcontext.deleteAlert(MyAlertDataClass.id)
        }
    }

    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvCheckinName: TextView
        internal var tvAddress: TextView
        internal var imgplace: ImageView
        internal var linOpinion: LinearLayout
        internal var imgDelete: ImageView

        init {
            tvCheckinName = view.findViewById(R.id.tvCheckinName) as TextView
            tvAddress = view.findViewById(R.id.tvAddress) as TextView
            imgplace = view.findViewById(R.id.imgplace) as ImageView
            linOpinion = view.findViewById(R.id.linOpinion) as LinearLayout
            imgDelete = view.findViewById(R.id.imgDelete) as ImageView

        }
    }

    fun clear() {
        arrayList.clear()
        notifyDataSetChanged()
    }

    fun updateList(list: ArrayList<Data_Alert>) {
        arrayList = list
        notifyDataSetChanged()
    }


}

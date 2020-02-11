package com.dish.seekdish.ui.navDrawer.notifications

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity

class NotificationAdapter(
    arrayList: ArrayList<Data_Notify>,
    activity: HomeActivity
) :
    RecyclerView.Adapter<NotificationAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Notify>()
    var activity: HomeActivity

    init {
        this.arrayList = arrayList
        this.activity=activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_notification_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val notifyModel = arrayList[position]

        // getting all vales and storing in val...
        var imgFriend: String = notifyModel.image
        Glide.with(activity).load(imgFriend).placeholder(R.drawable.ic_user).into(holder.imgUser);
        holder.tvNotifi.text = notifyModel.notification_message
        holder.tvTime.text = activity.dateTimePrase(notifyModel.date_and_time)


      /*  holder.imgFriend.setOnClickListener()
        {
            *//*val intent = Intent(activity, FriendInfoActivity::class.java)
            intent.putExtra("IMAGE", notifyModel.user_image);
            intent.putExtra("NAME", notifyModel.username);
            intent.putExtra("USER_ID", notifyModel.user_id);
            activity.startActivity(intent)*//*
        }*/

    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgUser: ImageView
        internal var tvNotifi: TextView
        internal var tvTime: TextView

        init {
            imgUser = view.findViewById(R.id.imgUser) as ImageView
            tvNotifi = view.findViewById(R.id.tvNotifi) as TextView
            tvTime = view.findViewById(R.id.tvTime) as TextView
        }
    }


 /*   fun updateList(list: ArrayList<Follower>) {
        arrayList = list
        notifyDataSetChanged()
    }*/


}

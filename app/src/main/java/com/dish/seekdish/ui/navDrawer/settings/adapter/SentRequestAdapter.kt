package com.dish.seekdish.ui.navDrawer.settings.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.activities.MyInformationActivity
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.SentRequestActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Req
import java.util.ArrayList

class SentRequestAdapter(
    arrayList: ArrayList<Data_Req>,
    var sentRequestActivity: SentRequestActivity
) :
    RecyclerView.Adapter<SentRequestAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Req>()
    lateinit var context: Context

    init {
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_sent_req, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val followingDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = followingDataClass.user_image
        Glide.with(sentRequestActivity).load(imageUrl)
            .into(holder.imgFriend);//        Glide.with(this).load(imgFriend).apply(options).into(holder.imgFoodImage);
        holder.tvFriendName.text = followingDataClass.username
        holder.imgFriend.setOnClickListener()
        {
            val intent = Intent(context, MyInformationActivity::class.java)
            context.startActivity(intent)
        }

        holder.imgFriend.setOnClickListener()
        {
            val intent = Intent(context, FriendInfoActivity::class.java)
            context.startActivity(intent)
        }

        holder.btnCancel.setOnClickListener()
        {
            sentRequestActivity.cancelReqHit(followingDataClass.user_id)
        }

    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgFriend: ImageView
        internal var tvFriendName: TextView
        internal var btnCancel: Button

        init {
            btnCancel = view.findViewById(R.id.btnCancel) as Button
            imgFriend = view.findViewById(R.id.imgFriend) as ImageView
            tvFriendName = view.findViewById(R.id.tvFriendName) as TextView
        }
    }
    fun updateList(list: ArrayList<Data_Req>) {
        arrayList = list
        notifyDataSetChanged()
    }


}


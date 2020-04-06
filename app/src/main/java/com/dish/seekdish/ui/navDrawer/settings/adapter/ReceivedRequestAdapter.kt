package com.dish.seekdish.ui.navDrawer.settings.adapter

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
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.ReceivedRequestActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Req
import java.util.ArrayList

class ReceivedRequestAdapter(
    arrayList: ArrayList<Data_Req>,
   var acitityReq: ReceivedRequestActivity
) :
    RecyclerView.Adapter<ReceivedRequestAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Req>()

    init {
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_received_req, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val followingDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = followingDataClass.user_image
        Glide.with(acitityReq).load(imageUrl).placeholder(R.drawable.ic_user).into(holder.imgFriend);
        holder.tvFriendName.text = followingDataClass.username

        holder.imgFriend.setOnClickListener()
        {
            val intent = Intent(acitityReq, FriendInfoActivity::class.java)
            intent.putExtra("USER_ID", followingDataClass.user_id);
            intent.putExtra("NAME", followingDataClass.username);
            intent.putExtra("IMAGE", followingDataClass.user_image);
            acitityReq.startActivity(intent)
        }

        holder.btnDecline.setOnClickListener()
        {
            acitityReq.apiHit(followingDataClass.user_id)
        }

        holder.btnAccept.setOnClickListener()
        {
            acitityReq.acceptReqApi(followingDataClass.user_id)
        }


    }



    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgFriend: ImageView
        internal var tvFriendName: TextView
        internal var btnDecline: Button
        internal var btnAccept: Button



        init {
            btnAccept = view.findViewById(R.id.btnAccept) as Button
            btnDecline = view.findViewById(R.id.btnDecline) as Button
            imgFriend = view.findViewById(R.id.imgFriend) as ImageView
            tvFriendName = view.findViewById(R.id.tvFriendName) as TextView
        }
    }

    fun updateList(list: ArrayList<Data_Req>) {
        arrayList = list
        notifyDataSetChanged()
    }


}


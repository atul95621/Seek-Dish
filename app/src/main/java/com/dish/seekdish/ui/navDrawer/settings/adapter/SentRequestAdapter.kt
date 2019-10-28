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
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.activities.MyInformationActivity
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SentRequestDataClass
import java.util.ArrayList

class SentRequestAdapter(
    arrayList: ArrayList<SentRequestDataClass>
) :
    RecyclerView.Adapter<SentRequestAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<SentRequestDataClass>()
    lateinit var context: Context

    init {
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_sent_req, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val followingDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imgFriend: String = followingDataClass.friendImageURL.toString()
//        Glide.with(this).load(imgFriend).apply(options).into(holder.imgFoodImage);
        holder.tvFriendName.text = followingDataClass.friendName
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


}


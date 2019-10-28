package com.dish.seekdish.ui.navDrawer.myFriends.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.FollowingFragDataClass
import java.util.ArrayList



class FollowingFragAdapter(
    arrayList: ArrayList<FollowingFragDataClass>,
    activity: HomeActivity
) :
    RecyclerView.Adapter<FollowingFragAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<FollowingFragDataClass>()
    var activity: HomeActivity


    init {
        this.arrayList = arrayList
        this.activity=activity

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_following_frag, parent, false)
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
            val intent = Intent(activity, FriendInfoActivity::class.java)
            activity.startActivity(intent)
        }


    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgFriend: ImageView
        internal var tvFriendName: TextView
        internal var btnReplace: Button

        init {
            btnReplace = view.findViewById(R.id.btnReplace) as Button
            imgFriend = view.findViewById(R.id.imgFriend) as ImageView
            tvFriendName = view.findViewById(R.id.tvFriendName) as TextView
        }
    }


}


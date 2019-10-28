package com.dish.seekdish.ui.navDrawer.myFriends.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.FollowersFragDataClass
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowerFragment
import java.util.ArrayList


class FollowersFragAdapter(
    arrayList: ArrayList<FollowersFragDataClass>,
    activity: HomeActivity
) :
    RecyclerView.Adapter<FollowersFragAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<FollowersFragDataClass>()

     var activity: HomeActivity

    init {
        this.arrayList = arrayList
        this.activity=activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_followers_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val friendDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imgFriend: String = friendDataClass.friendImageURL.toString()
//        Glide.with(this).load(imgFriend).apply(options).into(holder.imgFoodImage);
        holder.tvFriendName.text = friendDataClass.friendName

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

        init {
            imgFriend = view.findViewById(R.id.imgFriend) as ImageView
            tvFriendName = view.findViewById(R.id.tvFriendName) as TextView
        }
    }


}

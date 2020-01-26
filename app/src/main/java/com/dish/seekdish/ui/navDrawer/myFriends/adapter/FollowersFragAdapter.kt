package com.dish.seekdish.ui.navDrawer.myFriends.adapter

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
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Follower
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowerFragment
import java.util.ArrayList


class FollowersFragAdapter(
    arrayList: ArrayList<Follower>,
    activity: HomeActivity,
   var followerFragment: FollowerFragment
) :
    RecyclerView.Adapter<FollowersFragAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Follower>()

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
        var imgFriend: String = friendDataClass.user_image
        Glide.with(activity).load(imgFriend).into(holder.imgFriend);
        holder.tvFriendName.text = friendDataClass.username


        holder.imgFriend.setOnClickListener()
        {
            val intent = Intent(activity, FriendInfoActivity::class.java)
            intent.putExtra("IMAGE", friendDataClass.user_image);
            intent.putExtra("NAME", friendDataClass.username);
            intent.putExtra("USER_ID", friendDataClass.user_id);
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


    fun updateList(list: ArrayList<Follower>) {
        arrayList = list
        notifyDataSetChanged()
    }


}

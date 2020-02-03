package com.dish.seekdish.ui.navDrawer.myFriends.adapter

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
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Friend
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FriendsFragment
import com.dish.seekdish.util.SessionManager
import java.util.ArrayList


class FriendFragAdapter(
    arrayList: ArrayList<Friend>,
    activity: HomeActivity,
    var friendsFragment: FriendsFragment?,
    var userIdFromOutside: String
) :
    RecyclerView.Adapter<FriendFragAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Friend>()
    var activity: HomeActivity

    init {
        this.arrayList = arrayList
        this.activity = activity

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_following_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val friendDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imgFriend: String = friendDataClass.user_image
        Glide.with(activity).load(imgFriend).placeholder(R.drawable.ic_user).into(holder.imgFriend);
        holder.tvFriendName.text = friendDataClass.username

        holder.imgFriend.setOnClickListener()
        {
            val intent = Intent(activity, FriendInfoActivity::class.java)
            intent.putExtra("IMAGE", friendDataClass.user_image);
            intent.putExtra("NAME", friendDataClass.username);
            intent.putExtra("USER_ID", friendDataClass.user_id);
            activity.startActivity(intent)
        }

        holder.btnReplace.setOnClickListener()
        {
            friendsFragment?.removeFriend(friendDataClass.user_id)
        }

        if (friendsFragment?.sessionManager?.getValue(SessionManager.USER_ID).equals(
                userIdFromOutside
            )
        ) {
            holder.btnReplace.visibility = View.VISIBLE
        } else {
            holder.btnReplace.visibility = View.GONE
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


    fun updateList(list: ArrayList<Friend>) {
        arrayList = list
        notifyDataSetChanged()
    }


}


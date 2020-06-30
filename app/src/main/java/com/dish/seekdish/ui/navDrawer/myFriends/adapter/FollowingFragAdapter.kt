package com.dish.seekdish.ui.navDrawer.myFriends.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Following
import com.dish.seekdish.ui.navDrawer.myFriends.fargment.FollowingFragment
import com.dish.seekdish.util.SessionManager
import java.util.ArrayList


class FollowingFragAdapter(
    arrayList: ArrayList<Following>,
    activity: HomeActivity,
    var followingFragment: FollowingFragment,
    var userIdFrom: String
) :
    RecyclerView.Adapter<FollowingFragAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Following>()
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
        val followingDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imgFriend: String = followingDataClass.user_image
        Glide.with(activity).load(imgFriend).placeholder(R.drawable.ic_user).into(holder.imgFriend);
        holder.tvFriendName.text = followingDataClass.username


        holder.imgFriend.setOnClickListener()
        {
            val intent = Intent(activity, FriendInfoActivity::class.java)
            intent.putExtra("IMAGE", followingDataClass.user_image);
            intent.putExtra("NAME", followingDataClass.username);
            intent.putExtra("USER_ID", followingDataClass.user_id);
            activity.startActivity(intent)
        }

        holder.btnReplace.setOnClickListener()
        {
            followingFragment?.removeFriend(followingDataClass.user_id)
        }

        holder.btnFollow.setOnClickListener()
        {
            followingFragment.followFriend(followingDataClass.user_id)
        }
        holder.btnAddFriend.setOnClickListener()
        {
            followingFragment.addFriend(followingDataClass.user_id)
        }
        if (followingFragment?.sessionManager?.getValue(SessionManager.USER_ID).equals(userIdFrom)) {
            holder.btnReplace.visibility = View.VISIBLE
        } else {
            holder.btnReplace.visibility = View.GONE
            holder.linFollowAdd.visibility = View.VISIBLE
            if (followingDataClass.already_follower == 1) {
                holder.btnFollow.visibility = View.GONE
            } else {
                holder.btnFollow.visibility = View.VISIBLE
            }
            if (followingDataClass.already_friend == 1) {
                holder.btnAddFriend.visibility = View.GONE
            } else {
                holder.btnAddFriend.visibility = View.VISIBLE
            }

        }

        // this used to hide bcoz a person cant follow or send request to himself
        if ((followingFragment?.sessionManager?.getValue(SessionManager.USER_ID).toString()).equals(followingDataClass.user_id.toString())
        ) {
            holder.linFollowAdd.visibility = View.GONE
        }
     
   /*     if (!followingFragment.sessionManager.getValue(SessionManager.USER_ID).equals(userIdFrom)) {
            holder.btnReplace.visibility = View.GONE
            holder.linFollowAdd.visibility = View.VISIBLE
        }
        else {
            holder.btnReplace.visibility = View.VISIBLE
        }
*/
    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgFriend: ImageView
        internal var tvFriendName: TextView
        internal var btnReplace: Button
        var linFollowAdd: LinearLayout
        internal var btnFollow: Button
        internal var btnAddFriend: Button

        init {
            btnAddFriend = view.findViewById(R.id.btnAddFriend) as Button
            btnReplace = view.findViewById(R.id.btnReplace) as Button
            btnFollow = view.findViewById(R.id.btnFollow) as Button
            imgFriend = view.findViewById(R.id.imgFriend) as ImageView
            tvFriendName = view.findViewById(R.id.tvFriendName) as TextView
            linFollowAdd = view.findViewById(R.id.linFollowAdd) as LinearLayout
        }
    }
    fun updateList(list: ArrayList<Following>) {
        arrayList = list
        notifyDataSetChanged()
    }


}


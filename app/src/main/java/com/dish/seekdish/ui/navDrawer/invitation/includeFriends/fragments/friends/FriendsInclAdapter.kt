package com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.IncludeFriendsActivity
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.FollowersFragDataClass
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.Friend
import com.dish.seekdish.util.Global
import java.util.ArrayList

class FriendsInclAdapter(
    arrayList: ArrayList<Friend>,
    activity: IncludeFriendsActivity
) :
    RecyclerView.Adapter<FriendsInclAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Friend>()

    var activity: IncludeFriendsActivity

    init {
        this.arrayList = arrayList
        this.activity = activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_include_friends_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val friendDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imgFriend: String = friendDataClass.user_image
        Glide.with(activity).load(imgFriend).into(holder.imgFriend);
        holder.tvFriendName.text = friendDataClass.username

        /*    holder.imgFriend.setOnClickListener()
            {
                val intent = Intent(activity, FriendInfoActivity::class.java)
                activity.startActivity(intent)
            }*/

        holder.checkboxSelected.setOnClickListener()
        {
            if (friendDataClass.friendSelected == false) {
                holder.checkboxSelected.isChecked = true
                friendDataClass.friendSelected = true
                Global.selectedFriends.add(friendDataClass.user_id.toString())
            } else {
                holder.checkboxSelected.isChecked = false
                friendDataClass.friendSelected = false
                var ingrId = friendDataClass.user_id.toString()

                for (i in 0 until Global.selectedFriends.size) {
                    if (Global.selectedFriends.contains(ingrId)) {
                        Global.selectedFriends.remove(ingrId)
                    }
                }
            }
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgFriend: ImageView
        internal var tvFriendName: TextView
        internal var checkboxSelected: CheckBox

        init {
            imgFriend = view.findViewById(R.id.imgFriend) as ImageView
            tvFriendName = view.findViewById(R.id.tvFriendName) as TextView
            checkboxSelected = view.findViewById(R.id.checkboxSelected) as CheckBox

        }
    }


}

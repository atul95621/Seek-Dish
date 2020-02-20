package com.dish.seekdish.ui.navDrawer.notifications

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.activities.MyProfileActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.friendInfo.FriendInfoActivity
import com.dish.seekdish.ui.navDrawer.invitation.InvitationActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.ReceivedRequestActivity

class NotificationAdapter(
    arrayList: ArrayList<Data_Notify>,
    activity: HomeActivity
) :
    RecyclerView.Adapter<NotificationAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Notify>()
    var activity: HomeActivity

    init {
        this.arrayList = arrayList
        this.activity = activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_notification_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val notifyModel = arrayList[position]

        // getting all vales and storing in val...
        var imgFriend: String = notifyModel.image
        Glide.with(activity).load(imgFriend).placeholder(R.drawable.ic_user).into(holder.imgUser);
        holder.tvNotifi.text = notifyModel.notification_message
        holder.tvTime.text = activity.dateTimePrase(notifyModel.date_and_time)
        var noti_type = notifyModel.notification_type
        holder.linLayout.setOnClickListener()
        {
            if (noti_type.equals("rating_notification_to_followers")) {
                val intent = Intent(activity, DishDescriptionActivity::class.java)
                intent.putExtra("MEAL_ID", notifyModel.meal_id.toString())
                intent.putExtra("RESTAURANT_ID", notifyModel.restaurant_id.toString())
                activity.startActivity(intent)
            } else if (noti_type.equals("newmeal_alert_to_users")) {
                val intent = Intent(activity, DishDescriptionActivity::class.java)
                intent.putExtra("MEAL_ID", notifyModel.meal_id.toString())
                intent.putExtra("RESTAURANT_ID", notifyModel.restaurant_id.toString())
                activity.startActivity(intent)
            } else if (noti_type.equals("decline_invitation")) {
                val intent = Intent(activity, InvitationActivity::class.java)
                intent.putExtra("RESTAURANT_ID", notifyModel.restaurant_id.toString())
                intent.putExtra("FROM", "")
                intent.putExtra("USER_WHO_SENT_ID", "");
                intent.putExtra("TIME", "");
                activity.startActivity(intent)
            } else if (noti_type.equals("accept_invitation")) {
                val intent = Intent(activity, InvitationActivity::class.java)
                intent.putExtra("RESTAURANT_ID", notifyModel.restaurant_id.toString())
                intent.putExtra("FROM", "")
                intent.putExtra("USER_WHO_SENT_ID", "");
                intent.putExtra("TIME", "");
                activity.startActivity(intent)
            } else if (noti_type.equals("send_friend_request")) {
// no action
                val intent = Intent(activity, ReceivedRequestActivity::class.java)
                activity.startActivity(intent)
            } else if (noti_type.equals("accept_friend_request")) {
                val intent = Intent(activity, FriendInfoActivity::class.java)
                intent.putExtra("IMAGE", notifyModel.image);
                intent.putExtra("NAME", notifyModel.username);
                intent.putExtra("USER_ID", notifyModel.user_id);
                activity.startActivity(intent)
            } else if (noti_type.equals("decline_friend_request")) {
// no action
            } else if (noti_type.equals("post_invitation")) {
//                if (notifyModel.invitation_status.equals("pending")) {
                    val intent = Intent(activity, InvitationActivity::class.java)
                    intent.putExtra("RESTAURANT_ID", notifyModel.restaurant_id.toString())
                    intent.putExtra("FROM", "NotificationAdapter")
                    intent.putExtra("USER_WHO_SENT_ID", notifyModel.user_id.toString());
                    intent.putExtra("TIME", notifyModel.date_and_time);
                    activity.startActivity(intent)
//                } else {
//                    Toast.makeText(activity, "Action already taken", Toast.LENGTH_SHORT).show()
//                }
            } else if (noti_type.equals("following_friend_request")) {
                val intent = Intent(activity, FriendInfoActivity::class.java)
                intent.putExtra("IMAGE", notifyModel.image);
                intent.putExtra("NAME", notifyModel.follower_name.toString());
                intent.putExtra("USER_ID", notifyModel.user_id);
                activity.startActivity(intent)
            }
        }

    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgUser: ImageView
        internal var tvNotifi: TextView
        internal var tvTime: TextView
        internal var linLayout: LinearLayout

        init {
            imgUser = view.findViewById(R.id.imgUser) as ImageView
            tvNotifi = view.findViewById(R.id.tvNotifi) as TextView
            tvTime = view.findViewById(R.id.tvTime) as TextView
            linLayout = view.findViewById(R.id.linLayout) as LinearLayout

        }
    }


    /*   fun updateList(list: ArrayList<Follower>) {
           arrayList = list
           notifyDataSetChanged()
       }*/


}

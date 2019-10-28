package com.dish.seekdish.ui.navDrawer.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.settings.dataModel.ReceivedRequestDataClass
import java.util.ArrayList

class ReceivedRequestAdapter(arrayList: ArrayList<ReceivedRequestDataClass>) :
    RecyclerView.Adapter<ReceivedRequestAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<ReceivedRequestDataClass>()

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
        var imgFriend: String = followingDataClass.friendImageURL.toString()
//        Glide.with(this).load(imgFriend).apply(options).into(holder.imgFoodImage);
        holder.tvFriendName.text = followingDataClass.friendName

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


}


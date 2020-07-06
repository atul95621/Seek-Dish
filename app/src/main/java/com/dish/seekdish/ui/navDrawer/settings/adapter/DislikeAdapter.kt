package com.dish.seekdish.ui.navDrawer.settings.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.settings.activity.DislikeActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Disliked
import com.dish.seekdish.util.Global
import java.util.ArrayList

class DislikeAdapter(
    val dislikeActivity: DislikeActivity, var disLikedList: ArrayList<Data_Disliked>
) :
    RecyclerView.Adapter<DislikeAdapter.RecyclerViewHolder>() {


    var flag: Boolean = false
    var count: Int = 0


    fun DislikeAdapter(dataItems: ArrayList<Data_Disliked>) {
        this.disLikedList = dataItems
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_dislike, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val friendDataClass = disLikedList[position]
        // getting all vales and storing in val...
        /*   if (friendDataClass.liked == 1 || friendDataClass.checkForDisLike) {
               holder.checkBoxLiked.isChecked = false
               disdisLikedList.get(position).checkForDisLike = false

           } else if (friendDataClass.liked == 0 || friendDataClass.checkForDisLike) {
               holder.checkBoxLiked.isChecked = true
               disdisLikedList.get(position).checkForDisLike = true

           } else if (friendDataClass.liked == 2 || friendDataClass.checkForDisLike) {
               Log.e("checkbox liked", "" + friendDataClass.liked)
               holder.checkBoxLiked.isChecked = false
               disdisLikedList.get(position).checkForDisLike = false
           }*/

        if (friendDataClass.liked == 0) {
            holder.checkBoxLiked.isChecked = true

            Global.dislikedItemsSet.add(disLikedList[position].id.toString())

//            disLikedList.get(position).checkForLike = true
            if (friendDataClass.checkForDisLike == false) {
                holder.checkBoxLiked.isChecked = true
                disLikedList.get(position).checkForDisLike = true
            } else {
                holder.checkBoxLiked.isChecked = true
                disLikedList.get(position).checkForDisLike = true
            }

        } else if (friendDataClass.liked == 1 || friendDataClass.checkForDisLike) {

            if (friendDataClass.checkForDisLike) {
                holder.checkBoxLiked.isChecked = true
                disLikedList.get(position).checkForDisLike = true
            } else {
                holder.checkBoxLiked.isChecked = false
                disLikedList.get(position).checkForDisLike = false
            }

        } else if (friendDataClass.liked == 2 || friendDataClass.checkForDisLike) {
            if (friendDataClass.checkForDisLike) {
                holder.checkBoxLiked.isChecked = true
                disLikedList.get(position).checkForDisLike = true
            } else {
                holder.checkBoxLiked.isChecked = false
                disLikedList.get(position).checkForDisLike = false
            }

        }

        holder.tvFriendName.text = friendDataClass.name

        holder.checkBoxLiked.setOnClickListener()
        {

            if (holder.checkBoxLiked.isChecked == true) {
                disLikedList.get(position).checkForDisLike = true

                holder.checkBoxLiked.isChecked = true
                Global.dislikedItemsSet.add(disLikedList.get(position).id.toString())

            } else {
                disLikedList.get(position).checkForDisLike = false
                holder.checkBoxLiked.isChecked = false
                var ingrId = disLikedList.get(position).id.toString()

                for (i in 0 until Global.dislikedItemsSet.size) {
                    if (Global.dislikedItemsSet.contains(ingrId)) {
                        Global.dislikedItemsSet.remove(ingrId)
                    }
                }
            }
        }
    }


    override fun getItemCount(): Int {

        return disLikedList.size
    }

    fun addItems(dataItems: ArrayList<Data_Disliked>) {
        disLikedList.addAll(dataItems)
        dislikeActivity.runOnUiThread(Runnable { dislikeActivity.adapter?.notifyDataSetChanged() })
    }

    // clearing the list for searched item
    fun clearLikedList() {
        disLikedList.clear()
        notifyDataSetChanged()
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var checkBoxLiked: CheckBox
        internal var tvFriendName: TextView

        init {
            checkBoxLiked = view.findViewById(R.id.checkBoxLiked) as CheckBox
            tvFriendName = view.findViewById(R.id.tvFriendName) as TextView
        }
    }


}

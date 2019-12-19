package com.dish.seekdish.ui.navDrawer.settings.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.settings.activity.LikeActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Liked
import kotlin.collections.ArrayList
import com.dish.seekdish.util.Global


class LikeAdapter(
    val likeActivity: LikeActivity, var likedList: ArrayList<Data_Liked>
//    val likeActivity: LikeActivity
) :
    RecyclerView.Adapter<LikeAdapter.RecyclerViewHolder>() {

//    private var likedList: ArrayList<Data_Liked> = ArrayList<Data_Liked>()

    var flag: Boolean = false
    var count: Int = 0


    fun LikeAdapter(dataItems: ArrayList<Data_Liked>) {
        this.likedList = dataItems
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_like, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val friendDataClass = likedList[position]
        // getting all vales and storing in val...
        if (friendDataClass.liked == 1) {
            holder.checkBoxLiked.isChecked = true

            Global.likedItemsSet.add(likedList[position].id.toString())

//            likedList.get(position).checkForLike = true
            if (friendDataClass.checkForLike == false) {
                Log.e("like", "if entered on " + position)
                holder.checkBoxLiked.isChecked = true
                likedList.get(position).checkForLike = true
            } else {
                Log.e("like", "else entered on " + position)
                holder.checkBoxLiked.isChecked = true
                likedList.get(position).checkForLike = true
            }

        } else if (friendDataClass.liked == 0 || friendDataClass.checkForLike) {

            if (friendDataClass.checkForLike) {
                holder.checkBoxLiked.isChecked = true
                likedList.get(position).checkForLike = true
            } else {
                holder.checkBoxLiked.isChecked = false
                likedList.get(position).checkForLike = false
            }

        } else if (friendDataClass.liked == 2 || friendDataClass.checkForLike) {
            Log.e("checkbox liked", "" + friendDataClass.liked)

            if (friendDataClass.checkForLike) {
                holder.checkBoxLiked.isChecked = true
                likedList.get(position).checkForLike = true
            } else {
                holder.checkBoxLiked.isChecked = false
                likedList.get(position).checkForLike = false
            }

        }

        holder.tvFriendName.text = friendDataClass.name

        holder.checkBoxLiked.setOnClickListener()
        {

            if (holder.checkBoxLiked.isChecked == true) {
                Log.e("checkbox", "turned true")
                likedList.get(position).checkForLike = true
                Log.e("statLikeif", " " + likedList.get(position).checkForLike +"   "+ "position:  " + likedList.get(position).id+"  name:"+likedList.get(position).name)
                holder.checkBoxLiked.isChecked = true
                Global.likedItemsSet.add(likedList.get(position).id.toString())
            } else {
                Log.e("checkbox", "turned false")
                likedList.get(position).checkForLike = false
                Log.e("statLikeelse", " " + likedList.get(position).checkForLike+"   " + "position:  " + likedList.get(position).id+"  name:"+likedList.get(position).name)
                holder.checkBoxLiked.isChecked = false
                var ingrId = likedList.get(position).id.toString()

                for (i in 0 until Global.likedItemsSet.size) {
                    if (Global.likedItemsSet.contains(ingrId)) {
                        Global.likedItemsSet.remove(ingrId)
                    }
                }
            }

               Log.e("itemsLiked","  :"+Global.likedItemsSet.toString())

        }


    }


    fun addItems(dataItems: ArrayList<Data_Liked>) {

        Log.e("size of likelist before", "" + likedList.size)


        likedList.addAll(dataItems)

        Log.e("size of likelist after", "" + likedList.size)

        likeActivity.runOnUiThread(Runnable { likeActivity.adapter?.notifyDataSetChanged() })
    }


    // clearing the list for searched item
     fun clearLikedList() {
        likedList.clear()
        notifyDataSetChanged()
    }


    fun dataChanged() {
        likeActivity.runOnUiThread(Runnable { likeActivity.adapter?.notifyDataSetChanged() })

    }

    override fun getItemCount(): Int {

        return likedList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var checkBoxLiked: CheckBox
        internal var tvFriendName: TextView

        init {
            checkBoxLiked = view.findViewById(R.id.checkBoxLike) as CheckBox
            tvFriendName = view.findViewById(R.id.tvFriendName) as TextView
        }
    }


}

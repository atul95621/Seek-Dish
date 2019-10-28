package com.dish.seekdish.ui.navDrawer.toDo.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList

class ListTodoAdapter(
    arrayList: ArrayList<ListTodoDataClass>,
    activity: HomeActivity
) :
    RecyclerView.Adapter<ListTodoAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<ListTodoDataClass>()

    var activity: HomeActivity

    init {
        this.arrayList = arrayList
        this.activity = activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_list_todo_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val listDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = listDataClass.foodImageUrl.toString()
//        Glide.with(this).load(imageUrl).apply(options).into(holder.imgFoodImage);
        holder.tvDishName.text = listDataClass.dishName
        holder.tvDistance.text = listDataClass.distance
        var review: String = "(" + listDataClass.startReview + ")"
        holder.tvStarReview.text = review
        var startRating = listDataClass.startRating!!.toFloat()
        holder.starScaleRatingBar.rating = startRating
        var euroScaleRatingBar = listDataClass.euroRating!!.toFloat()
        holder.euroScaleRatingBar.rating = euroScaleRatingBar

        holder.frameTasteDish.setOnClickListener()
        {
            /*  val intent = Intent(activity, DishDescriptionActivity::class.java)
              activity.startActivity(intent)*/
        }
    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgFoodImage: ImageView
        internal var tvStarReview: TextView
        internal var tvDistance: TextView
        internal var tvDishName: TextView
        internal var starScaleRatingBar: ScaleRatingBar
        internal var euroScaleRatingBar: ScaleRatingBar
        internal var frameTasteDish: FrameLayout

        init {
            starScaleRatingBar = view.findViewById(R.id.simpleRatingBar) as ScaleRatingBar
            euroScaleRatingBar = view.findViewById(R.id.euroSignRatingBar) as ScaleRatingBar
            imgFoodImage = view.findViewById(R.id.imgFoodImage) as ImageView
            tvDistance = view.findViewById(R.id.tvDistance) as TextView
            tvStarReview = view.findViewById(R.id.tvStarReview) as TextView
            tvDishName = view.findViewById(R.id.tvDishName) as TextView
            frameTasteDish = view.findViewById(R.id.frameTasteDish) as FrameLayout

        }
    }

/*
    fun removeItem(position: Int) {
        arrayList.removeAt(position)
        notifyItemRemoved(position)
    }

//    public void restoreItem(String item, int position) {
//        data.add(position, item);
//        notifyItemInserted(position);
//    }

    fun getData(): ArrayList<ListTodoDataClass> {
        return arrayList
    }*/


}

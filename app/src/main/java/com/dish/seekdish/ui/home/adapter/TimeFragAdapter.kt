package com.dish.seekdish.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.home.dataModel.Data_time
import com.dish.seekdish.ui.home.dataModel.TimeFragDataClass
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList

class TimeFragAdapter(
    context: Context,
    arrayList: ArrayList<Data_time>,
    homeActivity: HomeActivity
) :
    RecyclerView.Adapter<TimeFragAdapter.RecyclerViewHolder>() {

    var homeActivity: HomeActivity
    internal var arrayList = ArrayList<Data_time>()
    var context: Context

    init {
        this.arrayList = arrayList
        this.homeActivity = homeActivity
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_time_frag, parent, false)
        return RecyclerViewHolder(view)
    }


    override fun onBindViewHolder(holder: TimeFragAdapter.RecyclerViewHolder, position: Int) {
        val timeFragDataClass = arrayList[position]


        // getting all vales and storing in val...
        var imageUrl: String = timeFragDataClass.meal_image
        GlideApp.with(context)
            .load(imageUrl)
            .into(holder.imgFoodImage)
        holder.tvDishName.text = timeFragDataClass.name
        var dist = timeFragDataClass.distance
        holder.tvDistance.text = String.format("%.2f", dist) + " Km"
        var review: String = "(" + timeFragDataClass.no_of_reviews + ")"
        holder.tvStarReview.text = review
        var startRating = timeFragDataClass.rating!!.toFloat()
        holder.starScaleRatingBar.rating = startRating
        var euroScaleRatingBar = timeFragDataClass.budget!!.toFloat()
        holder.euroScaleRatingBar.rating = euroScaleRatingBar

        holder.tvPrice.text = timeFragDataClass.meal_symbol + " " + timeFragDataClass.meal_price

        holder.frameTimeDish.setOnClickListener()
        {
            val intent = Intent(context, DishDescriptionActivity::class.java)
            intent.putExtra("MEAL_ID", timeFragDataClass.meal_id.toString())
            intent.putExtra("RESTAURANT_ID", timeFragDataClass.restro_id.toString())
            context.startActivity(intent)
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
        internal var frameTimeDish: FrameLayout
        internal var tvPrice: TextView

        init {
            starScaleRatingBar = view.findViewById(R.id.simpleRatingBar) as ScaleRatingBar
            euroScaleRatingBar = view.findViewById(R.id.euroSignRatingBar) as ScaleRatingBar
            imgFoodImage = view.findViewById(R.id.imgFoodImage) as ImageView
            tvDistance = view.findViewById(R.id.tvDistance) as TextView
            tvStarReview = view.findViewById(R.id.tvStarReview) as TextView
            tvDishName = view.findViewById(R.id.tvDishName) as TextView
            frameTimeDish = view.findViewById(R.id.frameTimeDish) as FrameLayout
            tvPrice = view.findViewById(R.id.tvPrice) as TextView

        }
    }


    fun addItems(dataItems: ArrayList<Data_time>) {
        arrayList.addAll(dataItems)
        homeActivity.runOnUiThread(Runnable { notifyDataSetChanged() })
    }


}

package com.dish.seekdish.ui.navDrawer.dishDescription.similar

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList

class SimilarAdapter(
    arrayList: ArrayList<SimilarDataClass>,
    activity: DishDescriptionActivity
) :
    RecyclerView.Adapter<SimilarAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<SimilarDataClass>()

    var activity: DishDescriptionActivity

    init {
        this.arrayList = arrayList
        this.activity = activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_similar_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val tasteDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = tasteDataClass.foodImageUrl.toString()
//        Glide.with(this).load(imageUrl).apply(options).into(holder.imgFoodImage);
        holder.tvDishName.text = tasteDataClass.dishName
        holder.tvDistance.text = tasteDataClass.distance
        var review: String = "(" + tasteDataClass.startReview + ")"
        holder.tvStarReview.text = review
        var startRating = tasteDataClass.startRating!!.toFloat()
        holder.starScaleRatingBar.rating = startRating
        var euroScaleRatingBar = tasteDataClass.euroRating!!.toFloat()
        holder.euroScaleRatingBar.rating = euroScaleRatingBar

        holder.frameTasteDish.setOnClickListener()
        {
            val intent = Intent(activity, DishDescriptionActivity::class.java)
            activity.startActivity(intent)
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


}

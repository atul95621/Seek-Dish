package com.dish.seekdish.ui.navDrawer.restaurantDiscription.meals

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import com.willy.ratingbar.ScaleRatingBar
import java.text.DecimalFormat
import java.util.ArrayList



class MealsAdapter(
    arrayList: ArrayList<MealsDataClass>,
    activity: RestroDescrpActivity
) :
    RecyclerView.Adapter<MealsAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<MealsDataClass>()

    var activity: RestroDescrpActivity

    init {
        this.arrayList = arrayList
        this.activity = activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.dish.seekdish.R.layout.item_layout_restro_meal_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val tasteDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = tasteDataClass.foodImageUrl.toString()
        Glide.with(activity).load(imageUrl).placeholder(R.drawable.app_logo).into(holder.imgFoodImage);
        holder.tvDishName.text = tasteDataClass.dishName
        var dist = tasteDataClass.distance?.toDouble()
        holder.tvDistance.setText(DecimalFormat("##.##").format(dist)+" km")
        var review: String = "(" + tasteDataClass.startReview + ")"
        holder.tvStarReview.text = review
        var startRating = tasteDataClass.startRating!!.toFloat()
        holder.starScaleRatingBar.rating = startRating
        var euroScaleRatingBar = tasteDataClass.euroRating!!.toFloat()
        holder.euroScaleRatingBar.rating = euroScaleRatingBar

        holder.tvPrice.text = tasteDataClass.symbol + " " + tasteDataClass.price

        holder.frameTasteDish.setOnClickListener()
        {
            val intent = Intent(activity, DishDescriptionActivity::class.java)
            intent.putExtra("MEAL_ID",tasteDataClass.mealId.toString())
            intent.putExtra("RESTAURANT_ID",tasteDataClass.restroId.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
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
        internal  var tvPrice:TextView

        init {
            starScaleRatingBar = view.findViewById(com.dish.seekdish.R.id.simpleRatingBar) as ScaleRatingBar
            euroScaleRatingBar = view.findViewById(com.dish.seekdish.R.id.euroSignRatingBar) as ScaleRatingBar
            imgFoodImage = view.findViewById(com.dish.seekdish.R.id.imgFoodImage) as ImageView
            tvDistance = view.findViewById(com.dish.seekdish.R.id.tvDistance) as TextView
            tvStarReview = view.findViewById(com.dish.seekdish.R.id.tvStarReview) as TextView
            tvDishName = view.findViewById(com.dish.seekdish.R.id.tvDishName) as TextView
            frameTasteDish = view.findViewById(com.dish.seekdish.R.id.frameTasteDish) as FrameLayout
            tvPrice = view.findViewById(R.id.tvPrice) as TextView

        }
    }


}

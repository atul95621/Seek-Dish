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
import com.dish.seekdish.ui.home.dataModel.Data_Taste
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList


class TasteFragAdapter(
    context: Context,
    arrayList: ArrayList<Data_Taste>,
    homeActivity: HomeActivity
) :
    RecyclerView.Adapter<TasteFragAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Taste>()


    var context: Context
    var homeActivity: HomeActivity

    init {
        this.arrayList = arrayList
        this.context = context
        this.homeActivity = homeActivity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_taste_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val tasteDataClass = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = tasteDataClass.meal_image
        GlideApp.with(context)
            .load(imageUrl)
            .into(holder.imgFoodImage)
        holder.tvDishName.text = tasteDataClass.name
       var dist= tasteDataClass.distance
        holder.tvDistance.text =String.format("%.2f", dist) +" Km"
        var review: String = "(" + tasteDataClass.no_of_reviews + ")"
        holder.tvStarReview.text = review
        var startRating = tasteDataClass.rating!!.toFloat()
        holder.starScaleRatingBar.rating = startRating
        var euroScaleRatingBar = tasteDataClass.budget!!.toFloat()
        holder.euroScaleRatingBar.rating = euroScaleRatingBar

        holder.frameTasteDish.setOnClickListener()
        {
            val intent = Intent(context, DishDescriptionActivity::class.java)
            intent.putExtra("MEAL_ID",tasteDataClass.meal_id.toString())
            intent.putExtra("RESTAURANT_ID",tasteDataClass.restro_id.toString())
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

    fun addItems(dataItems: ArrayList<Data_Taste>) {

        Log.e("size of likelist before", "" + arrayList.size)


        arrayList.addAll(dataItems)

        Log.e("size of likelist after", "" + arrayList.size)

        homeActivity.runOnUiThread(Runnable { notifyDataSetChanged() })
    }


}

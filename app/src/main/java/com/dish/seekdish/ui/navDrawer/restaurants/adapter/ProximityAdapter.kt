package com.dish.seekdish.ui.navDrawer.restaurants.adapter

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
import com.dish.seekdish.ui.WebViewActivity
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.Data_Proximity
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList

class ProximityAdapter(
    context: Context,
    arrayList: ArrayList<Data_Proximity>,
    homeActivity: HomeActivity

) :
    RecyclerView.Adapter<ProximityAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Proximity>()

    var context: Context
    var homeActivity: HomeActivity

    init {
        this.arrayList = arrayList
        this.homeActivity = homeActivity
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_proximity_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val proxiModel = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = proxiModel.restaurant_image
        GlideApp.with(context)
            .load(imageUrl)
            .into(holder.imgFoodImage)
        holder.tvDishName.text = proxiModel.name
        holder.tvAddress.text = proxiModel.street + "," + proxiModel.city + "," + proxiModel.zipcode
        var review: String = "(" + proxiModel.no_of_reviews + ")"
        var startRating = proxiModel.rating.toFloat()
        holder.starScaleRatingBar.rating = startRating
        var dist = proxiModel.distance
        holder.tvDistance.text = String.format("%.2f", dist) + " Km"

        holder.frameTasteDish.setOnClickListener()
        {
            val intent = Intent(homeActivity, RestroDescrpActivity::class.java)
            intent.putExtra("RESTAURANT_ID", proxiModel.id.toString())
            homeActivity.startActivity(intent)
        }

        if (proxiModel.meal_count != null && proxiModel.meal_count > 0) {
            holder.frameMealCount.visibility = View.VISIBLE
            if (proxiModel.meal_count > 99) {
                holder.tvMealCount.text = "99+"
            } else {
                holder.tvMealCount.text = proxiModel.meal_count.toString()
            }
        } else {
            holder.frameMealCount.visibility = View.GONE
        }

        if (proxiModel.menu_link != null) {
            holder.imgMenu.visibility = View.VISIBLE
            holder.imgMenu.setOnClickListener()
            {
                val intent = Intent(homeActivity, WebViewActivity::class.java)
                intent.putExtra("url", proxiModel.menu_link)
                intent.putExtra("from", "OPEN_PDF")
                homeActivity.startActivity(intent)
            }
        } else {
            holder.imgMenu.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgFoodImage: ImageView
        internal var tvAddress: TextView
        internal var tvDishName: TextView
        internal var tvDistance: TextView
        internal var starScaleRatingBar: ScaleRatingBar
        internal var frameTasteDish: FrameLayout
        internal var tvMealCount: TextView
        internal var frameMealCount: FrameLayout
        internal var imgMenu: ImageView


        init {
            tvDistance = view.findViewById(R.id.tvDistance) as TextView
            starScaleRatingBar = view.findViewById(R.id.simpleRatingBar) as ScaleRatingBar
            imgFoodImage = view.findViewById(R.id.imgFoodImage) as ImageView
            tvAddress = view.findViewById(R.id.tvAddress) as TextView
            tvDishName = view.findViewById(R.id.tvDishName) as TextView
            frameTasteDish = view.findViewById(R.id.frameTasteDish) as FrameLayout
            tvMealCount = view.findViewById(R.id.tvMealCount) as TextView
            frameMealCount = view.findViewById(R.id.frameMealCount) as FrameLayout
            imgMenu = view.findViewById(R.id.imgMenu) as ImageView
        }
    }

    fun addItems(dataItems: ArrayList<Data_Proximity>) {
        arrayList.addAll(dataItems)
        homeActivity.runOnUiThread(Runnable { notifyDataSetChanged() })
    }


}

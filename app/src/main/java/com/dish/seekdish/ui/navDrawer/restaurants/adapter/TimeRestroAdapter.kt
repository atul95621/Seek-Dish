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
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.Data_Time_Restro
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList

class TimeRestroAdapter(
    context: Context,
    arrayList: ArrayList<Data_Time_Restro>,
    homeActivity: HomeActivity
) :
    RecyclerView.Adapter<TimeRestroAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_Time_Restro>()

    var context: Context
    var homeActivity: HomeActivity
    init {
        this.arrayList = arrayList
        this.homeActivity=homeActivity
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_time_restro_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val restroTimeModel = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = restroTimeModel.restaurant_image
        GlideApp.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.app_logo)
            .into(holder.imgFoodImage)
        holder.tvDishName.text = restroTimeModel.name
        holder.tvAddress.text = restroTimeModel.street+","+restroTimeModel.city+","+restroTimeModel.zipcode
        var review: String="("+restroTimeModel.no_of_reviews+")"
        var startRating = restroTimeModel.rating.toFloat()
        holder.starScaleRatingBar.rating = startRating
        var dist= restroTimeModel.distance
        holder.tvDistance.text =String.format("%.2f", dist) +" Km"


        holder.frameTasteDish.setOnClickListener()
        {
            val intent = Intent(homeActivity, RestroDescrpActivity::class.java)
            intent.putExtra("RESTAURANT_ID",restroTimeModel.id.toString())
            homeActivity.startActivity(intent)
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

        init {
            starScaleRatingBar = view.findViewById(R.id.simpleRatingBar) as ScaleRatingBar
            imgFoodImage = view.findViewById(R.id.imgFoodImage) as ImageView
            tvAddress = view.findViewById(R.id.tvAddress) as TextView
            tvDistance = view.findViewById(R.id.tvDistance) as TextView
            tvDishName = view.findViewById(R.id.tvDishName) as TextView
            frameTasteDish = view.findViewById(R.id.frameTasteDish) as FrameLayout

        }
    }



    fun addItems(dataItems: ArrayList<Data_Time_Restro>) {

        Log.e("size of likelist before", "" + arrayList.size)
        arrayList.addAll(dataItems)

        Log.e("size of likelist after", "" + arrayList.size)

        homeActivity.runOnUiThread(Runnable { notifyDataSetChanged() })
    }


}

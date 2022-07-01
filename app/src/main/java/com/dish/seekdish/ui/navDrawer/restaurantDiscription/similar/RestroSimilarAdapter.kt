package com.dish.seekdish.ui.navDrawer.restaurantDiscription.similar

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.WebViewActivity
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.SimilarRestaurant
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList


class RestroSimilarAdapter(
    arrayList: ArrayList<SimilarRestaurant>,
    mcontext: RestroDescrpActivity
) :
    RecyclerView.Adapter<RestroSimilarAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<SimilarRestaurant>()

    var mcontext: RestroDescrpActivity

    init {
        this.arrayList = arrayList
        this.mcontext = mcontext
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_restro_similar, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val restroSimilarModel = arrayList[position]

        // getting all vales and storing in val...
        var imageUrl: String = restroSimilarModel.restaurant_image
        GlideApp.with(mcontext)
            .load(imageUrl)
//            .placeholder(R.drawable.app_logo)
            .into(holder.imgFoodImage)
        holder.tvDishName.text = restroSimilarModel.name
        holder.tvAddress.text =
            restroSimilarModel.street + "," + restroSimilarModel.city + "," + restroSimilarModel.zipcode
        var review: String = "(" + restroSimilarModel.no_of_reviews + ")"
        var startRating = restroSimilarModel.rating.toFloat()
        holder.tvSimpleRatingBar.text = startRating.toString()
        var dist = restroSimilarModel.distance
        var convertDist = dist.toDouble()
        var floatDista = convertDist.toFloat()
        holder.tvDistance.text = String.format("%.2f", floatDista) + " Km"

        holder.frameTasteDish.setOnClickListener()
        {
            val intent = Intent(mcontext, RestroDescrpActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restroSimilarModel.id.toString())
            mcontext.startActivity(intent)
        }

        if (restroSimilarModel.meal_count != null && restroSimilarModel.meal_count > 0) {
            holder.frameMealCount.visibility = View.VISIBLE
            if (restroSimilarModel.meal_count > 99) {
                holder.tvMealCount.text = "99+"
            } else {
                holder.tvMealCount.text = restroSimilarModel.meal_count.toString()
            }
        } else {
            holder.frameMealCount.visibility = View.GONE
        }

        if (restroSimilarModel.menu_link != null) {
            holder.imgMenu.visibility = View.VISIBLE
            holder.imgMenu.setOnClickListener()
            {
                val intent = Intent(mcontext, WebViewActivity::class.java)
                intent.putExtra("url", restroSimilarModel.menu_link)
                intent.putExtra("from", "OPEN_PDF")
                mcontext.startActivity(intent)
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
        internal var tvSimpleRatingBar: TextView
        internal var frameTasteDish: LinearLayout
        internal var tvMealCount: TextView
        internal var frameMealCount: FrameLayout
        internal var imgMenu: ImageView

        init {
            tvDistance = view.findViewById(R.id.tvDistance) as TextView
            tvSimpleRatingBar = view.findViewById(R.id.tvSimpleRatingBar) as TextView
            imgFoodImage = view.findViewById(R.id.imgFoodImage) as ImageView
            tvAddress = view.findViewById(R.id.tvAddress) as TextView
            tvDishName = view.findViewById(R.id.tvDishName) as TextView
            frameTasteDish = view.findViewById(R.id.frameTasteDish) as LinearLayout
            tvMealCount = view.findViewById(R.id.tvMealCount) as TextView
            frameMealCount = view.findViewById(R.id.frameMealCount) as FrameLayout
            imgMenu = view.findViewById(R.id.imgMenu) as ImageView
        }
    }
}

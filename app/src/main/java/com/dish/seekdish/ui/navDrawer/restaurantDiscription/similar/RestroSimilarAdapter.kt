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
            .placeholder(R.drawable.app_logo)
            .into(holder.imgFoodImage)
        holder.tvDishName.text = restroSimilarModel.name
        holder.tvAddress.text = restroSimilarModel.street+","+restroSimilarModel.city+","+restroSimilarModel.zipcode
        var review: String="("+restroSimilarModel.no_of_reviews+")"
        var startRating = restroSimilarModel.rating.toFloat()
        Log.e("rating",""+startRating+"   "+restroSimilarModel.name)
        holder.starScaleRatingBar.rating = startRating
        var dist= restroSimilarModel.distance
        var convertDist= dist.toDouble()
        var floatDista=convertDist.toFloat()
        holder.tvDistance.text =String.format("%.2f", floatDista) +" Km"

        holder.frameTasteDish.setOnClickListener()
        {
            val intent = Intent(mcontext, RestroDescrpActivity::class.java)
            intent.putExtra("RESTAURANT_ID",restroSimilarModel.id.toString())
            mcontext.startActivity(intent)
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
            tvDistance = view.findViewById(R.id.tvDistance) as TextView
            starScaleRatingBar = view.findViewById(R.id.simpleRatingBar) as ScaleRatingBar
            imgFoodImage = view.findViewById(R.id.imgFoodImage) as ImageView
            tvAddress = view.findViewById(R.id.tvAddress) as TextView
            tvDishName = view.findViewById(R.id.tvDishName) as TextView
            frameTasteDish = view.findViewById(R.id.frameTasteDish) as FrameLayout

        }
    }
}

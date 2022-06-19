package com.dish.seekdish.ui.navDrawer.myFavourite.adapter

import android.content.Intent
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
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.myFavourite.fragment.ListFavouriteFragment
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Req
import com.dish.seekdish.ui.navDrawer.toDo.list.Data_todo
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList

class ListFragAdapter(
    arrayList: ArrayList<Data_todo>,
    activity: HomeActivity,
    var listFavouriteFragment: ListFavouriteFragment
) :
    RecyclerView.Adapter<ListFragAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<Data_todo>()

    var activity: HomeActivity

    init {
        this.arrayList = arrayList
        this.activity = activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_list_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val listDataClass = arrayList[position]
/*
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
            *//*  val intent = Intent(activity, DishDescriptionActivity::class.java)
              activity.startActivity(intent)*//*
        }*/

        // getting all vales and storing in val...
        var imageUrl: String = listDataClass.meal_image
        GlideApp.with(activity)
            .load(imageUrl)
            .placeholder(R.drawable.app_logo)
            .into(holder.imgFoodImage)
        holder.tvDishName.text = listDataClass.name
        var dist = listDataClass.distance
        holder.tvDistance.text = String.format("%.2f", dist) + " Km"
        var review: String = "(" + listDataClass.no_of_reviews + ")"
        holder.tvStarReview.text = review
        var startRating = listDataClass.meal_avg_rating.toFloat()
        holder.tvSimpleRatingBar.text = startRating.toString()
        var euroScaleRatingBar = listDataClass.budget_rating.toFloat()

        holder.tvPrice.text = listDataClass.meal_symbol + " " + listDataClass.meal_price

        holder.frameTasteDish.setOnClickListener()
        {
            val intent = Intent(activity, DishDescriptionActivity::class.java)
            intent.putExtra("MEAL_ID", listDataClass.meal_id.toString())
            intent.putExtra("RESTAURANT_ID", listDataClass.restro_id.toString())
            activity.startActivity(intent)
        }

        holder.imgDelete.setOnClickListener()
        {

            var mealId = listDataClass.meal_id.toString()
            var restroId = listDataClass.restro_id.toString()
            listFavouriteFragment.deleteItemFromFavList(mealId, restroId, position)
        }


    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgFoodImage: ImageView
        internal var imgDelete: ImageView
        internal var tvStarReview: TextView
        internal var tvDistance: TextView
        internal var tvDishName: TextView
        internal var tvSimpleRatingBar: TextView
        internal var frameTasteDish: LinearLayout
        internal var tvPrice: TextView

        init {
            tvSimpleRatingBar = view.findViewById(R.id.tvSimpleRatingBar) as TextView
            imgFoodImage = view.findViewById(R.id.imgFoodImage) as ImageView
            imgDelete = view.findViewById(R.id.imgDelete) as ImageView
            tvDistance = view.findViewById(R.id.tvDistance) as TextView
            tvStarReview = view.findViewById(R.id.tvStarReview) as TextView
            tvDishName = view.findViewById(R.id.tvDishName) as TextView
            frameTasteDish = view.findViewById(R.id.frameTasteDish) as LinearLayout
            tvPrice = view.findViewById(R.id.tvPrice) as TextView
        }
    }

    fun updateList(list: ArrayList<Data_todo>) {
        arrayList = list
        notifyDataSetChanged()
    }


    /*   fun removeItem(position: Int) {
           arrayList.removeAt(position)
           notifyItemRemoved(position)
       }

   //    public void restoreItem(String item, int position) {
   //        data.add(position, item);
   //        notifyItemInserted(position);
   //    }

       fun getData(): ArrayList<ListFragDataClass> {
           return arrayList
       }
   */

}

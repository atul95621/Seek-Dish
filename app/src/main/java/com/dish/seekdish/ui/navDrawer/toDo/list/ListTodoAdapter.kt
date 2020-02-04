package com.dish.seekdish.ui.navDrawer.toDo.list

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
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Req
import com.dish.seekdish.ui.navDrawer.toDo.VM.TodoVM
import com.willy.ratingbar.ScaleRatingBar
import java.util.ArrayList

class ListTodoAdapter(
    arrayListPassed: ArrayList<Data_todo>,
    activity: HomeActivity,
    var listTodoFragment: ListTodoFragment
) :
    RecyclerView.Adapter<ListTodoAdapter.RecyclerViewHolder>() {

    var activity: HomeActivity
    internal var arrayList = ArrayList<Data_todo>()

    init {
        this.arrayList = arrayListPassed
        this.activity = activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_list_todo_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val listDataClass = arrayList[position]

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
        holder.starScaleRatingBar.rating = startRating
        var euroScaleRatingBar = listDataClass.budget_rating.toFloat()
        holder.euroScaleRatingBar.rating = euroScaleRatingBar


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
            listTodoFragment.deleteItemFromTodoList(mealId, restroId, position)
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
        internal var imgDelete: ImageView

        init {
            starScaleRatingBar = view.findViewById(R.id.starRating) as ScaleRatingBar
            euroScaleRatingBar = view.findViewById(R.id.euroRate) as ScaleRatingBar
            imgFoodImage = view.findViewById(R.id.imgFoodImage) as ImageView
            imgDelete = view.findViewById(R.id.imgDelete) as ImageView
            tvDistance = view.findViewById(R.id.tvDistance) as TextView
            tvStarReview = view.findViewById(R.id.tvStarReview) as TextView
            tvDishName = view.findViewById(R.id.tvDishName) as TextView
            frameTasteDish = view.findViewById(R.id.frameTasteDish) as FrameLayout
        }
    }

    fun removeItem(position: Int) {
        Log.e("tagh", "position: $position+ totalitem: ${arrayList.size}")
        arrayList.removeAt(position)
        notifyDataSetChanged()
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position,arrayList.size);
//        notifyDataSetChanged()
    }

    fun updateList(list: ArrayList<Data_todo>) {
        arrayList = list
        notifyDataSetChanged()
    }


}

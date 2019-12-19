package com.dish.seekdish.ui.navDrawer.dishDescription.opinion

import android.content.Intent
import android.util.Half.toFloat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.OpinionDetailsActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.model.UserMealComment
import java.util.ArrayList


class OpinionAdapter(
    arrayList: ArrayList<OpinionDataClass>,
    mcontext: DishDescriptionActivity,
    var opinionDetails: List<UserMealComment>
) :
    RecyclerView.Adapter<OpinionAdapter.RecyclerViewHolder>(), java.io.Serializable {
    internal var arrayList = ArrayList<OpinionDataClass>()

    var mcontext: DishDescriptionActivity

    init {
        this.arrayList = arrayList
        this.mcontext = mcontext
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_opinion, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val opinionModel = arrayList[position]

        if (opinionModel.anononymous == 0) {
            holder.tvCheckinName.text = opinionModel.opinionTitle + " : " + opinionModel.comment
        } else {
            holder.tvCheckinName.text = "Anonymous" + " : " + opinionModel.comment
        }
        holder.tvCheckinDate.text = opinionModel.opinionDate

        GlideApp.with(mcontext)
            .load(opinionModel.imageUrl)
            .placeholder(R.drawable.ic_applogo_small)
            .into(holder.imgplace)

        holder.linOpinion.setOnClickListener()
        {

            val intent = Intent(mcontext, OpinionDetailsActivity::class.java)
            intent.putExtra("TASTE_RATING", opinionDetails[position].taste_rating.toFloat())
            intent.putExtra("AMBIANCE_RATING", opinionDetails[position].ambiance_rating.toFloat())
            intent.putExtra("CLEAN_RATING", opinionDetails[position].cleanness_rating.toFloat())
            intent.putExtra("DECOR_RATING", opinionDetails[position].decore_rating.toFloat())
            intent.putExtra("ODOR_RATING", opinionDetails[position].odor_rating.toFloat())
            intent.putExtra("PRESENTATION_RATING", opinionDetails[position].presentation_rating.toFloat())
            intent.putExtra("SERVICE_RATING", opinionDetails[position].service_rating.toFloat())
            intent.putExtra("TEXTURE_RATING", opinionDetails[position].texture_rating.toFloat())
            intent.putExtra("IS_FOLLOWER", opinionDetails[position].follower.toString())
            intent.putExtra("IS_PRIVATE", opinionDetails[position].private.toString())
            intent.putExtra("IS_FRIEND", opinionDetails[position].friend.toString())
            intent.putExtra("IS_ANNONYMOUS", opinionDetails[position].anonymous.toString())
            intent.putExtra("AVG_RATING", opinionDetails[position].meal_avg_rating.toFloat())
            intent.putExtra("BUDGET_RATING", opinionDetails[position].budget.toFloat())
            intent.putExtra("MEAL_NAME", opinionDetails[position].name)
            intent.putExtra("DATE", opinionDetails[position].published_on)
            intent.putExtra("USERNAME", opinionDetails[position].username)
            intent.putExtra("COMMENT", opinionDetails[position].comment)
            intent.putExtra("MEAL_IMAGE", opinionDetails[position].meal_image)
            intent.putExtra("COMMENT_USER_ID", opinionDetails[position].user_id)

            var size_images_arr = opinionDetails[position].comment_images.size

            if (size_images_arr == 1) {
                var image1 = opinionDetails[position].comment_images[0].image1
                intent.putExtra("COMMENT_IMAGE_1", image1.toString())
            } else if (size_images_arr == 2) {
                var image1 = opinionDetails[position].comment_images[0].image1
                intent.putExtra("COMMENT_IMAGE_1", image1.toString())
                var image2 = opinionDetails[position].comment_images[0].image2
                intent.putExtra("COMMENT_IMAGE_2", image2.toString())
            } else {

            }


            mcontext.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvCheckinName: TextView
        internal var tvCheckinDate: TextView
        internal var imgplace: ImageView
        internal var linOpinion: LinearLayout


        init {
            tvCheckinName = view.findViewById(R.id.tvCheckinName) as TextView
            tvCheckinDate = view.findViewById(R.id.tvCheckinDate) as TextView
            imgplace = view.findViewById(R.id.imgplace) as ImageView
            linOpinion = view.findViewById(R.id.linOpinion) as LinearLayout

        }
    }


}

package com.dish.seekdish.ui.navDrawer.dishDescription.opinion

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import com.dish.seekdish.ui.navDrawer.dishDescription.OpinionDetailsActivity
import java.util.ArrayList


class OpinionAdapter(arrayList: ArrayList<OpinionDataClass>, mcontext: DishDescriptionActivity) :
    RecyclerView.Adapter<OpinionAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<OpinionDataClass>()
    
    var mcontext: DishDescriptionActivity

    init {
        this.arrayList = arrayList
        this.mcontext=mcontext
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_opinion, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val OpinionDataClass = arrayList[position]

        // getting all vales and storing in val...
        // now setted to the textview
        holder.tvCheckinName.text = OpinionDataClass.opinionTitle
        holder.tvCheckinDate.text = OpinionDataClass.opinionDate
//        holder.imgplace.text = OpinionDataClass.imageUrl
        holder.linOpinion.setOnClickListener()
        {
            val intent = Intent(mcontext, OpinionDetailsActivity::class.java)
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

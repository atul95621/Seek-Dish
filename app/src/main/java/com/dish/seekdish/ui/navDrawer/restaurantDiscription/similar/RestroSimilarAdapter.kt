package com.dish.seekdish.ui.navDrawer.restaurantDiscription.similar

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import java.util.ArrayList




class RestroSimilarAdapter(arrayList: ArrayList<RestroSimilarDataClass>, mcontext: RestroDescrpActivity) :
    RecyclerView.Adapter<RestroSimilarAdapter.RecyclerViewHolder>() {
    internal var arrayList = ArrayList<RestroSimilarDataClass>()

    var mcontext: RestroDescrpActivity

    init {
        this.arrayList = arrayList
        this.mcontext=mcontext
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_restro_similar, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val RestroSimilarDataClass = arrayList[position]

        // getting all vales and storing in val...
        // now setted to the textview
        holder.tvCheckinName.text = RestroSimilarDataClass.opinionTitle
        holder.tvItemAddress.text = RestroSimilarDataClass.tvItemAddress
        holder.linRetroSimilar.setOnClickListener()
        {
            val intent = Intent(mcontext, RestroDescrpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            mcontext.startActivity(intent)
        }
//        holder.imgplace.text = RestroSimilarDataClass.imageUrl

    }


    override fun getItemCount(): Int {

        return arrayList.size
    }


    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvCheckinName: TextView
        internal var tvItemAddress: TextView
        internal var imgplace: ImageView
        internal var linRetroSimilar: LinearLayout


        init {
            tvCheckinName = view.findViewById(R.id.tvCheckinName) as TextView
            tvItemAddress = view.findViewById(R.id.tvItemAddress) as TextView
            imgplace = view.findViewById(R.id.imgplace) as ImageView
            linRetroSimilar = view.findViewById(R.id.linRetroSimilar) as LinearLayout

        }
    }


}

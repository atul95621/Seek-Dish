package com.dish.seekdish.ui.navDrawer.invitation.invited

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.navDrawer.dishDescription.OpinionDetailsActivity
import com.dish.seekdish.ui.navDrawer.invitation.InvitationActivity
import com.dish.seekdish.ui.navDrawer.settings.myAlerts.InvitedTabArray
import java.util.ArrayList

class InvitedAdapter(
    arrayList: ArrayList<InvitedDataClass>,
    mcontext: InvitationActivity,
    var invitedTabArray: ArrayList<InvitedTabArray>
) :
    RecyclerView.Adapter<InvitedAdapter.RecyclerViewHolder>() {

    var mcontext: InvitationActivity

    init {
        this.mcontext = mcontext
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_invited_frag, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val InvitedDataClass = invitedTabArray[position]

        // getting all vales and storing in val...
        // now setted to the textview
        holder.tvCheckinName.text = InvitedDataClass.username
        holder.tvCheckinDate.text = InvitedDataClass.invitation_status
        var imageUrl: String = InvitedDataClass.user_image
        GlideApp.with(mcontext)
            .load(imageUrl)
            .into(holder.imgplace)

      /*  holder.linOpinion.setOnClickListener()
        {
            val intent = Intent(mcontext, OpinionDetailsActivity::class.java)
            mcontext.startActivity(intent)
        }*/
    }


    override fun getItemCount(): Int {

        return invitedTabArray.size
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

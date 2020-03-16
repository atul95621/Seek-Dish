package com.dish.seekdish.ui.navDrawer.myFriends.contactFetch

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import de.hdodenhof.circleimageview.CircleImageView

class ContFetchAdapter(
    private val context: Context,
    private val contactModelArrayList: HashSet<Data>,
    var contactFetchActivity: ContactFetchActivity
) : BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return contactModelArrayList.size
    }

    override fun getItem(position: Int): Any {
        return contactModelArrayList.elementAt(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.list_contact_item, null, true)

            holder.tvname = convertView!!.findViewById(R.id.name) as TextView
            holder.tvnumber = convertView.findViewById(R.id.number) as TextView
            holder.linContactLayout =
                convertView.findViewById(R.id.linContactLayout) as LinearLayout

            holder.imgContactImage =
                convertView.findViewById(R.id.imgContactImage) as CircleImageView


            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.tvname!!.setText(contactModelArrayList.elementAt(position).username)
        holder.tvnumber!!.setText(contactModelArrayList.elementAt(position).phone)
        var imageUrl = contactModelArrayList.elementAt(position).user_image
        var userIdToSend = contactModelArrayList.elementAt(position).id

        if (imageUrl.isNullOrEmpty() == false) {
            GlideApp.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_user)
                .into(holder.imgContactImage!!)
        }
        holder.linContactLayout?.setOnClickListener()
        {
            contactFetchActivity.sendFriendRequest(userIdToSend)
        }

        return convertView
    }

    private inner class ViewHolder {

        var tvname: TextView? = null
        var tvnumber: TextView? = null
        var imgContactImage: ImageView? = null
        var linContactLayout: LinearLayout? = null

    }
}
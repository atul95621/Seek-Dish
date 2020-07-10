package com.dish.seekdish.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LangData

class CustomListAdapterDialog(context: Context, private val listData: ArrayList<LangData>) : BaseAdapter() {

    private val layoutInflater: LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): Any {
        return listData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_languages_dialog, null)
            holder = ViewHolder()
            holder.countryName = convertView!!.findViewById<View>(R.id.countryName) as TextView
            holder.countryId = convertView.findViewById<View>(R.id.countryId) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.countryName!!.setText(listData[position].name.toString())
        holder.countryId!!.setText(listData[position].id.toString())

        return convertView
    }

    internal class ViewHolder {
        var countryName: TextView? = null
        var countryId: TextView? = null
    }

}
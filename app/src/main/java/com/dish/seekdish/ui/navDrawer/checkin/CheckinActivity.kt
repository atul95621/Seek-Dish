package com.dish.seekdish.ui.navDrawer.checkin

import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.checkin.adapter.CheckinAdapter
import com.dish.seekdish.ui.navDrawer.checkin.data.CheckinDataClass
import kotlinx.android.synthetic.main.activity_my_profile.*


class CheckinActivity : BaseActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: CheckinAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    internal var arrayList = ArrayList<CheckinDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val checkinDataClass =
                CheckinDataClass("url", "Caille Grallie", "14/06/2019");
            arrayList.add(checkinDataClass)
        }


        adapter = CheckinAdapter(arrayList, this@CheckinActivity)
        recyclerView!!.setAdapter(adapter)


        tvBack.setOnClickListener()
        {
            finish()
        }
    }



}




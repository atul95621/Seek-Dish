package com.dish.seekdish.ui.navDrawer.settings.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R

import com.dish.seekdish.ui.navDrawer.settings.adapter.ReceivedRequestAdapter
import com.dish.seekdish.ui.navDrawer.settings.dataModel.ReceivedRequestDataClass
import java.util.ArrayList

class ReceivedRequestActivity : BaseActivity() {


    private var recyclerView: RecyclerView? = null
    private var adapter: ReceivedRequestAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<ReceivedRequestDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_request)

        // hiding keyboard
        hideKeyBoard()

        recyclerView = findViewById(R.id.rvRecievedRequest) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val tasteData = ReceivedRequestDataClass("imageUrl", "Cocatre Chansophao"
            );
            arrayList.add(tasteData)
        }

        adapter = ReceivedRequestAdapter(arrayList)
        recyclerView!!.setAdapter(adapter)

    }
}

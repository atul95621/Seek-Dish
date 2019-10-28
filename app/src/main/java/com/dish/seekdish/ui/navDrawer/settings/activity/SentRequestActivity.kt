package com.dish.seekdish.ui.navDrawer.settings.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.settings.adapter.SentRequestAdapter
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SentRequestDataClass

import java.util.ArrayList

class SentRequestActivity : BaseActivity() {


    private var recyclerView: RecyclerView? = null
    private var adapter: SentRequestAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<SentRequestDataClass>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sent_request)

        // hiding keyboard
        hideKeyBoard()

        recyclerView = findViewById(R.id.rvSentRequest) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val tasteData = SentRequestDataClass("imageUrl", "Cocatre Chansophao");
            arrayList.add(tasteData)
        }

        adapter = SentRequestAdapter(arrayList)
        recyclerView!!.setAdapter(adapter)

    }
}

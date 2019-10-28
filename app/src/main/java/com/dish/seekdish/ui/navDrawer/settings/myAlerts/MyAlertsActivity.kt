package com.dish.seekdish.ui.navDrawer.settings.myAlerts

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import kotlinx.android.synthetic.main.activity_my_alerts.*
import java.util.ArrayList

class MyAlertsActivity : BaseActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: MyAlertAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    internal var arrayList = ArrayList<MyAlertDataClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_alerts)

        hideKeyBoard()


        recyclerView = findViewById(R.id.rvAlertsFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val MyAlertDataClass = MyAlertDataClass("Bisonauras 34", "Caspertang 32 cheoniski");
            arrayList.add(MyAlertDataClass)
        }


        adapter = MyAlertAdapter(arrayList, this@MyAlertsActivity)
        recyclerView!!.setAdapter(adapter)



        tvBack.setOnClickListener()
        {
            finish()
        }
    }

}

package com.dish.seekdish.ui.navDrawer.dishDescription.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.dishDescription.DishDescriptionActivity
import java.util.ArrayList


class SimilarFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: SimilarAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager
    internal var arrayList = ArrayList<SimilarDataClass>()

    private lateinit var dishActivity: DishDescriptionActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_similar, container, false)


        dishActivity = activity as DishDescriptionActivity

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvSimilarFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val similarDataClass = SimilarDataClass("Manager", "Brochette De Boeuf", "1.85 Km", "3", "14", "4");
            arrayList.add(similarDataClass)
        }

        adapter = SimilarAdapter(arrayList,dishActivity)
        recyclerView!!.setAdapter(adapter)


        return  view
    }


}

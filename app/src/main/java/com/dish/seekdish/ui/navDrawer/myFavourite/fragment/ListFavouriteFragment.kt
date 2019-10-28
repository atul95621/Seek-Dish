package com.dish.seekdish.ui.navDrawer.myFavourite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.myFavourite.adapter.ListFragAdapter
import com.dish.seekdish.ui.navDrawer.myFavourite.dataClass.ListFragDataClass


import java.util.ArrayList


class ListFavouriteFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: ListFragAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var homeActivity: HomeActivity
    internal var arrayList = ArrayList<ListFragDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Inflate the layout for this fragment
        homeActivity = activity as HomeActivity

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvTasteFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val tasteData = ListFragDataClass("Manager", "Brochette De Boeuf", "1.85 Km", "3", "14", "4");
            arrayList.add(tasteData)
        }

        adapter = ListFragAdapter(arrayList, homeActivity)
        recyclerView!!.setAdapter(adapter)


//        enableSwipeToDeleteAndUndo()


        return view
    }


   /* private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(homeActivity) {
            override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {


                val position = viewHolder.adapterPosition
                val dishName = adapter!!.getData().get(position).dishName
                val distance = adapter!!.getData().get(position).distance

                adapter!!.removeItem(position)

                Log.e("dish to be removed is:", dishName + "     " + distance)

                showSnackBar("Item was removed from the list.")

                *//*        val snackbar = Snackbar
                            .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                        //                snackbar.setAction("UNDO", new View.OnClickListener() {
                        //                    @Override
                        //                    public void onClick(View view) {
                        //
                        //                        mAdapter.restoreItem(item, position);
                        //                        recyclerView.scrollToPosition(position);
                        //                    }
                        //                });

                        snackbar.setActionTextColor(Color.YELLOW)
                        snackbar.show()*//*

            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(recyclerView)
    }*/


}

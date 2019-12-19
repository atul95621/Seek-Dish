package com.dish.seekdish.ui.navDrawer.myFavourite.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.myFavourite.VM.FavoriteVM
import com.dish.seekdish.ui.navDrawer.myFavourite.adapter.ListFragAdapter
import com.dish.seekdish.ui.navDrawer.toDo.list.Data_todo
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_list.*


import java.util.ArrayList


class ListFavouriteFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: ListFragAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var homeActivity: HomeActivity
    internal var arrayList = ArrayList<Data_todo>()
    var favoriteVM: FavoriteVM? = null
    var positionRecycler: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Inflate the layout for this fragment
        homeActivity = activity as HomeActivity
        favoriteVM = ViewModelProviders.of(this).get(FavoriteVM::class.java)


        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvFavList) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)


        getFavList()


        getDeleteObserver()
        getFavListObserver()


//        enableSwipeToDeleteAndUndo()
        return view
    }

    private fun getFavList() {
        // hitting api
        favoriteVM?.doGetFavList(
            sessionManager.getValue(SessionManager.USER_ID).toString()
        )
    }

    fun getFavListObserver() {

        //observe
        favoriteVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        favoriteVM!!.getFavoriteLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspFavList", response.toString())


                if (response.status == 1) {

                    arrayList = response.data

                    if (response.data.isEmpty()) {
                        rvFavList.visibility = View.INVISIBLE
                        tvFavAlert.visibility = View.VISIBLE

                    } else {
                        adapter = ListFragAdapter(arrayList, homeActivity, this)
                        recyclerView!!.setAdapter(adapter)
                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }

    fun getDeleteObserver() {

        //observe
        favoriteVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        favoriteVM!!.getFavDeleteLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspFavListDel", response.toString())

                Log.e("rspFavListDelStat", response.status.toString())

                if (response.status == 1) {
                    if (adapter != null) {

                        arrayList.clear()
                        getFavList()
                        showSnackBar(response.data.message)
                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }

    fun deleteItemFromFavList(mealId: String, restroId: String, position: Int) {
        positionRecycler = position
        favoriteVM?.deleteFavList(
            sessionManager.getValue(SessionManager.USER_ID).toString(), mealId, restroId
        )
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
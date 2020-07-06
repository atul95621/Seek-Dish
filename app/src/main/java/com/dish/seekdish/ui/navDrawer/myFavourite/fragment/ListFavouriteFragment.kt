package com.dish.seekdish.ui.navDrawer.myFavourite.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import kotlinx.android.synthetic.main.fragment_list.view.*


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
        favoriteVM = ViewModelProvider(this).get(FavoriteVM::class.java)


        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvFavList) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)


        getFavList()


        getDeleteObserver()
        getFavListObserver()

        searchTextListner(view)


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

        favoriteVM!!.getFavoriteLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.status == 1) {

                    arrayList = response.data

                    if (response.data.isEmpty()) {
                        rvFavList.visibility = View.INVISIBLE
                        tvFavAlert.visibility = View.VISIBLE

                    } else {
                        adapter = ListFragAdapter(arrayList, homeActivity, this)
                        recyclerView!!.setAdapter(adapter)
                    }
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(this.getResources().getString(R.string.error_occured) + "    $response");

            }
        })
    }

    fun getDeleteObserver() {

        //observe
        favoriteVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        favoriteVM!!.getFavDeleteLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {

                if (response.status == 1) {
                    if (adapter != null) {

                        arrayList.clear()
                        getFavList()
                        showSnackBar(response.message)
                    }
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(this.getResources().getString(R.string.error_occured) + "    $response");

            }
        })
    }

    fun deleteItemFromFavList(mealId: String, restroId: String, position: Int) {
        positionRecycler = position
        favoriteVM?.deleteFavList(
            sessionManager.getValue(SessionManager.USER_ID).toString(), mealId, restroId
        )
    }

    private fun searchTextListner(view: View) {

        view.edtSearchFavMeal.setOnClickListener()
        {
            view.edtSearchFavMeal.isCursorVisible = true
        }

        view.edtSearchFavMeal.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // filter your list from your input

                if (view.edtSearchFavMeal.text.isNullOrEmpty() == false) {
                    filter(s.toString())
                } else {
                    rvFavList.visibility = View.VISIBLE
                    tvFavAlert.visibility = View.GONE
                    getFavList()
                }
            }
        })
    }

    fun filter(text: String?) {
        val filteredItems = java.util.ArrayList<Data_todo>()
        for (d in arrayList) {
            if (d.name.contains(text.toString(), ignoreCase = true)) {
                filteredItems.add(d)
            }
        }
        if (filteredItems.size == 0) {
            rvFavList.visibility = View.GONE
            tvFavAlert.visibility = View.VISIBLE
            tvFavAlert.text = getResources().getString(R.string.no_todo)
        } else {
            rvFavList.visibility = View.VISIBLE
            tvFavAlert.visibility = View.GONE
        }
        //update recyclerview
        adapter?.updateList(filteredItems)
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

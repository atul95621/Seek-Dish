package com.dish.seekdish.ui.navDrawer.restaurants.fragments

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
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
import com.dish.seekdish.custom.PaginationScrollListener
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.home.dataModel.Data_time
import com.dish.seekdish.ui.navDrawer.restaurants.adapter.RestaurantAdapter
import com.dish.seekdish.ui.navDrawer.restaurants.adapter.TimeRestroAdapter
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.Data_Time_Restro
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.TimeRestroDataClass
import com.dish.seekdish.ui.navDrawer.restaurants.viewModel.RestroTimeVM
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_time_restro.*
import kotlinx.android.synthetic.main.fragment_time_restro.view.*
import java.util.ArrayList


class TimeRestroFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: TimeRestroAdapter? = null
    internal lateinit var layoutManager: LinearLayoutManager

    private lateinit var homeActivity: HomeActivity

    internal var arrayList = ArrayList<Data_Time_Restro>()
    var restroTimeVM: RestroTimeVM? = null

    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    var pageNumber: Int = 1
    var flagSearch: Boolean = false

    var alertShown: Boolean = false
    internal var searchArrayList = ArrayList<Data_Time_Restro>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time_restro, container, false)


        homeActivity = activity as HomeActivity
        restroTimeVM = ViewModelProviders.of(this).get(RestroTimeVM::class.java)

        // hiding keyboard
        hideKeyBoard()


        //check connection
        if (homeActivity.connectionDetector.isConnectingToInternet) {

            //hitting api
            getTimeRestro(pageNumber)

        } else {
            showSnackBar(getString(R.string.check_connection))
        }
        searchTextListner(view)


        //observer
        getTimeRestroRespeObserver()
        getSearchObserver()

        recyclerView = view.findViewById(R.id.rvtimeRestroFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        // these adapter is allocated memory and set on beacuse as so that focus of recyler does not go on top again
        /*  adapter = TimeRestroAdapter(conxt, arrayList, homeActivity)
          recyclerView!!.adapter = adapter
  */

        recyclerView!!.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }


            override fun loadMoreItems() {
                Log.e("loadMoreItems ", "hitted")


                if (flagSearch == false) {

                    Log.e("loadMoreItems", "entered   " + "isLastPage staus is " + isLastPage)

//                    progressBar.setVisibility(View.VISIBLE)
                    isLoading = true
                    if (!isLastPage) {

                        Log.e("loadMoreItems", "entered inide lastpage scope")

                        Handler().postDelayed({

                            //hitting api
                            getTimeRestro(pageNumber)
                        }, 200)
                    }
                }
            }
        })

        recyclerView = view.findViewById(R.id.rvtimeRestroFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        return view
    }

    private fun getTimeRestro(page: Int) {

        Log.e("loadMoreItems", "entered getLikedIngre ")
        var radius: String = sessionManager.getValue(SessionManager.RADIUS)
        if (radius != null && radius != "null" && radius != "") {
            radius = sessionManager.getValue(SessionManager.RADIUS)
        } else {
            radius = "15"
        }

        // hitting api
        restroTimeVM?.doGetTimeRestroData(
            sessionManager.getValue(SessionManager.USER_ID).toString(),
            page.toString(),
            sessionManager.getValue(SessionManager.LONGITUDE),
            sessionManager.getValue(SessionManager.LATITUDE), radius
        )
    }

    fun getTimeRestroRespeObserver() {

        Log.e("loadMoreItems", "entered getLikedResponseObserver ")

        //observe
        restroTimeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        restroTimeVM!!.getTimeRestroLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspgetLiked", response.toString())

                Log.e("rspgetLikedStat", response.status.toString())

                if (response.status == 1) {

                    var arrySize = arrayList.size

                    if (response.data.isEmpty() && alertShown == false) {
                        tvItemsAlert.visibility = View.VISIBLE
                    } else {

                        // this does not make 2 copies of item in recyclerview...
                        if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                            adapter?.getItemCount()?.minus(1)
                        ) {
                            // loading new items...
                            resultAction(response.data)
                            alertShown = true
                        }
                        if (pageNumber == 1) {
                            adapter = TimeRestroAdapter(conxt, arrayList, homeActivity)
                            recyclerView!!.adapter = adapter

                        }
                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }


    fun resultAction(data: ArrayList<Data_Time_Restro>) {
        Log.e("data came", "" + data.toString())

//        progressBar.setVisibility(View.INVISIBLE)
        isLoading = false
        if (data != null) {
            adapter?.addItems(data)

            Log.e("data to bind", "" + data.toString())
            if (data.size == 0) {
                isLastPage = true
            } else {
                /* var pos:Int= adapter?.itemCount?.minus(2)!!
                 recyclerView?.scrollToPosition(pos)*/
                pageNumber = pageNumber + 1

                Log.e("pgNumber", "" + pageNumber)
            }
        }
    }


    fun getSearchObserver() {
        //observe
        restroTimeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        restroTimeVM!!.searchTimeRestro.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
//                    var arrySize = arrayList.size
                    if (response.data.isEmpty()) {
                        tvItemsAlert.visibility = View.VISIBLE
                        tvItemsAlert.text =
                            homeActivity.getResources().getString(R.string.no_rest_found)
                        rvtimeRestroFrag.visibility = View.GONE
                    } else {
                        tvItemsAlert.visibility = View.GONE
                        rvtimeRestroFrag.visibility = View.VISIBLE
                        searchArrayList = response.data
                        //setting adapter again
                        adapter = TimeRestroAdapter(conxt, searchArrayList, homeActivity)
                        recyclerView!!.adapter = adapter
                    }
                }
            } else {
                showSnackBar("OOps! Error Occured.")
            }
        })
    }


    private fun searchTextListner(view: View) {
        view.edtSearchTime.addTextChangedListener(object : TextWatcher {
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

                if (view.edtSearchTime.text.isNullOrEmpty() == false) {
                    flagSearch = true
                    getSearchedText()
                } else {
                    flagSearch = false
                    pageNumber = 1
                    //hitting api
                    getTimeRestro(pageNumber)
                }
            }
        })
    }

    private fun getSearchedText() {
        var radius: String = sessionManager.getValue(SessionManager.RADIUS)
        if (radius.isNullOrEmpty() == false) {
            radius = sessionManager.getValue(SessionManager.RADIUS)
        } else {
            radius = "15"
        }

        // hitting api
        restroTimeVM?.getHomeMealSearched(
            sessionManager.getValue(SessionManager.USER_ID).toString(),
            "1",
            sessionManager.getValue(SessionManager.LONGITUDE),
            sessionManager.getValue(SessionManager.LATITUDE),
            radius,
            edtSearchTime.text.toString()
        )
    }


}

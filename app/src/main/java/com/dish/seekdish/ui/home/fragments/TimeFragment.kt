package com.dish.seekdish.ui.home.fragments

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
import com.dish.seekdish.ui.home.adapter.TimeFragAdapter
import com.dish.seekdish.ui.home.dataModel.Data_time
import com.dish.seekdish.ui.home.viewModel.TimeVM
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_time.*
import kotlinx.android.synthetic.main.fragment_time.view.*
import java.util.ArrayList


class TimeFragment : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    private var adapter: TimeFragAdapter? = null
    internal lateinit var layoutManager: LinearLayoutManager

    var timeVM: TimeVM? = null


    private lateinit var homeActivity: HomeActivity
    internal var arrayList = ArrayList<Data_time>()


    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    var pageNumber: Int = 1
    var flagSearch: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time, container, false)
        // Inflate the layout for this fragment
        homeActivity = activity as HomeActivity

        // hiding keyboard
        hideKeyBoard()

        timeVM = ViewModelProviders.of(this).get(TimeVM::class.java)


        //check connection
        if (homeActivity.connectionDetector.isConnectingToInternet) {

            //hitting api
            getTimeMeals(pageNumber)

        } else {
            showSnackBar(getString(R.string.check_connection))
        }

        //observer
        getTimeResponseObserver()

        recyclerView = view.findViewById(R.id.rvTimeFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        /*   for (i in 0..6) {
               val tasteData = TimeFragDataClass("Manager", "Plat Origine", "1.45 Km", "4", "16", "2");
               arrayList.add(tasteData)
           }

           adapter = TimeFragAdapter(arrayList,homeActivity)
           recyclerView!!.setAdapter(adapter)*/


        // these adapter is allocated memory and set on beacuse as so that focus of recyler does not go on top again
        adapter = TimeFragAdapter(conxt, arrayList, homeActivity)
        recyclerView!!.adapter = adapter


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
                            getTimeMeals(pageNumber)
                        }, 200)
                    }
                }
            }
        })

        view.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                // search like ingredient
                var serchedText = edtSearch.text.toString();

                if (serchedText != null && serchedText != "null" && serchedText != "") {
                    flagSearch = true
                    Log.e("textWatcher", "entered if scope")
//                    getSearchedIngre(serchedText)
                } else {

                    Log.e("textWatcher", "entered else scope")

                    flagSearch = false
                    pageNumber = 1
                    getTimeMeals(pageNumber)

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        return view
    }


    fun getTimeResponseObserver() {

        Log.e("loadMoreItems", "entered getLikedResponseObserver ")

        //observe
        timeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        timeVM!!.getTasteLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspgetLiked", response.toString())

                Log.e("rspgetLikedStat", response.status.toString())

                if (response.status == 1) {

                    var arrySize = arrayList.size

//                    resultAction(response.data)


                    // this does not make 2 copies of item in recyclerview...
                    if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                        adapter?.getItemCount()?.minus(1)
                    ) {
                        // loading new items...
                        resultAction(response.data)

                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }


    fun resultAction(data: ArrayList<Data_time>) {
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


    private fun getTimeMeals(page: Int) {

        Log.e("loadMoreItems", "entered getLikedIngre ")
        var radius: String = sessionManager.getValue(SessionManager.RADIUS)
        if (radius != null && radius != "null" && radius != "") {
            radius = sessionManager.getValue(SessionManager.RADIUS)
        } else {
            radius = "15"
        }

        // hitting api
        timeVM?.doGetTimeMealData(
            sessionManager.getValue(SessionManager.USER_ID).toString(),
            page.toString(),
            sessionManager.getValue(SessionManager.LONGITUDE),
            sessionManager.getValue(SessionManager.LATITUDE), radius
        )
    }
}

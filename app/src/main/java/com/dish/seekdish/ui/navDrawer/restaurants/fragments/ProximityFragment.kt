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
import com.dish.seekdish.ui.navDrawer.restaurants.adapter.ProximityAdapter
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.Data_Proximity
import com.dish.seekdish.ui.navDrawer.restaurants.viewModel.ProximityVM
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_proximity.*
import kotlinx.android.synthetic.main.fragment_proximity.view.edtSearch
import java.util.ArrayList


class ProximityFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: ProximityAdapter? = null
    internal lateinit var layoutManager: LinearLayoutManager

    private lateinit var homeActivity: HomeActivity
    internal var arrayList = ArrayList<Data_Proximity>()
    var proximityVM: ProximityVM? = null

    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    var pageNumber: Int = 1
    var flagSearch: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_proximity, container, false)



        homeActivity = activity as HomeActivity
        proximityVM = ViewModelProviders.of(this).get(ProximityVM::class.java)

        // hiding keyboard
        hideKeyBoard()


        //check connection
        if (homeActivity.connectionDetector.isConnectingToInternet) {

            //hitting api
            getProxiRestro(pageNumber)

        } else {
            showSnackBar(getString(R.string.check_connection))
        }

        //observer
        getProxiRestroRespeObserver()


        recyclerView = view.findViewById(R.id.rvProximityFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        // these adapter is allocated memory and set on beacuse as so that focus of recyler does not go on top again
        adapter = ProximityAdapter(conxt, arrayList, homeActivity)
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
                            getProxiRestro(pageNumber)
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
                    getProxiRestro(pageNumber)

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

/*        homeActivity = activity as HomeActivity

        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvProximityFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        for (i in 0..6) {
            val tasteData = ProximityDataClass("Manager", "Brochette De Boeuf", "Bizers,3400, Occtine BOJUAN", "3", "14", "4");
            arrayList.add(tasteData)
        }

        adapter = ProximityAdapter(arrayList,homeActivity)
        recyclerView!!.setAdapter(adapter)*/

        return view
    }


    private fun getProxiRestro(page: Int) {

        Log.e("loadMoreItems", "entered getLikedIngre ")
        var radius: String = sessionManager.getValue(SessionManager.RADIUS)
        if (radius != null && radius != "null" && radius != "") {
            radius = sessionManager.getValue(SessionManager.RADIUS)
        } else {
            radius = "15"
        }

        // hitting api
        proximityVM?.doGetProxiRestroData(
            sessionManager.getValue(SessionManager.USER_ID).toString(),
            page.toString(),
            sessionManager.getValue(SessionManager.LONGITUDE),
            sessionManager.getValue(SessionManager.LATITUDE), radius
        )
    }

    fun getProxiRestroRespeObserver() {

        Log.e("loadMoreItems", "entered getLikedResponseObserver ")

        //observe
        proximityVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        proximityVM!!.getProxiRestroLiveData.observe(this, Observer { response ->
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


    fun resultAction(data: ArrayList<Data_Proximity>) {
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


}

package com.dish.seekdish.ui.navDrawer.settings.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R

import com.dish.seekdish.ui.navDrawer.settings.adapter.LikeAdapter
import com.dish.seekdish.ui.navDrawer.settings.viewModel.LikeVM
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_forgot.tvBack
import kotlinx.android.synthetic.main.activity_like.*
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Liked
import android.text.TextUtils.join as join1
import com.dish.seekdish.custom.PaginationScrollListener
import kotlin.collections.ArrayList
import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.dish.seekdish.util.Global
import android.text.method.TextKeyListener.clear


class LikeActivity : BaseActivity() {


    private var recyclerView: RecyclerView? = null
    var adapter: LikeAdapter? = null
    internal lateinit var layoutManager: LinearLayoutManager
    internal var arrayList = ArrayList<Data_Liked>()

    var sessionManager: SessionManager? = null;

    var likeVM: LikeVM? = null

    var pageNumber: Int = 1

    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    var flagSearch: Boolean = false


    //WILL BE SAVING THE SAVED INGREDIENTS IN ANDROID...
    companion object {
        var savedIngredients = ""
        var pageNumber: Int = 1


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        // hiding keyboard
        hideKeyBoard()

        likeVM = ViewModelProviders.of(this).get(LikeVM::class.java)
        sessionManager = SessionManager(this)

        Global.likeSavedArraylist.clear()

        //hitting api
        getLikedIngre(pageNumber)

        //response observer
        getLikedResponseObserver()
        postSavedLikedResponseObserver()
        getSearchLikedResponseObserver()




        recyclerView = findViewById(R.id.rvLikeActivity) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        // these adapter is allocated memory and set on beacuse as so that focus of recyler does not go on top again
        adapter = LikeAdapter(this, arrayList)
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

                    progressBar.setVisibility(View.VISIBLE)
                    isLoading = true
                    if (!isLastPage) {

                        Log.e("loadMoreItems", "entered inide lastpage scope")

                        Handler().postDelayed({

                            //hitting api
                            getLikedIngre(pageNumber)
                        }, 200)
                    }
                }
            }
        })


        tvBack.setOnClickListener()
        {
            finish()
        }

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                // search like ingredient
                var serchedText = edtSearch.text.toString();

                if (serchedText != null && serchedText != "null" && serchedText != "") {
                    flagSearch = true
                    Log.e("textWatcher", "entered if scope")
                    getSearchedIngre(serchedText)
                } else {

                    Log.e("textWatcher", "entered else scope")

                    /*  arrayList.clear()
                      adapter?.notifyDataSetChanged()*/


                    /* arrayList.clear()
                     adapter = LikeAdapter(this@LikeActivity, arrayList)
                     recyclerView!!.adapter = adapter*/

                    flagSearch = false
                    pageNumber = 1
                    getLikedIngre(pageNumber)

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        tvAdd.setOnClickListener()
        {
            if (arrayList.size != null && arrayList.size > 0) {
//                var selectedIngreId: String = ""
                var arrayListTrue = ArrayList<String>()
                for (items in arrayList) {
                    if (items.checkForLike == true) {
                        arrayListTrue.add(items.id.toString())
                    }
                }


                val hashSet = HashSet<String>()
                hashSet.addAll(Global.likeSavedArraylist)
                Global.likeSavedArraylist.clear()
                Global.likeSavedArraylist.addAll(hashSet)
                savedIngredients = join1(",", Global.likeSavedArraylist)
                Log.e("LikedCommaSepSearch", savedIngredients)


                     if (savedIngredients != null && savedIngredients != "null" && savedIngredients != "") {
                         //hitiing save api
                         likeVM?.doSaveLikedIngredients(
                             sessionManager?.getValue(SessionManager.USER_ID).toString(),
                             savedIngredients
                         )
                     } else {
                         showSnackBar("Please select atleast one ingredient.")
                     }
            }
        }

    }


    private fun getLikedIngre(page: Int) {

        Log.e("loadMoreItems", "entered getLikedIngre ")


        // hitting api
        likeVM?.doGetLikedIngredients(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            page.toString()
        )
    }

    private fun getSearchedIngre(serchedText: String) {
        // hitting api
        likeVM?.doGetSearchedIngredients(
            sessionManager?.getValue(SessionManager.USER_ID).toString(), serchedText
        )
    }


    private fun getLikedResponseObserver() {

        Log.e("loadMoreItems", "entered getLikedResponseObserver ")

        //observe
        likeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        likeVM!!.getLikedLiveData.observe(this, Observer { response ->
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

                    /*     if (response.data != null) {

                             arrayList = response.data

                             adapter = LikeAdapter(this, arrayList)
                             recyclerView!!.adapter = adapter

                             *//*    Log.e("data to bind", "" + response.data.toString())
                            if (response.data.size == 0) {
                                isLastPage = true
                            } else {
                                *//**//* var pos:Int= adapter?.itemCount?.minus(2)!!
                             recyclerView?.scrollToPosition(pos)*//**//*
                            pageNumber = pageNumber + 1
                        }*//*
                    }*/


                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }


    private fun getSearchLikedResponseObserver() {
        //observe
        likeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        likeVM!!.getSearchLikedLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspgetsearchLiked", response.toString())

                Log.e("rspgetsearchLikedStat", response.status.toString())

                /*   var arrySize = arrayList.size
                   Log.e("searchSize", "" + arrySize)*/

                if (response.status == 1) {


                    /*  // this does not make 2 copies of item in recyclerview...
                      if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                          adapter?.getItemCount()?.minus(1)
                      ) {
                          // loading new items...
                          resultAction(response.data)
                      }*/

                    if (response.data.size != 0) {

                        arrayList = response.data

                        Log.e("search", "" + "   " + arrayList)

                        adapter = LikeAdapter(this, arrayList)
                        recyclerView!!.adapter = adapter
                        adapter!!.notifyDataSetChanged()
                    }


                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspsearchSnak", "else error")

            }
        })
    }


    fun resultAction(data: ArrayList<Data_Liked>) {
        Log.e("data came", "" + data.toString())

        progressBar.setVisibility(View.INVISIBLE)
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


    // post like save ingredients
    private fun postSavedLikedResponseObserver() {
        //observe
        likeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        likeVM!!.saveLikedLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspsaveLiked", response.toString())

                Log.e("rspsaveLikedStat", response.status.toString())

                if (response.status == 1) {
                    showSnackBar(response.data.message)

                    Handler().postDelayed({
                        val returnIntent = Intent()
                        setResult(Activity.RESULT_CANCELED, returnIntent)
                        finish()
                    }, 1000)
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspsaveLikedError", "else error")

            }
        })
    }


}

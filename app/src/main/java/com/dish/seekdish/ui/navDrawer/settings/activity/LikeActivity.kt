package com.dish.seekdish.ui.navDrawer.settings.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

//    var pageNumber: Int = 1

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

        likeVM = ViewModelProvider(this).get(LikeVM::class.java)
        sessionManager = SessionManager(this)

        Global.likedItemsSet.clear()

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
        edtSearch.setOnClickListener()
        {
            edtSearch.isCursorVisible=true
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

                    flagSearch = false

                    adapter?.clearLikedList()
                    pageNumber = 1

                    // hitting api when the text is cleared from search
                    getLikedIngre(1)

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        tvAdd.setOnClickListener()
        {
            savedIngredients = join1(",", Global.likedItemsSet)
            Log.e("LikedCommaSepSearch", savedIngredients)

            //hitiing save api
            likeVM?.doSaveLikedIngredients(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                savedIngredients
            )
         /*   if (savedIngredients != null && savedIngredients != "null" && savedIngredients != "") {

            } else {
                showSnackBar("Please select atleast one ingredient.")
            }*/

        }

    }


    private fun getLikedIngre(page: Int) {

        Log.e("loadMoreItems", "entered getLikedIngre " + "current page no is:" + page)


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

/*IMPORTANT:
* if page no is 1, we hit the resultAction mehtod indivisually coz it was not working, if only work for page no.1
* else  is used for page that are not equal to 1, means above 1.
* */

                    if (pageNumber == 1) {
                        adapter?.clearLikedList()
                        Log.e("TestingLike", "entered in for 1st row")
                        resultAction(response.data)
                    } else {
                        // this does not make 2 copies of item in recyclerview...
                        if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                            adapter?.getItemCount()?.minus(1)
                        ) {

                            Log.e("TestingLike", "entered in last row")

                            // loading new items...
                            resultAction(response.data)
                        }
                    }


                }
                else
                {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(resources.getString(R.string.error_occured) + "    ${response}");
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

                if (response.status == 1) {


                    if (response.data.size != 0) {

                        arrayList = response.data

                        Log.e("search", "" + "   " + arrayList)

                        adapter = LikeAdapter(this, arrayList)
                        recyclerView!!.adapter = adapter
                        adapter!!.notifyDataSetChanged()
                    }
                }

            } else {

                showSnackBar(resources.getString(R.string.error_occured) + "    ${response}");
                Log.e("rspsearchSnak", "else error")

            }
        })
    }


    fun resultAction(data: ArrayList<Data_Liked>) {
        Log.e("dataCame", "" + data.toString())

        progressBar.setVisibility(View.INVISIBLE)
        isLoading = false
        if (data != null) {
            adapter?.addItems(data)

            Log.e("dataBind", "" + data.toString())
            if (data.size == 0) {
                isLastPage = true
            } else {
                Log.e("pgNumber_current", "" + pageNumber)
                pageNumber = pageNumber + 1

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
                    showSnackBar(response.message)

                    Handler().postDelayed({
                        val returnIntent = Intent()
                        setResult(Activity.RESULT_CANCELED, returnIntent)
                        finish()
                    }, 1000)
                }
                else
                {
                    showSnackBar(response.message)
                }
            } else {

                showSnackBar(resources.getString(R.string.error_occured) + "    ${response}");
                Log.e("rspsaveLikedError", "else error")

            }
        })
    }


    // re fining the page number to 1 because its static and should be started from 1 when user comes on screen
    override fun onStop() {
        super.onStop()
        pageNumber = 1
    }


}

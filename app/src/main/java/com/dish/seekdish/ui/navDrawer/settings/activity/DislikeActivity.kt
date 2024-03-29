package com.dish.seekdish.ui.navDrawer.settings.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.PaginationScrollListener
import com.dish.seekdish.ui.navDrawer.settings.adapter.DislikeAdapter
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Disliked
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Liked
import com.dish.seekdish.ui.navDrawer.settings.viewModel.DisLikeVM
import com.dish.seekdish.util.Global
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_dislike.*
import kotlinx.android.synthetic.main.activity_forgot.tvBack
import java.util.ArrayList
import android.text.TextUtils.join as join1

class DislikeActivity : BaseActivity() {

    var recyclerView: RecyclerView? = null
    var adapter: DislikeAdapter? = null

    internal lateinit var layoutManager: LinearLayoutManager
    internal var arrayList = ArrayList<Data_Disliked>()

    var sessionManager: SessionManager? = null;

    var disLikeVM: DisLikeVM? = null


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
        setContentView(R.layout.activity_dislike)

        // hiding keyboard
        hideKeyBoard()

        disLikeVM = ViewModelProvider(this).get(DisLikeVM::class.java)
        sessionManager = SessionManager(this)


        Global.dislikedItemsSet.clear()

        //hitting api
        getDislikedIngre(pageNumber)

        //response observer
        getDisLikedResponseObserver()
        postSavedDisLikedResponseObserver()
        getSearchDisLikedResponseObserver()


        recyclerView = findViewById(R.id.rvDislikeActivity) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        // these adapter is allocated memory and set on beacuse as so that focus of recyler does not go on top again
        adapter = DislikeAdapter(this, arrayList)
        recyclerView!!.adapter = adapter


        recyclerView!!.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }


            override fun loadMoreItems() {
                progressBarD.setVisibility(View.VISIBLE)
                isLoading = true
                if (!isLastPage) {
                    Handler().postDelayed({
                        //hitting api
                        getDislikedIngre(pageNumber)
                    }, 200)
                }
            }
        })

        tvBack.setOnClickListener()
        {
            finish()
        }

        edtSearch.setOnClickListener()
        {
            edtSearch.isCursorVisible = true
        }

        tvAdd.setOnClickListener()
        {
            savedIngredients = join1(",", Global.dislikedItemsSet)

            //hitiing save api
            disLikeVM?.doSaveDisLikedIngredients(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                savedIngredients
            )
            /*  if (savedIngredients != null && savedIngredients != "null" && savedIngredients != "") {

              } else {
                  showSnackBar("Please select atleast one ingredient.")
              }*/

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
                    getDislikedIngre(1)

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }


    private fun getDislikedIngre(page: Int) {
        // hitting api
        disLikeVM?.doGetDisLikedIngredients(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            page.toString()
        )
    }

    private fun getSearchedIngre(serchedText: String) {
        // hitting api
        disLikeVM?.doGetSearchIngred(
            sessionManager?.getValue(SessionManager.USER_ID).toString(), serchedText
        )
    }


    private fun getDisLikedResponseObserver() {
        //observe
        disLikeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        disLikeVM!!.getDisLikedLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {

                    var arrySize = arrayList.size

                    /*IMPORTANT:
       * if page no is 1, we hit the resultAction mehtod indivisually coz it was not working, if only work for page no.1
       * else  is used for page that are not equal to 1, means above 1.
       * */

                    if (pageNumber == 1) {
                        adapter?.clearLikedList()
                        resultAction(response.data)
                    } else {
                        // this does not make 2 copies of item in recyclerview...
                        if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                            adapter?.getItemCount()?.minus(1)
                        ) {
                            // loading new items...
                            resultAction(response.data)
                        }
                    }

                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(this.getResources().getString(R.string.error_occured) + "    ${response}");
            }
        })
    }


    fun resultAction(data: ArrayList<Data_Disliked>) {

        progressBarD.setVisibility(View.INVISIBLE)
        isLoading = false
        if (data != null) {
            adapter?.addItems(data)
            if (data.size == 0) {
                isLastPage = true
            } else {
                /* var pos:Int= adapter?.itemCount?.minus(2)!!
                 recyclerView?.scrollToPosition(pos)*/
                pageNumber = pageNumber + 1
            }
        }
    }


    // post like save ingredients
    private fun postSavedDisLikedResponseObserver() {
        //observe
        disLikeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        disLikeVM!!.saveDisLikedLiveData.observe(this, Observer { response ->
            if (response != null) {


                if (response.status == 1) {
                    showSnackBar(response.message)

                    Handler().postDelayed({
                        val returnIntent = Intent()
                        setResult(Activity.RESULT_CANCELED, returnIntent)
                        finish()
                    }, 1000)
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(this.getResources().getString(R.string.error_occured) + "    ${response}");
            }
        })
    }


    private fun getSearchDisLikedResponseObserver() {
        //observe
        disLikeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        disLikeVM!!.searchDislIngreLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {


                    if (response.data.size != 0) {
                        arrayList = response.data
                        adapter = DislikeAdapter(this, arrayList)
                        recyclerView!!.adapter = adapter
                        adapter!!.notifyDataSetChanged()
                    }


                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(this.getResources().getString(R.string.error_occured) + "    ${response}");

            }
        })
    }

    // re fining the page number to 1 because its static and should be started from 1 when user comes on screen
    override fun onStop() {
        super.onStop()
        pageNumber = 1
    }


}

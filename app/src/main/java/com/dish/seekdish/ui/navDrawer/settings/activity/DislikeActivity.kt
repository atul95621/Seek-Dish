package com.dish.seekdish.ui.navDrawer.settings.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.PaginationScrollListener
import com.dish.seekdish.ui.navDrawer.settings.adapter.DislikeAdapter
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Disliked
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Liked
import com.dish.seekdish.ui.navDrawer.settings.viewModel.DisLikeVM
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_dislike.*
import kotlinx.android.synthetic.main.activity_forgot.tvBack
import java.util.ArrayList

class DislikeActivity : BaseActivity() {

    var recyclerView: RecyclerView? = null
    var adapter: DislikeAdapter? = null

    internal lateinit var layoutManager: LinearLayoutManager
    internal var arrayList = ArrayList<Data_Disliked>()

    var sessionManager: SessionManager? = null;

    var disLikeVM: DisLikeVM? = null

    var pageNumber: Int = 1

    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dislike)

        // hiding keyboard
        hideKeyBoard()

        disLikeVM = ViewModelProviders.of(this).get(DisLikeVM::class.java)
        sessionManager = SessionManager(this)

        //hitting api
        getLikedIngre(pageNumber)

        //response observer
        getLikedResponseObserver()
        postSavedLikedResponseObserver()



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
                        getLikedIngre(pageNumber)
                    }, 200)
                }
            }
        })



        tvBack.setOnClickListener()
        {
            finish()
        }


        tvAdd.setOnClickListener()
        {
            if (arrayList.size != null && arrayList.size > 0) {
                var arrayListTrue = ArrayList<String>()
                var selectedIngreId: String = ""

                for (items in arrayList) {
                    if (items.checkForDisLike == true) {
                        arrayListTrue.add(items.id.toString())
                    }
                }
                selectedIngreId = TextUtils.join(",", arrayListTrue)
                Log.e("LikedCommaSepratedDis", selectedIngreId)

                if (selectedIngreId != null && selectedIngreId != "null" && selectedIngreId != "") {
                    //hitiing save api
                    disLikeVM?.doSaveDisLikedIngredients(
                        sessionManager?.getValue(SessionManager.USER_ID).toString(),
                        selectedIngreId
                    )
                } else {
                    showSnackBar("Please select atleast one ingredient.")
                }
            }
        }
    }


    private fun getLikedIngre(page: Int) {
        // hitting api
        disLikeVM?.doGetDisLikedIngredients(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            page.toString()
        )
    }


    private fun getLikedResponseObserver() {
        //observe
        disLikeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        disLikeVM!!.getDisLikedLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspgetDisLiked", response.toString())

                Log.e("rspgetDisLikedStat", response.status.toString())

                if (response.status == 1) {

                    var arrySize = arrayList.size

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


    fun resultAction(data: ArrayList<Data_Disliked>) {
        Log.e("data came", "" + data.toString())

        progressBarD.setVisibility(View.INVISIBLE)
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
            }
        }
    }


    // post like save ingredients
    private fun postSavedLikedResponseObserver() {
        //observe
        disLikeVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        disLikeVM!!.saveDisLikedLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspsaveDisLiked", response.toString())

                Log.e("rspsaveDisLikedStat", response.status.toString())

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

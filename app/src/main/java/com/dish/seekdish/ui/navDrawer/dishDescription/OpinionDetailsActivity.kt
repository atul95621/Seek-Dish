package com.dish.seekdish.ui.navDrawer.dishDescription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.navDrawer.dishDescription.VM.DishDescriptionVM
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_opinion_details.*
import kotlinx.android.synthetic.main.activity_dish_detail.*
import kotlinx.android.synthetic.main.activity_opinion_details.tvBack


class OpinionDetailsActivity : BaseActivity() {

    var comment_userId = ""
    var dishDescriptionVM: DishDescriptionVM? = null
    var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opinion_details)

        sessionManager = SessionManager(this)
        dishDescriptionVM = ViewModelProviders.of(this).get(DishDescriptionVM::class.java)

        getIntentData()

        getFriendReqObserver()
        getFollowingReqObserver()


        tvBack.setOnClickListener()
        {
            finish()
        }

        imgFriendRequest.setOnClickListener()
        {
            dishDescriptionVM?.doSendFriendRequest(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                comment_userId.toString()
            )
        }

        imgFollowing.setOnClickListener()
        {
            dishDescriptionVM?.doSendFollwoingRequest(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                comment_userId.toString()
            )
        }
    }

    private fun getFriendReqObserver() {
        //observe
        dishDescriptionVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        dishDescriptionVM!!.getFriendReqLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspGetaddtodoDetails", response.status.toString())

                if (response.status == 1) {
                    showSnackBar(response.data.message)
                }
            } else {
                showSnackBar("OOps! Error Occured.")
                Log.e("rspGetaddtodoFail", "else error")
            }
        })
    }

    private fun getFollowingReqObserver() {
        //observe
        dishDescriptionVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        dishDescriptionVM!!.getFollowingReqLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspGetaddtodoDetails", response.status.toString())

                if (response.status == 1) {
                    showSnackBar(response.data.message)
                }
            } else {
                showSnackBar("OOps! Error Occured.")
                Log.e("rspGetaddtodoFail", "else error")
            }
        })
    }

    private fun getIntentData() {

        comment_userId = intent.getStringExtra("COMMENT_USER_ID")

        val mealImage = intent.getStringExtra("MEAL_IMAGE")
        Glide.with(this).load(mealImage).dontAnimate().fitCenter().into(imgMealImage)

        ratingTaste.rating = intent.getFloatExtra("TASTE_RATING", 0F)
        ratingAmbience.rating = intent.getFloatExtra("AMBIANCE_RATING", 0F)
        ratingCleaniness.rating = intent.getFloatExtra("CLEAN_RATING", 0F)
        ratingDecor.rating = intent.getFloatExtra("DECOR_RATING", 0F)
        ratingOdor.rating = intent.getFloatExtra("ODOR_RATING", 0F)
        ratingPresentation.rating = intent.getFloatExtra("PRESENTATION_RATING", 0F)
        ratingService.rating = intent.getFloatExtra("SERVICE_RATING", 0F)
        ratingTexture.rating = intent.getFloatExtra("TEXTURE_RATING", 0F)
        ratingStar.rating = intent.getFloatExtra("AVG_RATING", 0F)
        ratingEuro.rating = intent.getFloatExtra("BUDGET_RATING", 0F)

        tvMealName.setText(intent.getStringExtra("MEAL_NAME"))
        tvComment.setText(intent.getStringExtra("COMMENT"))
        var date = intent.getStringExtra("DATE")
        tvDateName.setText(datePrase(date) + " - " + intent.getStringExtra("USERNAME"))

        var firend = intent.getStringExtra("IS_FRIEND")
        var private = intent.getStringExtra("IS_PRIVATE")
        var follower = intent.getStringExtra("IS_FOLLOWER")

        if (firend.equals(1)) {
            imgFriendRequest.visibility = View.INVISIBLE
        }
        if (follower.equals(1)) {
            imgFollowing.visibility = View.INVISIBLE
        }
        if (private.equals(1)) {
            imgFollowing.visibility = View.INVISIBLE
        }

        var image1 = intent.getStringExtra("COMMENT_IMAGE_1")
        var image2 = intent.getStringExtra("COMMENT_IMAGE_2")

        if (image1 != null && image1 != "null" && image1 != "") {
            Glide.with(this).load(image1).dontAnimate().fitCenter().into(imgCommentImage)
        } else {
            imgCommentImage.visibility = View.GONE
        }

        if (image2 != null && image2 != "null" && image2 != "") {
            Glide.with(this).load(image2).dontAnimate().fitCenter().into(imgCommentImageTwo)
        } else {
            imgCommentImageTwo.visibility = View.GONE
        }

        if (sessionManager?.getValue(SessionManager.USER_ID).equals(comment_userId)) {
            linFollowOptions.visibility = View.GONE
        } else {
            linFollowOptions.visibility = View.VISIBLE
        }

    }
}

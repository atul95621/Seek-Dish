package com.dish.seekdish.ui.navDrawer.dishDescription

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.dishDescription.VM.DishDescriptionVM
import com.dish.seekdish.ui.navDrawer.dishDescription.model.UserMealComment
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_opinion_details.*
import kotlinx.android.synthetic.main.activity_opinion_details.tvBack


class OpinionDetailsActivity : BaseActivity() {

    var comment_userId = ""
    var dishDescriptionVM: DishDescriptionVM? = null
    var sessionManager: SessionManager? = null
    var mealId = ""
    var restaurantId = ""
    var commentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opinion_details)

        sessionManager = SessionManager(this)
        dishDescriptionVM = ViewModelProviders.of(this).get(DishDescriptionVM::class.java)

        getIntentData()

        // for getting comment info
        getCommentApi()

        
        getCommentObserver()
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

    private fun getCommentObserver() {

        //observe
        dishDescriptionVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        dishDescriptionVM!!.getCommentLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspComment", response.status.toString())

                if (response.status == 1) {
                    val mealImage = response.response.data.meal_image
                    Glide.with(this).load(mealImage).dontAnimate().fitCenter().into(imgMealImage)

                    ratingTaste.rating = response.response.data.taste_rating.toFloat()
                    ratingAmbience.rating = response.response.data.ambiance_rating.toFloat()
                    ratingCleaniness.rating = response.response.data.cleanness_rating.toFloat()
                    ratingDecor.rating = response.response.data.decore_rating.toFloat()
                    ratingOdor.rating = response.response.data.odor_rating.toFloat()
                    ratingPresentation.rating = response.response.data.presentation_rating.toFloat()
                    ratingService.rating = response.response.data.service_rating.toFloat()
                    ratingTexture.rating = response.response.data.texture_rating.toFloat()
                    ratingStar.rating = response.response.rating.toFloat()
                    ratingEuro.rating = response.response.data.budget.toFloat()

                    tvMealName.setText(response.response.data.meal_name)
                    tvComment.setText(response.response.data.comment)

                    var date = response.response.data.published_on
                    tvDateName.setText(datePrase(date) + " - " + response.response.data.username)

                    var firend = response.response.friend
                    var private = response.response.private
                    var follower = response.response.follower

                    var image1 = response.response.data.image1
                    var image2 = response.response.data.image2


                    if (firend.equals(1)) {
                        imgFriendRequest.visibility = View.INVISIBLE
                    }
                    if (follower.equals(1)) {
                        imgFollowing.visibility = View.INVISIBLE
                    }
                    if (private.equals(1)) {
                        imgFollowing.visibility = View.INVISIBLE
                    }


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
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(getResources().getString(R.string.error_occured) + " $response");
                Log.e("rspComment", "else error")
            }
        })

    }

    private fun getCommentApi() {

        dishDescriptionVM?.getComments(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            mealId, restaurantId, commentId
        )

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
                    imgFriendRequest.visibility = View.GONE
                    showSnackBar(response.message)


                } else {
                    imgFriendRequest.visibility = View.VISIBLE
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(getResources().getString(R.string.error_occured) + " $response");
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

                    imgFollowing.visibility = View.GONE
                    showSnackBar(response.message)

                } else {
                    imgFollowing.visibility = View.VISIBLE
                    showSnackBar(response.message)
                }
            } else {

                showSnackBar(getResources().getString(R.string.error_occured) + " $response");
                Log.e("rspGetaddtodoFail", "else error")
            }
        })
    }

    private fun getIntentData() {

        val userMealComment = intent.getSerializableExtra("OPINION_RATING") as UserMealComment

        comment_userId = userMealComment.user_id.toString()
        restaurantId = userMealComment.restro_id.toString()
        mealId = userMealComment.meal_id.toString()
        commentId = userMealComment.comment_id.toString()

       /* val mealImage = userMealComment.meal_image
        Glide.with(this).load(mealImage).dontAnimate().fitCenter().into(imgMealImage)

        ratingTaste.rating = userMealComment.taste_rating.toFloat()
        ratingAmbience.rating = userMealComment.ambiance_rating.toFloat()
        ratingCleaniness.rating = userMealComment.cleanness_rating.toFloat()
        ratingDecor.rating = userMealComment.decore_rating.toFloat()
        ratingOdor.rating = userMealComment.odor_rating.toFloat()
        ratingPresentation.rating = userMealComment.presentation_rating.toFloat()
        ratingService.rating = userMealComment.service_rating.toFloat()
        ratingTexture.rating = userMealComment.texture_rating.toFloat()
        ratingStar.rating = userMealComment.meal_avg_rating.toFloat()
        ratingEuro.rating = userMealComment.budget.toFloat()

        tvMealName.setText(userMealComment.name)
        tvComment.setText(userMealComment.comment)

        var date = userMealComment.published_on
        tvDateName.setText(datePrase(date) + " - " + userMealComment.username)

        var firend = userMealComment.friend
        var private = userMealComment.private
        var follower = userMealComment.follower

        var image1 = userMealComment.rating_image1
        var image2 = userMealComment.rating_image2


        if (firend.equals(1)) {
            imgFriendRequest.visibility = View.INVISIBLE
        }
        if (follower.equals(1)) {
            imgFollowing.visibility = View.INVISIBLE
        }
        if (private.equals(1)) {
            imgFollowing.visibility = View.INVISIBLE
        }


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
        }*/

    }
}

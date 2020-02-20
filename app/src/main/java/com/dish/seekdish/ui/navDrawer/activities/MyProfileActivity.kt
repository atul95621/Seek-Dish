package com.dish.seekdish.ui.navDrawer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.activities.model.ProfileDataClass
import com.dish.seekdish.ui.navDrawer.checkin.CheckinActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.DislikeActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.LikeActivity
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_my_profile.tvBack
import kotlinx.android.synthetic.main.activity_received_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileActivity : BaseActivity() {
    var sessionManager: SessionManager? = null;
    internal lateinit var apiInterface: APIInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)


        clickListners()


    }


    fun reqApiHit() {

        ProgressBarClass.progressBarCalling(this)

        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)


        val call =
            apiInterface.getProfileData(sessionManager?.getValue(SessionManager.USER_ID).toString())
        call.enqueue(object : Callback<ProfileDataClass> {
            override fun onResponse(
                call: Call<ProfileDataClass>,
                response: Response<ProfileDataClass>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

                Log.e("respStr", " " + response.body().toString())

                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as ProfileDataClass
                    if (modelObj.status == 1) {

                        tvFriends.text = modelObj.data.new_info_data.friends.toString() + " Friends"
                        tvFollower.text =
                            modelObj.data.new_info_data.followers.toString() + " Followers"
                        tvLike.text = modelObj.data.new_info_data.like.toString() + " Likes"
                        tvDislike.text =
                            modelObj.data.new_info_data.dislike.toString() + " Dislikes"
                        tvCheckin.text =
                            modelObj.data.new_info_data.checkin.toString() + " Check-In"
                        tvProfilePercent.text =
                            modelObj.data.new_info_data.profile_percentage.toString() + " of Profile"
                        tvName.text = modelObj.data.first_name
                        tvBio.text = modelObj.data.bio


                    }

                } else {
//                    iSignUpView.onSetLoggedin(false, response)
                    showSnackBar(resources.getString(R.string.error_occured));

                }


            }

            override fun onFailure(call: Call<ProfileDataClass>, t: Throwable) {

//                Log.e("responseFailure", " " + t.toString())

                showSnackBar(resources.getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    override fun onResume() {
        super.onResume()


        initView()

        if (connectionDetector.isConnectingToInternet) {
            // api hit
            reqApiHit()
        } else {
            showSnackBar(resources.getString(R.string.check_connection))
        }


        if (!sessionManager?.getValue(SessionManager.PHOTO_URL).equals(null) && !sessionManager?.getValue(
                SessionManager.PHOTO_URL
            ).equals(
                "null"
            ) && !sessionManager?.getValue(SessionManager.PHOTO_URL).equals("") && !sessionManager?.getValue(
                SessionManager.FIRST_NAME
            ).equals("")
        ) {
            GlideApp.with(this)
                .load(sessionManager?.getValue(SessionManager.PHOTO_URL))
                .placeholder(R.drawable.ic_user)
                .into(profile_image)
        }

    }

    private fun initView() {
        sessionManager = SessionManager(this)
    }


    private fun clickListners() {

        btnUpdateProfile.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, MyInformationActivity::class.java)
            startActivity(intent)
        }

        tvBack.setOnClickListener()
        {
            finish()
        }

        btnChnagePassword.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        tvContact.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, ContactActivity::class.java)
            intent.putExtra("USER_ID", sessionManager?.getValue(SessionManager.USER_ID).toString());
            startActivity(intent)
        }

        tvCheckin.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, CheckinActivity::class.java)
            intent.putExtra("USER_ID",sessionManager?.getValue(SessionManager.USER_ID).toString());
            startActivity(intent)
        }

        tvDislike.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, DislikeActivity::class.java)
            startActivity(intent)
        }

        tvLike.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, LikeActivity::class.java)
            startActivity(intent)
        }

        tvFriends.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, HomeActivity::class.java)
            intent.putExtra("from", "MyProfileActivity")
            startActivity(intent)

        }

        tvFollower.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, HomeActivity::class.java)
            intent.putExtra("from", "MyProfileActivity")
            startActivity(intent)

        }
    }
}

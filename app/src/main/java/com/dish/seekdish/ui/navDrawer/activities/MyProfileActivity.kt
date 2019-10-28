package com.dish.seekdish.ui.navDrawer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.checkin.CheckinActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.DislikeActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.LikeActivity
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_my_profile.tvBack

class MyProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

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
            startActivity(intent)
        }

        tvCheckin.setOnClickListener()
        {
            val intent = Intent(this@MyProfileActivity, CheckinActivity::class.java)
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

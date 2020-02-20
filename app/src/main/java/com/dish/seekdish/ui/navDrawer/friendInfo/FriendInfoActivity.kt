package com.dish.seekdish.ui.navDrawer.friendInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.activities.ContactActivity
import com.dish.seekdish.ui.navDrawer.checkin.CheckinActivity
import kotlinx.android.synthetic.main.activity_friend_info.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.checkin.data.Data_Checkin


class FriendInfoActivity : AppCompatActivity() {
    var image = ""
    var name = ""
    var user_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.dish.seekdish.R.layout.activity_friend_info)

        getIntents()

        tvCheckin.setOnClickListener {
            val intent = Intent(this@FriendInfoActivity, CheckinActivity::class.java)
            intent.putExtra("USER_ID", user_id.toString());
            startActivity(intent)
        }

        tvContact.setOnClickListener {
            val intent = Intent(this@FriendInfoActivity, ContactActivity::class.java)
            intent.putExtra("USER_ID", user_id.toString());
            startActivity(intent)
        }

        tvFriend.setOnClickListener()
        {
            val intent = Intent(this@FriendInfoActivity, HomeActivity::class.java)
            intent.putExtra("from", "FriendInfoActivity")
            intent.putExtra("fromValue", user_id)
            startActivity(intent)

        }

    }

    private fun getIntents() {
        image = intent.getStringExtra("IMAGE")
        name = intent.getStringExtra("NAME")
        user_id = intent.getIntExtra("USER_ID", 0).toString()

        setViews()
    }

    fun setViews() {
        GlideApp.with(this)
            .load(image)
            .placeholder(com.dish.seekdish.R.drawable.ic_user)
            .into(imgProfileImage)
        tvName.setText(name)
    }

}

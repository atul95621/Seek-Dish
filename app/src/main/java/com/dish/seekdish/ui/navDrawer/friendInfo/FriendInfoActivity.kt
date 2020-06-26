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
import android.view.View
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.checkin.data.Data_Checkin
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager


class FriendInfoActivity : BaseActivity() {
    var image = ""
    var name = ""
    var user_id = ""
    var sessionManager: SessionManager? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_info)
        sessionManager = SessionManager(this)

        getIntents()

        tvBack.setOnClickListener()
        {
            finish()
        }

        tvCheckin.setOnClickListener {
            val intent = Intent(this@FriendInfoActivity, CheckinActivity::class.java)
            intent.putExtra("USER_ID", user_id.toString());
            intent.putExtra("fromUsername", name)

            startActivity(intent)
        }

        tvContact.setOnClickListener {
            if (sessionManager?.getValue(SessionManager.USER_ID).equals(user_id)) {
                val intent = Intent(this@FriendInfoActivity, ContactActivity::class.java)
                intent.putExtra("USER_ID", user_id.toString());
                startActivity(intent)
            } else {
                tvContact.visibility = View.GONE
            }
        }

        tvFriend.setOnClickListener()
        {
            val intent = Intent(this@FriendInfoActivity, HomeActivity::class.java)
            intent.putExtra("from", "FriendInfoActivity")
            intent.putExtra("fromValue", user_id)
            intent.putExtra("fromUsername", name)
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

package com.dish.seekdish.ui.navDrawer.friendInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.activities.ContactActivity
import com.dish.seekdish.ui.navDrawer.checkin.CheckinActivity
import kotlinx.android.synthetic.main.activity_friend_info.*

class FriendInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_info)



        tvCheckin.setOnClickListener {
                val intent = Intent(this@FriendInfoActivity, CheckinActivity::class.java)
                startActivity(intent)
        }

        tvContact.setOnClickListener {
            val intent = Intent(this@FriendInfoActivity, ContactActivity::class.java)
            startActivity(intent)
        }

    }
}

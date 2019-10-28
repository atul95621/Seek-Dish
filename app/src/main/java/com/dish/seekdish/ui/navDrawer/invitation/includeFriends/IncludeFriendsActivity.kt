package com.dish.seekdish.ui.navDrawer.invitation.includeFriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.invitation.InvitationActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_include_friends.*

class IncludeFriendsActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: IncludeFriendsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_include_friends)


        // setting up tabLayout
        this.tabLayout = findViewById(R.id.tabLayoutIncludeFriendsFrag)

        tabLayout.addTab(tabLayout.newTab().setText("Friends"))
        tabLayout.addTab(tabLayout.newTab().setText("Selected"))

//        //change font
//        changeTabsFont();

        viewPager = findViewById(R.id.viewPagerIncludeFriendsFrag) as ViewPager
        adapter = IncludeFriendsAdapter(this!!.supportFragmentManager, tabLayout.tabCount)


        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


//        tabLayout.setTabTextColors(
//                ContextCompat.getColor(this, R.color.black),
//                ContextCompat.getColor(this, R.color.black)


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


        tvConfirm.setOnClickListener()
        {
            val intent = Intent(this, InvitationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}

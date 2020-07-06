package com.dish.seekdish.ui.navDrawer.invitation.includeFriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_include_friends.*
import com.facebook.login.LoginFragment
import androidx.fragment.app.FragmentTransaction
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.invitation.includeFriendDataModels.fragments.selected.SelectedInclFragment
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.friends.FriendsInclFragment
import com.dish.seekdish.util.Global


class IncludeFriendsActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: IncludeFriendsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.dish.seekdish.R.layout.activity_include_friends)

        setFragment(1)


        /*  // setting up tabLayout
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

  */

        tvFriends.setOnClickListener()
        {
            setFragment(1)
            tvFriends.setBackgroundResource(R.drawable.tab_background_selected)
            tvSelected.setBackgroundResource(R.drawable.tab_layout_background)

        }
        tvSelected.setOnClickListener()
        {
            setFragment(2)
            tvSelected.setBackgroundResource(R.drawable.tab_background_selected)
            tvFriends.setBackgroundResource(R.drawable.tab_layout_background)

        }

        tvConfirm.setOnClickListener()
        {
            /*val intent = Intent(this, InvitationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)*/
            finish()
        }
        tvBack.setOnClickListener()
        {
            finish()
        }
    }

    fun setFragment(fargmentNo: Int) {
        val transaction: FragmentTransaction
        transaction = supportFragmentManager.beginTransaction()
        if (fargmentNo == 1) {
            transaction.replace(com.dish.seekdish.R.id.frameInvited, FriendsInclFragment())
        } else {
            transaction.replace(com.dish.seekdish.R.id.frameInvited, SelectedInclFragment())
        }
        transaction.commit()
    }
}

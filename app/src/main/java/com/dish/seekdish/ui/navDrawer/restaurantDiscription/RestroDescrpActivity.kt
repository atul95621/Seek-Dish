package com.dish.seekdish.ui.navDrawer.restaurantDiscription

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.R
import com.dish.seekdish.custom.PagerContainer
import com.dish.seekdish.ui.navDrawer.dishDescription.MealRatingActivity
import com.dish.seekdish.ui.navDrawer.invitation.InvitationActivity
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.adapter.RestroDescrpAdapter
import com.google.android.material.tabs.TabLayout
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_restro_description.*
import kotlinx.android.synthetic.main.activity_restro_description.imgGoogleApp
import kotlinx.android.synthetic.main.activity_restro_description.imgRatings
import kotlinx.android.synthetic.main.activity_restro_description.tvActions
import kotlinx.android.synthetic.main.activity_restro_description.tvBack
import java.util.ArrayList

class RestroDescrpActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: RestroDescrpAdapter


    // for images
    internal lateinit var mContainer: PagerContainer
    internal lateinit var pager: ViewPager
    internal lateinit var adapterPager: PagerAdapter


    internal var mResources = intArrayOf(
        R.drawable.ic_foodex,
        R.drawable.ic_pasta,
        R.drawable.ic_foodimg,
        R.drawable.ic_foodex,
        R.drawable.ic_pasta,
        R.drawable.ic_foodimg
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restro_description)


        //for swipe images on top
        initializeviews()




        // setting up tabLayout
        this.tabLayout = findViewById(R.id.tabLayoutRestroActivity)

        tabLayout.addTab(tabLayout.newTab().setText("Meals"))
        tabLayout.addTab(tabLayout.newTab().setText("Similar"))
        tabLayout.addTab(tabLayout.newTab().setText("Details"))

//        //change font
//        changeTabsFont();

        viewPager = findViewById(R.id.viewPagerRestroActivity) as ViewPager
        adapter = RestroDescrpAdapter(this.supportFragmentManager, tabLayout.tabCount)


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

        imgGoogleApp.setOnClickListener()
        {
            val intent = Intent(
                android.content.Intent.ACTION_VIEW,
                Uri.parse(
                    "http://maps.google.com/maps?saddr=" + "&daddr=" + 27.480253 + "," + 74.847381 + "(Event Location)"
                )
            )
            startActivity(intent)
        }

        tvActions.setOnClickListener()
        {
            onShare()
        }


        tvBack.setOnClickListener()
        {
            finish()
        }

        imgRatings.setOnClickListener()
        {
            val intent = Intent(this@RestroDescrpActivity, MealRatingActivity::class.java)
            startActivity(intent)
        }

        imgInvitation.setOnClickListener()
        {
            val intent = Intent(this@RestroDescrpActivity, InvitationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onShare() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_share_restro)

        val tvShare = dialog.findViewById<TextView>(R.id.tvShare)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val tvAlert = dialog.findViewById<TextView>(R.id.tvAlert)
        val tvShowMap = dialog.findViewById<TextView>(R.id.tvShowMap)


        tvShowMap.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "http://maps.google.com/maps?saddr=" + "&daddr=" + 27.480253 + "," + 74.847381 + "(Event Location)"
                )
            )
            startActivity(intent)
        }


        // button_yes clk
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvShare.setOnClickListener()
        {
            onShareSocial()
            dialog.dismiss()
        }

        dialog.show()

    }


    private fun onShareSocial() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_social_share)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))   // making backgrnd color tarnsparent code begind progress circle bar
        dialog.window!!.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)

        val ic_facebk = dialog.findViewById<ImageView>(R.id.ic_facebk)
        val imgTwitter = dialog.findViewById<ImageView>(R.id.imgTwitter)

        ic_facebk.setOnClickListener()
        {
            dialog.dismiss()
        }

        imgTwitter.setOnClickListener()
        {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun initializeviews() {

        mContainer = findViewById(R.id.pager_container) as PagerContainer
        pager = mContainer.viewPager
        adapterPager = MyPageradapterPager(this)
        pager.adapter = adapterPager
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.offscreenPageLimit = adapterPager.count
        //A little space between pages
        pager.pageMargin = 15
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.clipChildren = false


        //setting dots with viewpager...
        springDotsIndicator.setViewPager(pager)


    }

//Nothing special about this adapterPager, just throwing up colored views for demo

    private inner class MyPageradapterPager(internal var mContext: Context) : PagerAdapter() {
        override fun getCount(): Int {
            return mResources.size
        }

        internal var mLayoutInflater: LayoutInflater

        init {
            mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as LinearLayout
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false)

            val imageView = itemView.findViewById(R.id.imageView) as ImageView
            val profile_image = itemView.findViewById(R.id.profile_image) as CircleImageView

            imageView.setImageResource(mResources[position])
            profile_image.setImageResource(mResources[position])
            container.addView(itemView)

            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as LinearLayout)
        }
    }
}


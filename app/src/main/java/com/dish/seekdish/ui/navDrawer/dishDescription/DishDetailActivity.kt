package com.dish.seekdish.ui.navDrawer.dishDescription

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.PagerContainer
import kotlinx.android.synthetic.main.activity_opinion_details.*

class DishDetailActivity : BaseActivity() {


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
        setContentView(R.layout.activity_dish_detail)

        //for swipe images on top
        initializeviews()


        tvBack.setOnClickListener()
        {
            finish()
        }


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
            val itemView = mLayoutInflater.inflate(R.layout.pager_detail_item, container, false)

            val imageView = itemView.findViewById(R.id.imageView) as ImageView

//            imageView.setOnClickListener()
//            {
//                val intent = Intent(this@DishDetailActivity, DishDetailActivity::class.java)
//                startActivity(intent)
//            }

            imageView.setImageResource(mResources[position])
            container.addView(itemView)

            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as LinearLayout)
        }
    }
}

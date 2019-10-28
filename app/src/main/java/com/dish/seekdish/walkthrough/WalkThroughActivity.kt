package com.dish.seekdish.walkthrough

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dish.seekdish.R
import com.dish.seekdish.util.UiHelper
import com.dish.seekdish.walkthrough.adapter.MyPagerAdapter
import com.dish.seekdish.walkthrough.listener.ViewPagerListener
import com.dish.seekdish.walkthrough.fragments.IntroFirstFragment
import com.dish.seekdish.walkthrough.fragments.IntroSecondFragment
import com.dish.seekdish.walkthrough.fragments.IntroThirdFragment
import com.dish.seekdish.walkthrough.fragments.RegisterFragment


import kotlinx.android.synthetic.main.activity_walkthrough.*
import android.content.Intent
import android.util.Log
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import android.R.attr.data


class WalkThroughActivity : AppCompatActivity() {

    companion object {
        private const val MIN_SCALE = 0.65f
        private const val MIN_ALPHA = 0.3f
    }

    private lateinit var pagerAdapterView: MyPagerAdapter
    private val uiHelper = UiHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_walkthrough)


        pagerAdapterView = MyPagerAdapter(supportFragmentManager)
        addPagerFragments()
        myViewPager.adapter = pagerAdapterView
        // viewpager pages animation
//        myViewPager.setPageTransformer(true, this::zoomOutTransformation)
        myViewPager.addOnPageChangeListener(ViewPagerListener(this::onPageSelected))


    }


    private fun onPageSelected(position: Int) {
        when (position) {
            0 -> {
                firstDotImageView.setImageResource(R.drawable.ic_white_dot)
                secondDotImageView.setImageResource(R.drawable.ic_grey_dot)
                thirdDotImageView.setImageResource(R.drawable.ic_grey_dot)
                fourthDotImageView.setImageResource(R.drawable.ic_grey_dot)

            }
            1 -> {
                firstDotImageView.setImageResource(R.drawable.ic_grey_dot)
                secondDotImageView.setImageResource(R.drawable.ic_white_dot)
                thirdDotImageView.setImageResource(R.drawable.ic_grey_dot)
                fourthDotImageView.setImageResource(R.drawable.ic_grey_dot)

            }
            2 -> {
                firstDotImageView.setImageResource(R.drawable.ic_grey_dot)
                secondDotImageView.setImageResource(R.drawable.ic_grey_dot)
                thirdDotImageView.setImageResource(R.drawable.ic_white_dot)
                fourthDotImageView.setImageResource(R.drawable.ic_grey_dot)

            }
            3 -> {
                firstDotImageView.setImageResource(R.drawable.ic_grey_dot)
                secondDotImageView.setImageResource(R.drawable.ic_grey_dot)
                thirdDotImageView.setImageResource(R.drawable.ic_grey_dot)
                fourthDotImageView.setImageResource(R.drawable.ic_white_dot)
            }
        }
    }

    private fun addPagerFragments() {
        pagerAdapterView.addFragments(RegisterFragment())
        pagerAdapterView.addFragments(IntroFirstFragment())
        pagerAdapterView.addFragments(IntroSecondFragment())
        pagerAdapterView.addFragments(IntroThirdFragment())
    }

    private fun zoomOutTransformation(page: View, position: Float) {
        when {
            position < -1 ->
                page.alpha = 0f
            position <= 1 -> {
                page.scaleX = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.scaleY = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.alpha = Math.max(MIN_ALPHA, 1 - Math.abs(position))
            }
            else -> page.alpha = 0f
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (f in fragments) {
                if (f is RegisterFragment) {
                    Log.e("Twitter", "Acitivty onActivityresult called")
                    f.onActivityResult(requestCode, resultCode, data)
                } /*else if (f is CreateEventFragment) {
                      f.onActivityResult(requestCode, resultCode, data)
                  }*/
            }
        }

    }


}

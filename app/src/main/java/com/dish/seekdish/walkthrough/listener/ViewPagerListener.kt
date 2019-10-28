package com.dish.seekdish.walkthrough.listener

import androidx.viewpager.widget.ViewPager

class ViewPagerListener(private val closure: (Int) -> Unit) : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(position: Int) = closure(position)
}
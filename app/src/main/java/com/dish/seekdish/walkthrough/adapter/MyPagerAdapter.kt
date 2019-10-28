package com.dish.seekdish.walkthrough.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragmentList: MutableList<Fragment> = ArrayList()

    override fun getItem(position: Int) = fragmentList[position]

    override fun getCount() = fragmentList.size

    fun addFragments(fragment: Fragment) = fragmentList.add(fragment)
}
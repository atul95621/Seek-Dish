package com.dish.seekdish.ui.navDrawer.toDo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dish.seekdish.ui.navDrawer.toDo.list.ListTodoDataClass

import com.dish.seekdish.ui.navDrawer.toDo.list.ListTodoFragment
import com.dish.seekdish.ui.navDrawer.toDo.map.TodoMap

class TodoAdapter(
    fm: FragmentManager,
    private val mNumOfTabs: Int
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return ListTodoFragment()
            }
            1 -> {
                return TodoMap()
            }

            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
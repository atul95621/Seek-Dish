package com.dish.seekdish.ui.navDrawer.toDo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager

import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.toDo.VM.TodoVM
import com.dish.seekdish.ui.navDrawer.toDo.list.ListTodoDataClass
import com.dish.seekdish.ui.navDrawer.toDo.list.ListTodoFragment
import com.dish.seekdish.ui.navDrawer.toDo.map.TodoMap
import com.dish.seekdish.util.BaseFragment
import com.dish.seekdish.util.SessionManager
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.ArrayList


class TodoFragment : BaseFragment() {
    lateinit var tabLayout: TabLayout
//    internal lateinit var viewPager: ViewPager
//    internal lateinit var adapter: TodoAdapter
    internal var arrayList = ArrayList<ListTodoDataClass>()

    var todoVM: TodoVM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo, container, false)

        todoVM = ViewModelProviders.of(this).get(TodoVM::class.java)

        // setting up tabLayout
        this.tabLayout = view.findViewById(R.id.tabLayoutTodoFrag)

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.lists)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.map)))

        replaceFragment(ListTodoFragment())


        /*  viewPager = view.findViewById(R.id.viewPagerTodoFrag) as ViewPager
          adapter = TodoAdapter(activity!!.supportFragmentManager, tabLayout.tabCount)
          viewPager.adapter = adapter
          viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))*/

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.currentItem = tab.position
                setCurrentTabFragment(tab.getPosition());

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        return view


    }

    fun setCurrentTabFragment(tabPosition: Int) {
        when (tabPosition) {
            0 -> replaceFragment(ListTodoFragment())
            1 -> replaceFragment(TodoMap())
        }
    }
    fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction
        transaction = childFragmentManager.beginTransaction()
        transaction.replace(com.dish.seekdish.R.id.content_todo_fragment, fragment)
        transaction.commit()
    }

}

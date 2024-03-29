package com.dish.seekdish.ui.home

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dish.seekdish.Constants
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.home.adapter.FilterAdapter
import com.dish.seekdish.ui.home.dataModel.FilterDataModel
import com.dish.seekdish.ui.home.fragments.HomeFragment
import com.dish.seekdish.ui.home.viewModel.HomeActivityVM
import com.dish.seekdish.ui.navDrawer.activities.MyProfileActivity
import com.dish.seekdish.ui.navDrawer.myFavourite.MyFavouriteFragment
import com.dish.seekdish.ui.navDrawer.myFriends.MyFriendsFragment
import com.dish.seekdish.ui.navDrawer.notifications.NotificationFarg
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.ChildData
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.GroupData
import com.dish.seekdish.ui.navDrawer.restaurants.RestaurantsFragment
import com.dish.seekdish.ui.navDrawer.settings.SettingsFragment
import com.dish.seekdish.ui.navDrawer.toDo.TodoFragment
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.Global
import com.dish.seekdish.util.SessionManager
import com.dish.seekdish.walkthrough.WalkThroughActivity
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.twitter.sdk.android.core.*
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import android.text.TextUtils.join as join1


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    // for right filters
    private val team = LinkedHashMap<String, GroupData>()
    private val deptList = ArrayList<GroupData>()


    private var listAdapter: FilterAdapter? = null
    private var simpleExpandableListView: ExpandableListView? = null
    private var btnSaveFilterItems: Button? = null

    //    private var rightToggle: ActionBarDrawerToggle? = null
    lateinit var toggle: ActionBarDrawerToggle
    var homeActivityVM: HomeActivityVM? = null

    internal var filterArrayList = ArrayList<FilterDataModel>()


    //___________________

    lateinit var tvTitle: TextView
    lateinit var tvAdd: TextView
    lateinit var imgHamburger: ImageView
    lateinit var imgFilters: ImageView
    lateinit var imageViewNavDrawer: CircleImageView
    lateinit var tvName: TextView

    var sessionManager: SessionManager? = null;

    // stored filtered items
    var budgetItems = ""
    var serviceSpeedItems = ""
    var mealItems = ""
    var compatIntolerItems = ""
    var restroSpecialItems = ""
    var restroAmbianceItems = ""
    var compAmbianceItems = ""
    var additonalItems = ""
    var seasonlityItems = ""
    var from: String? = null
    var fromValue: String? = null
    var fromUsername: String? = null
    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null
    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sessionManager = SessionManager(this)
        homeActivityVM = ViewModelProvider(this).get(HomeActivityVM::class.java)

        tvTitle = findViewById(R.id.tvToolbarTitle) as TextView
        tvAdd = findViewById(R.id.tvAdd) as TextView
        imgHamburger = findViewById(R.id.imgHamburger) as ImageView
        imgFilters = findViewById(R.id.imgFilters) as ImageView

        checkIfUpdateAvailable()

        from = intent.getStringExtra("from")

// setting imitial fragment to HomeFragment for "HOMEPAGE"
        if (from == null) {
            setInitialFragment()
        }


        // hiding custom icon
        imgHamburger.visibility = View.GONE

        val toolbar = findViewById(R.id.toolbar) as Toolbar

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById<NavigationView>(R.id.nav_view)
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout?.addDrawerListener(toggle)
//        toggle.setHomeAsUpIndicator(R.drawable.ic_hamburger);

        toggle.syncState()

        navView?.setNavigationItemSelectedListener(this)

        // setting  text to navigation header
        val headerView = navView?.getHeaderView(0)
        imageViewNavDrawer = headerView?.findViewById(R.id.imageViewNavDrawer) as CircleImageView
        tvName = headerView?.findViewById(R.id.tvName) as TextView

//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,right_drawer);


        //hitting api when drawer gets opened...
        drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(p0: Int) {
            }

            override fun onDrawerSlide(p0: View, p1: Float) {

            }

            override fun onDrawerClosed(p0: View) {
            }

            override fun onDrawerOpened(p0: View) {
//                Log.e("drawerDirection", ":  " + drawerLayout?.isDrawerOpen(GravityCompat.END))
                if (drawerLayout?.isDrawerOpen(GravityCompat.END) == true) {
                    // hitiing apai if user slide the right drawer
                    getFilterData()
                }

            }
        })
        // setting user name and image in nav drawer
        setUserDetail()

        // initialising twitter for logout
        setUpTwitter()


// callbacks from activities
        fromValue = intent.getStringExtra("fromValue")
        fromUsername = intent.getStringExtra("fromUsername")

        if (from != null) {
            if (from.equals("MyProfileActivity")) {
                tvTitle.setText(resources.getString(R.string.my_friends))
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(
                    R.id.content_frame,
                    MyFriendsFragment(sessionManager?.getValue(SessionManager.USER_ID).toString())
                ).commit()
            } else if (from.equals("FriendInfoActivity")) {
                if (!fromValue.isNullOrEmpty()) {
                    if (sessionManager?.getValue(SessionManager.USER_ID).equals(fromValue)) {
                        tvTitle.setText(resources.getString(R.string.my_friends))
                    } else {
                        tvTitle.setText(
                            fromUsername + "'s" + resources.getString(R.string.space) + resources.getString(
                                R.string.friends
                            )
                        )
                    }
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction().replace(
                        R.id.content_frame,
                        MyFriendsFragment(fromValue.toString())
                    ).commit()
                }
            } else if (from.equals("SettingsFragment")) {
                if (!fromValue.isNullOrEmpty()) {
                    // showing back button
                    hideHamburgerIcon()
                    // disabling the right and left gesture to open nav drawer
                    drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    tvTitle.setText(resources.getString(R.string.my_friends))
                    tvAdd.visibility = View.VISIBLE
                    imgFilters.visibility = (View.INVISIBLE)
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction().replace(
                        R.id.content_frame,
                        MyFriendsFragment(fromValue.toString())
                    ).commit()
                }
            } else if (from.equals("Notification")) {
                tvTitle.setText(resources.getString(R.string.notifications))
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(
                    R.id.content_frame,
                    NotificationFarg()
                ).commit()
            }
        }

        //============================

        // clickListener for darwer items
        imageViewNavDrawer.setOnClickListener()
        {
            val intent = Intent(this@HomeActivity, MyProfileActivity::class.java)
            startActivity(intent)
        }

        tvName.setOnClickListener()
        {
            val intent = Intent(this@HomeActivity, MyProfileActivity::class.java)
            startActivity(intent)
        }

        // for left drawerr
        /*  imgHamburger.setOnClickListener(View.OnClickListener {
              if (drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                  drawerLayout?.closeDrawer(GravityCompat.START)
              } else {
                  drawerLayout?.openDrawer(GravityCompat.START)
              }
          })*/

        // for right drawer that is filter
        imgFilters.setOnClickListener(View.OnClickListener {
            if (drawerLayout?.isDrawerOpen(GravityCompat.END)!!) {
                drawerLayout?.closeDrawer(GravityCompat.END)
            } else {

//                getFilterData()
                drawerLayout?.openDrawer(GravityCompat.END)
            }
        })


        //=========================== right filter
        // update observer
        checkUpdateObserver()

        // filter Observer
        getFilterObserver()

        //logout observer
        getLogoutObserver()

        // filter save observer
        saveFilterObserver()

        // to get unread notification
        getNotificationQtyOberver()

        //get reference of the ExpandableListView
        simpleExpandableListView = findViewById(R.id.filterExpandableListView) as ExpandableListView
        btnSaveFilterItems = findViewById(R.id.btnSaveFilterItems) as Button

        // to open all the childs
        /*  val count = listAdapter!!.getGroupCount()
          for (i in 0 until count)
              simpleExpandableListView!!.expandGroup(i)*/

        // setOnChildClickListener listener for child row click
        simpleExpandableListView!!.setOnChildClickListener(ExpandableListView.OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            //get the group header
            val headerInfo = deptList[groupPosition]
            //get the child info
            val detailInfo = headerInfo.playerName[childPosition]
            //display it or do something with it
            /*      Toast.makeText(
                      this, " Team And Player :: " + headerInfo.name
                              + "/" + detailInfo.name, Toast.LENGTH_LONG
                  ).show()*/
            false
        })
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView!!.setOnGroupClickListener(ExpandableListView.OnGroupClickListener { parent, v, groupPosition, id ->
            //get the group header
            val headerInfo = deptList[groupPosition]
            //display it or do something with it
            /* Toast.makeText(
                 this, " Team Name :: " + headerInfo.name,
                 Toast.LENGTH_LONG
             ).show()
*/
            false
        })

        btnSaveFilterItems!!.setOnClickListener()
        {

            budgetItems = join1(",", Global.budgetSet)
            serviceSpeedItems = join1(",", Global.serviceSet)
            mealItems = join1(",", Global.mealSet)
            compatIntolerItems = join1(",", Global.compatIntSet)
            restroSpecialItems = join1(",", Global.restroSpeclSet)
            restroAmbianceItems = join1(",", Global.restroAmbiSet)
            compAmbianceItems = join1(",", Global.compAmbianceSet)
            additonalItems = join1(",", Global.additonalSet)
            seasonlityItems = join1(",", Global.seasonlitySet)


            /*   if (budgetItems == "" && serviceSpeedItems == "" && mealItems == "" && compatIntolerItems == "" && restroSpecialItems == "" && compAmbianceItems == "" && additonalItems == "" && seasonlityItems == "") {
                   Toast.makeText(this, "Please select filter items first", Toast.LENGTH_LONG).show()
               } else {*/

            var switchConsider = "0"
            if (switch_consider_my_profile.isChecked) {
                switchConsider = "1"
            } else {
                switchConsider = "0"
            }
            homeActivityVM?.doSaveFilterData(
                budgetItems,
                serviceSpeedItems,
                mealItems,
                compatIntolerItems,
                restroAmbianceItems,
                restroSpecialItems,
                compAmbianceItems,
                additonalItems,
                seasonlityItems,
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                switchConsider
            )
        }

    }

    private fun checkIfUpdateAvailable() {
        homeActivityVM?.checkUpdate()
    }


    private fun getFilterData() {
        // hitting api

        if (sessionManager?.getValue(SessionManager.LANGUAGE_ID).toString().isNullOrEmpty()) {
            homeActivityVM?.doGetFilterData(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                sessionManager?.getValue(SessionManager.LANGUAGE_HOME_ACTIVITY).toString()
            )
        } else {
            homeActivityVM?.doGetFilterData(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                sessionManager?.getValue(SessionManager.LANGUAGE_ID).toString()
            )
        }
    }

    private fun setUserDetail() {
        if (!sessionManager?.getValue(SessionManager.PHOTO_URL).isNullOrEmpty()) {
            GlideApp.with(this)
                .load(sessionManager?.getValue(SessionManager.PHOTO_URL))
                .placeholder(R.drawable.ic_user)
                .into(imageViewNavDrawer)
        } else {
            GlideApp.with(this)
                .load(R.drawable.ic_user)
                .placeholder(R.drawable.ic_user)
                .into(imageViewNavDrawer)
        }

        tvName.setText(sessionManager?.getValue(SessionManager.USERNAME) ?: "Null")

    }


    // here we maintain team and player names
    private fun addProduct(
        categoryName: String,
        itemName: String,
        itemId: Int,
        selected: Int
    ): Int {

        var groupPosition = 0

        //check the hash map if the group already exists
        var headerInfo: GroupData? = team[categoryName]
        //add the group if doesn't exists
        if (headerInfo == null) {
            headerInfo = GroupData()
            headerInfo.name = categoryName
            team[categoryName] = headerInfo
            deptList.add(headerInfo)
        }

        // get the children for the group
        val productList = headerInfo.playerName
        // size of the children list
        var listSize = productList.size
        // add to the counter
        listSize++

        // create a new child and add that to the group
        val detailInfo = ChildData()
        detailInfo.name = itemName
        detailInfo.itemId = itemId.toString()
        detailInfo.groupName = headerInfo.name
        detailInfo.selected = selected.toString()

        productList.add(detailInfo)
        headerInfo.playerName = productList

        // find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo)
        return groupPosition
    }


    private fun setInitialFragment() {
        tvTitle.setText(resources.getString(R.string.home))
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.content_frame, HomeFragment()).commit()
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // isTaskRoot tells if the activity is last one in stack
            if (isTaskRoot()) {
                // alert for user when backispressed
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    finish();
                    return;
                }
                doubleBackToExitPressedOnce = true
                Toast.makeText(this, getString(R.string.back_again), Toast.LENGTH_SHORT).show()

                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            } else {
                super.onBackPressed();
                finish();
                return;
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        drawerLayout.closeDrawer(GravityCompat.START)

// addding a delay so that fragment gets changes and then drawer is closed so lag cant happen...

        Handler().postDelayed({

            // Handle navigation view item clicks here.
            when (item.itemId) {
                R.id.nav_home -> {
                    imgFilters.visibility = (View.VISIBLE)
                    tvAdd.visibility = (View.INVISIBLE)
                    tvTitle.setText(resources.getString(R.string.home))
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, HomeFragment())
                        .commit()

                }
                R.id.nav_restaurants -> {
                    tvAdd.visibility = (View.INVISIBLE)
                    tvTitle.setText(resources.getString(R.string.restaurant))
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, RestaurantsFragment())
                        .commit()
                }
                R.id.nav_my_friends -> {
                    tvAdd.setText(resources.getString(R.string.add))
                    tvAdd.visibility = (View.VISIBLE)
                    tvTitle.setText(resources.getString(R.string.my_friends))
                    imgFilters.visibility = (View.INVISIBLE)

                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(
                            R.id.content_frame,
                            MyFriendsFragment(
                                sessionManager?.getValue(SessionManager.USER_ID).toString()
                            )
                        )
                        .commit()
                }
                R.id.nav_favorite_menu -> {
                    tvAdd.visibility = (View.INVISIBLE)
                    imgFilters.visibility = (View.INVISIBLE)
                    tvTitle.setText(resources.getString(R.string.my_favourites))
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, MyFavouriteFragment())
                        .commit()

                }
                R.id.nav_todo -> {
                    tvAdd.visibility = (View.INVISIBLE)
                    imgFilters.visibility = (View.INVISIBLE)
                    tvTitle.setText(resources.getString(R.string.todo))
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, TodoFragment())
                        .commit()
                }
                R.id.nav_notifications -> {
                    tvAdd.visibility = (View.INVISIBLE)
                    imgFilters.visibility = (View.INVISIBLE)
                    tvTitle.setText(resources.getString(R.string.notifications))
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, NotificationFarg())
                        .commit()
                }
                R.id.nav_settings -> {

                    tvAdd.visibility = (View.VISIBLE)
                    tvAdd.setText(resources.getString(R.string.save))

                    imgFilters.visibility = (View.INVISIBLE)
                    tvTitle.setText(resources.getString(R.string.settings))

                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, SettingsFragment(this))
                        .commit()
                }
                R.id.nav_logout -> {
                    // hitting api
                    homeActivityVM?.doLogout(
                        sessionManager?.getValue(SessionManager.USER_ID).toString()
                    )
                }
            }
        }, 300)

//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        drawerLayout.closeDrawer(GravityCompat.START)
//        Handler().postDelayed({
////        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//            drawerLayout.closeDrawer(GravityCompat.START)
//        }, 300)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (f in fragments) {
                if (f is SettingsFragment) {
                    f.onActivityResult(requestCode, resultCode, data)
                }
                /*else if (f is CreateEventFragment) {
                      f.onActivityResult(requestCode, resultCode, data)
                  }*/
            }
        }

    }


    private fun getFilterObserver() {
        //observe
        homeActivityVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        homeActivityVM!!.getFilterLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    var arrySize = filterArrayList.size
                    if (deptList.size == 0) {
                        for (items in response.data.consider_my_profile) {
                            if (items.filter == 1) {
                                switch_consider_my_profile.isChecked = true
                            } else {
                                switch_consider_my_profile.isChecked = false
                            }
                        }
                        for (items in response.data.additional_services) {
                            addProduct(
                                getString(R.string.addtional_service),
                                items.name,
                                items.id,
                                items.selected
                            )
                        }
                        /*       for (items in response.data.budget) {
                                   Log.e("Budget", items.name)
                                   addProduct(
                                       getString(R.string.budget),
                                       items.name,
                                       items.id,
                                       items.selected
                                   )
                               }*/
                        for (items in response.data.intolerance_compatibilities) {
                            addProduct(
                                getString(R.string.comp_intolr),
                                items.name,
                                items.id,
                                items.selected
                            )
                        }
                        for (items in response.data.ambiance_complementary) {
                            addProduct(
                                getString(R.string.comp_ambiance),
                                items.name,
                                items.id,
                                items.selected
                            )
                        }
                        for (items in response.data.meal_types) {
                            addProduct(
                                getString(R.string.meal_type),
                                items.name,
                                items.id,
                                items.selected
                            )
                        }
                        for (items in response.data.ambiance) {
                            addProduct(
                                getString(R.string.restro_ambiance),
                                items.name,
                                items.id,
                                items.selected
                            )
                        }
                        for (items in response.data.speciality) {
                            addProduct(
                                getString(R.string.restro_speacial),
                                items.name,
                                items.id,
                                items.selected
                            )
                        }
                        for (items in response.data.seasons) {
                            addProduct(
                                getString(R.string.seasonali),
                                items.name,
                                items.id,
                                items.selected
                            )
                        }
                        for (items in response.data.service_speed) {
                            addProduct(
                                getString(R.string.service_sped),
                                items.name,
                                items.id,
                                items.selected
                            )
                        }

                        // create the adapter by passing your ArrayList data
                        listAdapter = FilterAdapter(this, deptList)
                        // attach the adapter to the expandable list view
                        simpleExpandableListView!!.setAdapter(listAdapter)
                    }
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    $response"
                );
            }
        })
    }

    private fun getLogoutObserver() {
        //observe
        homeActivityVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        homeActivityVM!!.getLogoutLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    TwitterCore.getInstance().getSessionManager().clearActiveSession()

                    if (LoginManager.getInstance() != null) {
                        LoginManager.getInstance().logOut();
                    }


                    //clearing the values of session
                    sessionManager?.clearValues();

                    val intent = Intent(this@HomeActivity, WalkThroughActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    $response"
                );
            }
        })
    }

    private fun saveFilterObserver() {
        //observe
        homeActivityVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        homeActivityVM!!.saveFilterLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    drawerLayout?.closeDrawer(GravityCompat.END)
                    val message = response.message
                    showSnackBar(message)
                    val finalFrag =
                        Constants.refreshFragment(
                            sessionManager?.getValue(SessionManager.CURRENT_SCREEN).toString()
                        )

                    reOpenFrag(finalFrag)
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    $response"
                );
            }
        })
    }

    private fun checkUpdateObserver() {
        //observe
        /*    homeActivityVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
                setIsLoading(it)
            }*/

        homeActivityVM!!.getUpdateLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {

                    sessionManager?.setValues(
                        SessionManager.RESTRO_MAP_QTY,
                        response.restro_map_qty.toString()
                    )
                    sessionManager?.setValues(
                        SessionManager.MEAL_MAP_QTY,
                        response.meal_map_qty.toString()
                    )
                    var version = ""
                    try {
                        val pInfo: PackageInfo =
                            this.getPackageManager().getPackageInfo(packageName, 0)
                        version = pInfo.versionName
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                    if (!version.isNullOrEmpty()) {
                        if (version.toFloat() < response.Android_version.toFloat()) {
                            //making the isLoggedIn key to "0"
                            sessionManager?.setValues(SessionManager.LOGGEDIN, "0")
                            val intent = Intent(this@HomeActivity, WalkThroughActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    $response"
                );
            }
        })
    }

    private fun getColoredSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }

    private fun getNotificationQtyOberver() {

        homeActivityVM!!.getNotificationQtyLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
                    // making the navigation view item dynamic and providing the dynamic notification
                    if (navView != null) {
                        var menu: Menu = navView!!.getMenu()
                        val nav_login = menu.findItem(R.id.nav_notifications)
                        var qtyNotify = "(" + response.data.quantityOfNotification.toString() + ")"
                        val surName: String = getColoredSpanned(qtyNotify, "#FF0000")
                        nav_login.title =
                            Html.fromHtml(getString(R.string.notifications) + " " + surName)
                    }
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(
                    this.getResources().getString(R.string.error_occured) + "    $response"
                );
            }
        })
    }


    private fun setUpTwitter() {

        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    getResources().getString(R.string.CONSUMER_KEY),
                    getResources().getString(R.string.CONSUMER_SECRET)
                )
            )
            .debug(true)
            .build()

        Twitter.initialize(config)

    }

    override fun onResume() {
        super.onResume()

        var photoUrl = sessionManager?.getValue(SessionManager.PHOTO_URL);
        var name = sessionManager?.getValue(SessionManager.FIRST_NAME);
        var USER_ID = sessionManager?.getValue(SessionManager.USER_ID);

        // fetching notifications quantity
        homeActivityVM?.notificationCount(USER_ID.toString())

        if (photoUrl != null && photoUrl != "null" && photoUrl != "") {

            GlideApp.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.ic_user)
                .into(imageViewNavDrawer)

            tvName.setText(sessionManager?.getValue(SessionManager.USERNAME))
        }
        if (name != null && name != "null" && name != "") {
            tvName.setText(sessionManager?.getValue(SessionManager.USERNAME))
        }
    }


    fun reOpenFrag(finalFrag: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.content_frame, finalFrag)
            .commit()
    }

    fun hideHamburgerIcon() {
        toggle.setDrawerIndicatorEnabled(false);
        // here imgHamburger has back button icon
        imgHamburger.visibility = View.VISIBLE
        imgHamburger.setOnClickListener()
        {
            finish()
        }
    }
}

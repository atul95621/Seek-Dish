package com.dish.seekdish.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log

import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import android.view.Menu

import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.fragments.HomeFragment
import com.dish.seekdish.ui.navDrawer.activities.MyProfileActivity
import com.dish.seekdish.ui.navDrawer.myFavourite.MyFavouriteFragment
import com.dish.seekdish.ui.navDrawer.myFriends.MyFriendsFragment
import com.dish.seekdish.ui.navDrawer.restaurants.RestaurantsFragment
import com.dish.seekdish.ui.navDrawer.settings.SettingsFragment
import de.hdodenhof.circleimageview.CircleImageView

import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.ui.home.adapter.FilterAdapter
import com.dish.seekdish.ui.home.dataModel.FilterDataModel
import com.dish.seekdish.ui.home.viewModel.HomeActivityVM
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.ChildData
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.details.GroupData
import com.dish.seekdish.ui.navDrawer.settings.dataModel.Data_Liked
import com.dish.seekdish.ui.navDrawer.settings.viewModel.LikeVM
import com.dish.seekdish.ui.navDrawer.toDo.TodoFragment
import com.dish.seekdish.util.Global
import com.dish.seekdish.util.SessionManager
import com.dish.seekdish.walkthrough.WalkThroughActivity
import com.facebook.FacebookSdk
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import java.util.ArrayList
import java.util.LinkedHashMap
import android.text.TextUtils.join as join1


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    // for right filters
    private val team = LinkedHashMap<String, GroupData>()
    private val deptList = ArrayList<GroupData>()


    private var listAdapter: FilterAdapter? = null
    private var simpleExpandableListView: ExpandableListView? = null
    private var btnSaveFilterItems: Button? = null

    private var rightToggle: ActionBarDrawerToggle? = null

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sessionManager = SessionManager(this)
        homeActivityVM = ViewModelProviders.of(this).get(HomeActivityVM::class.java)



        tvTitle = findViewById(R.id.tvToolbarTitle) as TextView
        tvAdd = findViewById(R.id.tvAdd) as TextView
        imgHamburger = findViewById(R.id.imgHamburger) as ImageView
        imgFilters = findViewById(R.id.imgFilters) as ImageView


// setting imitial fragment to HomeFragment for "HOMEPAGE"
        setInitialFragment()

        val toolbar = findViewById(R.id.toolbar) as Toolbar

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        var toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.setHomeAsUpIndicator(R.drawable.ic_hamburger);

        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        // setting  text to navigation header
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        imageViewNavDrawer = headerView.findViewById(R.id.imageViewNavDrawer) as CircleImageView
        tvName = headerView.findViewById(R.id.tvName) as TextView

        // setting user name and image in nav drawer
        setUserDetail()

        // initialising twitter for logout
        setUpTwitter()


// callbacks from activities
        from = intent.getStringExtra("from")
        if (from != null) {
            if (from.equals("MyProfileActivity")) {
                tvTitle.setText("My Friends")
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(
                    R.id.content_frame,
                    MyFriendsFragment()
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

        // for left drawerr
        imgHamburger.setOnClickListener(View.OnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        })

        // for right drawer that is filter

        imgFilters.setOnClickListener(View.OnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                Log.e("deptList size", " " + deptList.size)

                getFilterData()
                drawerLayout.openDrawer(GravityCompat.END)
            }
        })


        //=========================== right filter


        // filter Observer
        getFilterObserver()

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
            Toast.makeText(
                this, " Team And Player :: " + headerInfo.name
                        + "/" + detailInfo.name, Toast.LENGTH_LONG
            ).show()
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


            if (budgetItems == "" && serviceSpeedItems == "" && mealItems == "" && compatIntolerItems == "" && restroSpecialItems == "" && compAmbianceItems == "" && additonalItems == "" && seasonlityItems == "") {
                Toast.makeText(this, "Please select filter items first", Toast.LENGTH_LONG).show()
            } else {
                Log.e(
                    "filterItems",
                    "" + "\nbudget   " + budgetItems + "\nservice  " + serviceSpeedItems + "\nmeal   " + mealItems + "\ncompat   " + compatIntolerItems + "\nrestro   " + restroSpecialItems + "\ncompAm  " + compAmbianceItems + "\naddtional   " + additonalItems + "\nseason   " + seasonlityItems
                )
            }
        }

        /*   //drawer set up
           rightToggle = ActionBarDrawerToggle(
               this, drawerLayout, toolbar,
               R.string.app_name, R.string.app_name
           )
           rightToggle!!.isDrawerIndicatorEnabled = false
           rightToggle!!.setHomeAsUpIndicator(R.drawable.ic_settings_button)
           drawerLayout.addDrawerListener(toggle)
           rightToggle!!.syncState()

           //toogle listener
           rightToggle!!.toolbarNavigationClickListener = View.OnClickListener {
               if (drawerLayout.isDrawerVisible(GravityCompat.END)) {
                   drawerLayout.closeDrawer(GravityCompat.END)
               } else {
                   drawerLayout.openDrawer(GravityCompat.END)
               }
           }*/


    }

    private fun getFilterData() {
        // hitting api
        homeActivityVM?.doGetFilterData(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            sessionManager?.getValue(SessionManager.LANGUAGE_ID).toString()
        )
    }

    private fun setUserDetail() {
        if (!sessionManager?.getValue(SessionManager.PHOTO_URL).equals(null) && !sessionManager?.getValue(SessionManager.PHOTO_URL).equals(
                "null"
            ) && !sessionManager?.getValue(SessionManager.PHOTO_URL).equals("") && !sessionManager?.getValue(
                SessionManager.FIRST_NAME
            ).equals("")
        ) {

            Log.e("imageurl", "" + sessionManager?.getValue(SessionManager.PHOTO_URL))


            GlideApp.with(this)
                .load(sessionManager?.getValue(SessionManager.PHOTO_URL))
                .into(imageViewNavDrawer)


            tvName.setText(sessionManager?.getValue(SessionManager.FIRST_NAME))

        } else {
            GlideApp.with(this)
                .load(R.drawable.ic_user)
                .into(imageViewNavDrawer)
            tvName.setText(sessionManager?.getValue(SessionManager.FIRST_NAME))

        }

    }


    // here we maintain team and player names
    private fun addProduct(categoryName: String, itemName: String, itemId: Int): Int {

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

        productList.add(detailInfo)
        headerInfo.playerName = productList

        // find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo)
        return groupPosition
    }


    private fun setInitialFragment() {
        tvTitle.setText("Home")
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.content_frame, HomeFragment()).commit()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
                    tvTitle.setText("Home")
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, HomeFragment())
                        .commit()

                }
                R.id.nav_restaurants -> {
                    tvAdd.visibility = (View.INVISIBLE)
                    tvTitle.setText("Restaurants")
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, RestaurantsFragment())
                        .commit()
                }
                R.id.nav_my_friends -> {
                    tvAdd.visibility = (View.VISIBLE)
                    tvTitle.setText("My Friends")
                    imgFilters.visibility = (View.INVISIBLE)

                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(
                            R.id.content_frame,
                            MyFriendsFragment()
                        )
                        .commit()
                }
                R.id.nav_favorite_menu -> {
                    tvAdd.visibility = (View.INVISIBLE)
                    imgFilters.visibility = (View.INVISIBLE)
                    tvTitle.setText("My Favourites")
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, MyFavouriteFragment())
                        .commit()

                }
                R.id.nav_todo -> {
                    tvAdd.visibility = (View.INVISIBLE)
                    imgFilters.visibility = (View.INVISIBLE)
                    tvTitle.setText("To do")
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, TodoFragment())
                        .commit()
                }
                R.id.nav_notifications -> {


                }
                R.id.nav_settings -> {

                    tvAdd.visibility = (View.VISIBLE)
                    tvAdd.setText("Save")

                    imgFilters.visibility = (View.INVISIBLE)
                    tvTitle.setText("Settings")

                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, SettingsFragment())
                        .commit()
                }
                R.id.nav_logout -> {

                    TwitterCore.getInstance().getSessionManager().clearActiveSession()
                    Log.e("Twitter", "logout")

                    //clearing the values of session
                    sessionManager?.clearValues();

                    val intent = Intent(this@HomeActivity, WalkThroughActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
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
                    Log.e("Fragment", "SettingsFragment onActivityresult called")
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


                Log.e("rspFilter", response.toString())

                Log.e("rspFilterStat", response.status.toString())

                if (response.status == 1) {


                    var arrySize = filterArrayList.size

                    if (deptList.size == 0) {


                        for (items in response.data.budget) {
                            Log.e("Budget", items.name)
                            addProduct("Budget", items.name, items.id)
                        }
                        for (items in response.data.service_speed) {
                            addProduct("Service Speed", items.name, items.id)
                        }
                        for (items in response.data.meal_types) {
                            addProduct("Meal type", items.name, items.id)
                        }
                        for (items in response.data.intolerance_compatibilities) {
                            addProduct("Compatibility intolerance", items.name, items.id)
                        }
                        for (items in response.data.speciality) {
                            addProduct("Restaurant speciality", items.name, items.id)
                        }
                        for (items in response.data.ambiance) {
                            addProduct("Restaurant ambiance", items.name, items.id)
                        }
                        for (items in response.data.ambiance_complementary) {
                            addProduct("Complementary ambiance", items.name, items.id)
                        }
                        for (items in response.data.additional_services) {
                            addProduct("Additional Services", items.name, items.id)
                        }
                        for (items in response.data.seasons) {
                            addProduct("Seasonality", items.name, items.id)
                        }


                        // create the adapter by passing your ArrayList data
                        listAdapter = FilterAdapter(this, deptList)
                        // attach the adapter to the expandable list view
                        simpleExpandableListView!!.setAdapter(listAdapter)
                    }


                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

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

        /*   Log.e("TwitterClient1", "" + client.toString())
           client = TwitterAuthClient()*/
    }

    override fun onResume() {
        super.onResume()
        var photoUrl = sessionManager?.getValue(SessionManager.PHOTO_URL);
        if (photoUrl != null && photoUrl != "null" && photoUrl != "") {

            GlideApp.with(this)
                .load(photoUrl)
                .into(imageViewNavDrawer)
        }

    }

    /*      // load some initial data into out list
      private fun loadData() {

          addProduct("Budget", "Less than 5")
          addProduct("Budget", "5 to 10")
          addProduct("Budget", "10 to 15")
          addProduct("Budget", "15 to 25")

          addProduct("Service Speed", "Fast (less 30 min)")
          addProduct("Service Speed", "Classic (less 1 hour")
          addProduct("Service Speed", "Gastronomic (more 1 hour")

          addProduct("Meal Type", "Main Dish")
          addProduct("Meal Type", "Side dish")
          addProduct("Meal Type", "Desert")
          addProduct("Meal Type", "Chinesse")
          addProduct("Meal Type", "Pizza")
          addProduct("Meal Type", "Burger")
          addProduct("Meal Type", "Pastry")
          addProduct("Meal Type", "Ice cream")
          addProduct("Meal Type", "Drink")
          addProduct("Meal Type", "Confectionary")


          addProduct("Compatibility Intolerance", "wwww.Seekdish.com")
          addProduct("Restaurant Speciality", "wwww.Seekdish.com")
          addProduct("Restaurant Ambience", "wwww.Seekdish.com")
          addProduct("Complementary Ambience", "wwww.Seekdish.com")
          addProduct("Additional Services", "wwww.Seekdish.com")
          addProduct("Seasonality", "wwww.Seekdish.com")


      }
*/


}

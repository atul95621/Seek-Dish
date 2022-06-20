package com.dish.seekdish.ui.navDrawer.dishDescription

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.custom.PagerContainer
import com.dish.seekdish.ui.navDrawer.dishDescription.VM.DishDescriptionVM
import com.dish.seekdish.ui.navDrawer.dishDescription.adapter.DishDescpAdapter
import com.dish.seekdish.ui.navDrawer.dishDescription.model.Ingredients
import com.dish.seekdish.ui.navDrawer.dishDescription.model.Meals
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescrpActivity
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.facebook.share.widget.ShareDialog.canShow
import com.google.android.material.tabs.TabLayout
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_dish_description.*
import java.io.Serializable
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet


class DishDescriptionActivity : BaseActivity(), Serializable {
    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: DishDescpAdapter

    // for images
    internal lateinit var mContainer: PagerContainer
    internal lateinit var pager: ViewPager
    internal lateinit var adapterPager: PagerAdapter

    var dishDescriptionVM: DishDescriptionVM? = null
    var sessionManager: SessionManager? = null

    var meal_id: String? = null
    var restro_id: String? = null

    var latitude: String? = null
    var longitude: String? = null
    var dishInfoDetails: Meals? = null
    var ingredientSearilize: Ingredients? = null
    private var twitterAuthClient: TwitterAuthClient? = null
    var facebookLink: String = ""
    var twitterLink: String = ""

//    val mResources: ArrayList<String> = arrayListOf<String>()

    val mResources: HashSet<String> = HashSet<String>()

    internal lateinit var callbackManager: CallbackManager

    lateinit var shareDialog: ShareDialog
    lateinit var actionDialog: Dialog
    var imageUrl: String = ""
    var phoneNumber = ""

    //   lateinit var messageDialog :Dialog
    val PERMISSION_REQUEST_IMG_CODE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_description)
        mResources.clear()

        sessionManager = SessionManager(this)
        dishDescriptionVM = ViewModelProvider(this).get(DishDescriptionVM::class.java)

        getIntents()


        if (meal_id != null && restro_id != null) {
            //check connection
            if (connectionDetector.isConnectingToInternet) {
                //hitting api
                getMealDetials(meal_id.toString(), restro_id.toString())
                Log.e("infomeal", "  " + meal_id + "  " + restro_id)
            } else {
                showSnackBar(getString(R.string.check_connection))
            }
        }

        getDishDetailsObserver()
        getAddTODOObserver()
        getAddFavoriteObserver()


        getCallCountCount()
        clickListner()



        mContainer = findViewById(R.id.pager_container) as PagerContainer
// setting up tabLayout
        this.tabLayout = findViewById(R.id.tabLayoutDishActivity)

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ingre)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.opini)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.simi)))

//        //change font
//        changeTabsFont();


        viewPager = findViewById(R.id.viewPagerDishActivity) as ViewPager
//        viewPager.setOffscreenPageLimit(3);

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

    }

    override fun onResume() {
        super.onResume()


    }

    @SuppressLint("MissingPermission")
    private fun clickListner() {


        imgGoogleApp.setOnClickListener()
        {

            if (latitude != null && longitude != null) {
                val intent = Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse(
                        "http://maps.google.com/maps?saddr=" + "&daddr=" + latitude + "," + longitude + "(Event Location)"
                    )
                )
                startActivity(intent)
            }
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

            val intent = Intent(this@DishDescriptionActivity, MealRatingActivity::class.java)
            intent.putExtra("MEALID", meal_id)
            intent.putExtra("RESTROID", restro_id)
            intent.putExtra("IMAGE", imageUrl)
            intent.putExtra("FROM_SCREEN", "Dish_Description_Activity")

            startActivity(intent)
        }

        /*imgInvitation.setOnClickListener()
        {
            val intent = Intent(this@DishDescriptionActivity, InvitationActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restro_id.toString())
            intent.putExtra("FROM", "DishDescriptionActivity")

            startActivity(intent)
        }*/


        imgCallRestro.setOnClickListener()
        {
            if (checkImgPermissionIsEnabledOrNot()) {
                if (!meal_id.isNullOrEmpty()) {
                    hitCallCountApi()
                    callTheRestaurant();
                }
            } else {
                requestImagePermission()
            }
        }


        tvRestaurantName.setOnClickListener()
        {
            if (connectionDetector.isConnectingToInternet) {
                val intent = Intent(this, RestroDescrpActivity::class.java)
                intent.putExtra("RESTAURANT_ID", restro_id.toString())
                startActivity(intent)
            } else {
                showSnackBar(getString(R.string.check_connection))
            }
        }
        tvMealName.setOnClickListener()
        {
            if (connectionDetector.isConnectingToInternet) {
                val intent = Intent(this, RestroDescrpActivity::class.java)
                intent.putExtra("RESTAURANT_ID", restro_id.toString())
                startActivity(intent)
            } else {
                showSnackBar(getString(R.string.check_connection))
            }
        }
    }

    private fun hitCallCountApi() {
        val currentDate: String =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        dishDescriptionVM?.postCallCount(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            restro_id.toString(),
            currentDate
        )
    }

    @SuppressLint("MissingPermission")
    private fun callTheRestaurant() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + phoneNumber)
        startActivity(callIntent)
    }

    public fun getIntents() {
        meal_id = intent.getStringExtra("MEAL_ID")
        restro_id = intent.getStringExtra("RESTAURANT_ID")
        var refersh = intent.getStringExtra("REFRESH_ACTIVITY")
    }

    fun getMealDetials(mealId: String, restroId: String) {
        dishDescriptionVM?.doGetMealDetails(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            mealId,
            restroId,
            sessionManager?.getValue(SessionManager.LONGITUDE).toString(),
            sessionManager?.getValue(SessionManager.LATITUDE).toString()
        )

        Log.e(
            "informa22", "  " + sessionManager?.getValue(SessionManager.USER_ID).toString() + "  " +
                    mealId + " " +
                    restroId + " " +
                    sessionManager?.getValue(SessionManager.LONGITUDE).toString() + " " +
                    sessionManager?.getValue(SessionManager.LATITUDE).toString()
        )
    }

    fun getDishDetailsObserver() {
        //observe
        dishDescriptionVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }


        dishDescriptionVM!!.getDishDetailLiveData.observe(this, Observer { response ->
            if (response != null) {
                try {
                    if (response.status == 1) {

                        tvMealName.setText(response.data.meals.meal_name)
                        tvRestaurantName.text = response.data.meals.restro_name ?: ""
                        tvRestroAddrss.setText(response.data.meals.street + ", " + response.data.meals.city + ", " + response.data.meals.zipcode)
                        tvSimpleRatingBar.text =
                            response.data.meals.meal_avg_rating ?: ""
//                        ratingEuroMeal.rating = response.data.meals.budget?.toFloat() ?: 0.0F

                        tvPrice.setText(
                            response.data.meals.meal_symbol + " " + response.data.meals.meal_price
                        )

                        // feeding the image to the list
                        var imageMeal = response.data.meals.meal_image
                        imageUrl = imageMeal
                        mResources.add(imageMeal)

                        latitude = response.data.meals.latitude
                        longitude = response.data.meals.longitude

                        // passing the varibale to DishDeatailActivity with SEARIALIZABLE...
                        dishInfoDetails = response.data.meals
                        ingredientSearilize = response.data.Ingredients
                        // setting up the model classs for dish onClick info...
                        Log.e("ResourceSizePrev", "" + mResources.size)
                        facebookLink = response.data.meals.facebook
                        twitterLink = response.data.meals.twitter

                        phoneNumber = response.data.meals.phone

                        //for swipe images on top
                        initializeviews()

                        /*    tvRestaurantName.setOnClickListener()
                    {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/#q=" + tvRestaurantName.getText())
                            )
                        );
                    }*/

                        //++++++++++++++++++++++++ setting the adapter after the responses come in...
                        adapter =
                            DishDescpAdapter(
                                this.supportFragmentManager,
                                tabLayout.tabCount,
                                response
                            )
                        viewPager.adapter = adapter
                        viewPager.addOnPageChangeListener(
                            TabLayout.TabLayoutOnPageChangeListener(
                                tabLayout
                            )
                        )
                    } else {
                        showSnackBar(response.message)
                    }
                } catch (e: Exception) {
                    showSnackBar(e.message.toString())
                }
            } else {
                showSnackBar(resources.getString(R.string.error_occured) + "  $response")
            }
        })
    }


    fun getAddTODOObserver() {

        //observe
        dishDescriptionVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        dishDescriptionVM!!.getAddTodoLiveData.observe(this, Observer { response ->
            if (response != null) {

                if (response.status == 1) {
//                    onSendClick(response.data.message)
                    actionDialog.dismiss()
                    showSnackBar(response.message)
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(getResources().getString(R.string.error_occured) + "     $response ");
//                showSnackBar("OOps! Error Occured.")
            }
        })
    }


    fun getAddFavoriteObserver() {

        //observe
        dishDescriptionVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        dishDescriptionVM!!.getAddFavouriteLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
//                    onSendClick(response.data.message)
                    actionDialog.dismiss()
                    showSnackBar(response.message)
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(getResources().getString(R.string.error_occured) + "   $response ");
            }
        })
    }


    fun getCallCountCount() {
        //observe
        dishDescriptionVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        dishDescriptionVM!!.getCallCountLiveData.observe(this, Observer { response ->
            if (response != null) {
                if (response.status == 1) {
//                    showSnackBar(response.message)
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(getResources().getString(R.string.error_occured) + "   $response ");
            }
        })
    }

    private fun onShare() {
        actionDialog = Dialog(this)
        actionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        actionDialog.setCancelable(true)
        actionDialog.setContentView(R.layout.dialog_share_dish)

        val tvShare = actionDialog.findViewById<TextView>(R.id.tvShare)
        val btnCancel = actionDialog.findViewById<Button>(R.id.btnCancel)
        val tvAddtodo = actionDialog.findViewById<TextView>(R.id.tvAddtodo)
        val tvAddfav = actionDialog.findViewById<TextView>(R.id.tvAddfav)
        val tvViewDetail = actionDialog.findViewById<TextView>(R.id.tvViewDetail)
        val tvShowMap = actionDialog.findViewById<TextView>(R.id.tvShowMap)

        tvShowMap.setOnClickListener {
            if (latitude != null && longitude != null) {

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "http://maps.google.com/maps?saddr=" + "&daddr=" + latitude + "," + longitude + "(Event Location)"
                    )
                )
                startActivity(intent)
                actionDialog.dismiss()

            }
        }
        // button_yes clk
        btnCancel.setOnClickListener {
            actionDialog.dismiss()
        }
        tvShare.setOnClickListener()
        {
            //check connection
            if (connectionDetector.isConnectingToInternet) {
                //hitting api
                onShareSocial()
                actionDialog.dismiss()
            } else {
                showSnackBar(getString(R.string.check_connection))
            }

        }

        tvAddfav.setOnClickListener()
        {
            if (connectionDetector.isConnectingToInternet) {
                dishDescriptionVM?.getAddFavouritestat(
                    sessionManager?.getValue(SessionManager.USER_ID).toString(),
                    meal_id.toString(),
                    restro_id.toString()
                )
            } else {
                showSnackBar(getString(R.string.check_connection))
            }
        }

        tvViewDetail.setOnClickListener()
        {
            if (connectionDetector.isConnectingToInternet) {
                val intent = Intent(this, RestroDescrpActivity::class.java)
                intent.putExtra("RESTAURANT_ID", restro_id.toString())
                startActivity(intent)
                actionDialog.dismiss()
            } else {
                showSnackBar(getString(R.string.check_connection))
            }
        }

        tvAddtodo.setOnClickListener()
        {
            if (connectionDetector.isConnectingToInternet) {
                dishDescriptionVM?.getAddTodostat(
                    sessionManager?.getValue(SessionManager.USER_ID).toString(),
                    meal_id.toString(),
                    restro_id.toString()
                )
            } else {
                showSnackBar(getString(R.string.check_connection))
            }

        }
        actionDialog.show()
    }


    private fun onShareSocial() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_social_share)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))   // making backgrnd color tarnsparent code begind progress circle bar
        dialog.window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )

        val ic_facebk = dialog.findViewById<ImageView>(R.id.ic_facebk)
        val imgTwitter = dialog.findViewById<ImageView>(R.id.imgTwitter)

        ic_facebk.setOnClickListener()
        {
            if (facebookLink.isEmpty()) {
                showSnackBar(resources.getString(R.string.not_found))
            } else {
                sharePostOnFacebook()
            }
            dialog.dismiss()
        }

        imgTwitter.setOnClickListener()
        {
            if (facebookLink.isEmpty()) {
                showSnackBar(resources.getString(R.string.not_found))
            } else {
                shareProductOnTwitter()
            }
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun initializeviews() {

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
//        springDotsIndicator.setViewPager(pager)
        springDotsIndicator.visibility = View.GONE
    }

//Nothing special about this adapterPager, just throwing up colored views for demo

    private inner class MyPageradapterPager(internal var mContext: Context) : PagerAdapter() {
        override fun getCount(): Int {
            return mResources.size
        }

        internal var mLayoutInflater: LayoutInflater

        init {
            mLayoutInflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as LinearLayout
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false)

            val imageView = itemView.findViewById(R.id.imageView) as ImageView
//            val profile_image = itemView.findViewById(R.id.profile_image) as CircleImageView

            imgRestroLogo.setOnClickListener()
            {
                val intent = Intent(this@DishDescriptionActivity, DishDetailActivity::class.java)
                intent.putExtra("MEAL_SEARIALIZE", dishInfoDetails as Serializable)
                intent.putExtra("INGREDIENT_SEARIALIZE", ingredientSearilize as Serializable)
                startActivity(intent)
            }


            GlideApp.with(this@DishDescriptionActivity)
                .load(mResources.elementAt(position))
                .placeholder(R.drawable.app_logo)
                .into(imageView)
            GlideApp.with(this@DishDescriptionActivity)
                .load(mResources.elementAt(position))
                .placeholder(R.drawable.app_logo)
                .into(imgRestroLogo)

//            imageView.setImageBitmap(getBitmapFromUrl(mResources[position]))
//            profile_image.setImageBitmap(getBitmapFromUrl(mResources[position]))

            //            imageView.setImageResource(mResources[position].toInt())
//            profile_image.setImageResource(mResources[position].toInt())
            container.addView(itemView)

            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as LinearLayout)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            if (twitterAuthClient != null) {
//                Log.e("TAG", "Twitter post Sharing called")
                twitterAuthClient?.onActivityResult(requestCode, resultCode, data)
            }
        } else {
//            Log.e("TAG", "Facebook post Sharing called")
            // Use Facebook callback manager here
            callbackManager.onActivityResult(requestCode, resultCode, data)

        }


    }

    private fun sharePostOnFacebook() {
        // facebook login code
        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)
        shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result) {
//                Log.e("TAG", "Facebook Share Success")
            }

            override fun onCancel() {
//                Log.e("TAG", "Facebook Sharing Cancelled by User")
            }

            override fun onError(error: FacebookException) {
//                Log.e("TAG", "Facebook Share Success: Error: " + error.getLocalizedMessage())
            }
        })

//        var oldIosUrl = "https://seekdish.com/restaurant/get/meals/details/182?language_id=fr";
        if (canShow(ShareLinkContent::class.java)) {
            val linkContent = ShareLinkContent.Builder()
                .setQuote(resources.getString(R.string.seekdish))
                .setContentUrl(Uri.parse(facebookLink))
                .build()
            shareDialog.show(linkContent)
        }
    }

    fun shareProductOnTwitter() {
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

//        Log.e("TwitterClient1", "" + twitterAuthClient.toString())
        twitterAuthClient = TwitterAuthClient()
        val twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession()
        if (twitterSession == null) {
            twitterAuthClient?.authorize(this, object : Callback<TwitterSession>() {
                override fun success(result: com.twitter.sdk.android.core.Result<TwitterSession>?) {
                    val twitterSession = result?.data
                    shareOnTwitter()
                }

                override fun failure(e: TwitterException) {
//                    Log.e("TAG", "Twitter Error while authorize user " + e.message)
                }
            })
        } else {
            shareOnTwitter()
        }
    }

    private fun shareOnTwitter() {
        var urlTwi =
            "http://twitter.com/share?text=Im Sharing on Twitter&url=https://stackoverflow.com/users/2943186/youssef-subehi&hashtags=stackoverflow,example,youssefusf";
        val tweetUrl = ("https://twitter.com/share?text=SeekDish&url=" + twitterLink)
        val uri = Uri.parse(tweetUrl)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("methodIn", "onNewIntentCalled")

        if (meal_id != null && restro_id != null) {
            getMealDetials(meal_id.toString(), restro_id.toString())

        }
    }

    //calling
    private fun requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CALL_PHONE

                ), PERMISSION_REQUEST_IMG_CODE
            )
        }
    }

    //CALLING
    private fun checkImgPermissionIsEnabledOrNot(): Boolean {
        val FirstPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 2) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callTheRestaurant()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }
}



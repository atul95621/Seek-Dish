package com.dish.seekdish.ui.navDrawer.dishDescription

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.custom.PagerContainer
import com.dish.seekdish.ui.navDrawer.dishDescription.VM.DishDescriptionVM
import com.dish.seekdish.ui.navDrawer.dishDescription.adapter.DishDescpAdapter
import com.dish.seekdish.ui.navDrawer.dishDescription.model.Ingredients
import com.dish.seekdish.ui.navDrawer.dishDescription.model.Meals
import com.dish.seekdish.ui.navDrawer.invitation.InvitationActivity
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
//   lateinit var messageDialog :Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_description)

        Log.e("methodIn", "onCreate")
        mResources.clear()

        sessionManager = SessionManager(this)
        dishDescriptionVM = ViewModelProviders.of(this).get(DishDescriptionVM::class.java)

        getIntents()




        if (meal_id != null && restro_id != null) {

            //check connection
            if (connectionDetector.isConnectingToInternet) {
                //hitting api
                getMealDetials(meal_id.toString(), restro_id.toString())
            } else {
                showSnackBar(getString(R.string.check_connection))
            }

        }

        getDishDetailsObserver()
        getAddTODOObserver()
        getAddFavoriteObserver()
        clickListner()


        // setting up tabLayout
        this.tabLayout = findViewById(R.id.tabLayoutDishActivity)
        mContainer = findViewById(R.id.pager_container) as PagerContainer


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

        imgInvitation.setOnClickListener()
        {
            val intent = Intent(this@DishDescriptionActivity, InvitationActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restro_id.toString())
            intent.putExtra("FROM", "DishDescriptionActivity")

            startActivity(intent)
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
    }

    fun getDishDetailsObserver() {
        //observe
        dishDescriptionVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setIsLoading(it)
            }

        dishDescriptionVM!!.getDishDetailLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspGetDishDetails", response.status.toString())

                if (response.status == 1) {

                    tvMealName.setText(response.data.meals.meal_name)
                    tvRestaurantName.setText(response.data.meals.restro_name + ", " + response.data.meals.street + ", " + response.data.meals.city + ", " + response.data.meals.zipcode)
                    ratingStarMeal.rating = response.data.meals.meal_avg_rating.toFloat()
                    ratingEuroMeal.rating = response.data.meals.budget.toFloat()

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
                        DishDescpAdapter(this.supportFragmentManager, tabLayout.tabCount, response)
                    viewPager.adapter = adapter
                    viewPager.addOnPageChangeListener(
                        TabLayout.TabLayoutOnPageChangeListener(
                            tabLayout
                        )
                    )
                } else {
                    showSnackBar(response.message)
                }

            } else {
                showSnackBar(resources.getString(R.string.error_occured) + "  $response")
                Log.e("rspGetDishDetailsFail", "else error")

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

                Log.e("rspGetaddtodoDetails", response.toString())

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
                Log.e("rspGetaddtodoFail", "else error")
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
                Log.e("rspGetaddtodoDetailsd", response.toString())
                if (response.status == 1) {
//                    onSendClick(response.data.message)
                    actionDialog.dismiss()
                    showSnackBar(response.message)
                } else {
                    showSnackBar(response.message)
                }
            } else {
                showSnackBar(getResources().getString(R.string.error_occured) + "   $response ");
                Log.e("rspGetaddtodoFail", "else error")
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
        dialog.setCancelable(false)
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
            val profile_image = itemView.findViewById(R.id.profile_image) as CircleImageView

            profile_image.setOnClickListener()
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
                .into(profile_image)

            Log.e("ResourceSize", "" + mResources.size)


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
                Log.e("TAG", "Twitter post Sharing called")
                twitterAuthClient?.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            Log.e("TAG", "Facebook post Sharing called")
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
                Log.e("TAG", "Facebook Share Success")
            }

            override fun onCancel() {
                Log.e("TAG", "Facebook Sharing Cancelled by User")
            }

            override fun onError(error: FacebookException) {
                Log.e("TAG", "Facebook Share Success: Error: " + error.getLocalizedMessage())
            }
        })
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

        Log.e("TwitterClient1", "" + twitterAuthClient.toString())
        twitterAuthClient = TwitterAuthClient()
        val twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession()
        if (twitterSession == null) {
            twitterAuthClient?.authorize(this, object : Callback<TwitterSession>() {
                override fun success(result: com.twitter.sdk.android.core.Result<TwitterSession>?) {
                    val twitterSession = result?.data
                    shareOnTwitter()
                }

                override fun failure(e: TwitterException) {
                    Log.e("TAG", "Twitter Error while authorize user " + e.message)
                }
            })
        } else {
            shareOnTwitter()
        }
    }

    private fun shareOnTwitter() {
        val tweetUrl = ("https://twitter.com/intent/tweet?text=SeekDish&url=" + twitterLink)
        val uri = Uri.parse(tweetUrl)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
   /*     val statusesService = TwitterCore.getInstance().getApiClient().getStatusesService()
        val tweetCall =
            statusesService.update("Seekdish:  ", null, false, null, null, null, false, false, null)
        tweetCall.enqueue(object : Callback<Tweet>() {
            override fun success(result: com.twitter.sdk.android.core.Result<Tweet>?) {
                Log.e("TAG", "Twitter Share Success")
//                logoutTwitter()            }
            }

            override fun failure(exception: TwitterException) {
                Log.e("TAG", "Twitter Share Failed with Error: " + exception.getLocalizedMessage())
            }
        })*/
    }


/*    private fun onSendClick(message: String) {
       messageDialog = Dialog(this)
        messageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        messageDialog.setCancelable(false)
        messageDialog.setContentView(R.layout.dialog_email_verify)

        val textViewDescrp = messageDialog.findViewById<TextView>(R.id.textViewDescrp)
        val btnAccept = messageDialog.findViewById<Button>(R.id.btnAccept)
        textViewDescrp.setText(message)
        // button_yes clk
        btnAccept.setOnClickListener {
            messageDialog.dismiss()
        }

        messageDialog.show()

    }*/

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("methodIn", "onNewIntentCalled")

        if (meal_id != null && restro_id != null) {
            getMealDetials(meal_id.toString(), restro_id.toString())
            Log.e("methodIn", "hit again")

        }
    }
}



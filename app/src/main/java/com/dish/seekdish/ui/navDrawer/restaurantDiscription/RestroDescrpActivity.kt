package com.dish.seekdish.ui.navDrawer.restaurantDiscription

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
import com.dish.seekdish.custom.PagerContainer
import com.dish.seekdish.ui.navDrawer.dishDescription.MealRatingActivity
import com.dish.seekdish.ui.navDrawer.invitation.InvitationActivity
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.VM.RestroDescpVM
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.adapter.RestroDescrpAdapter
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.material.tabs.TabLayout
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.Tweet
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_restro_description.*
import kotlinx.android.synthetic.main.activity_restro_description.tvBack
import java.util.ArrayList

class RestroDescrpActivity : BaseActivity() {
    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: RestroDescrpAdapter


    // for images
    internal lateinit var mContainer: PagerContainer
    internal lateinit var pager: ViewPager
    internal lateinit var adapterPager: PagerAdapter

    val mResources: ArrayList<String> = arrayListOf<String>()

    var restroDescpVM: RestroDescpVM? = null
    var sessionManager: SessionManager? = null
    var restro_id: String? = null
    var latitude: String? = null
    var longitude: String? = null

    private var twitterAuthClient: TwitterAuthClient? = null

    internal lateinit var callbackManager: CallbackManager

    lateinit var shareDialog: ShareDialog
    lateinit var actionDialog: Dialog
    var imageUrl: String = ""

    /*  internal var mResources = intArrayOf(
          R.drawable.ic_foodex,
          R.drawable.ic_pasta,
          R.drawable.ic_foodimg,
          R.drawable.ic_foodex,
          R.drawable.ic_pasta,
          R.drawable.ic_foodimg
      )*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restro_description)

        Log.e("methodIn", "onCreate")
        mResources.clear()

        sessionManager = SessionManager(this)
        restroDescpVM = ViewModelProviders.of(this).get(RestroDescpVM::class.java)


        getIntents()

        if (restro_id != null) {
            getMealDetials(restro_id.toString())

        }
        getDishDetailsObserver()

        clickListner()


        // setting up tabLayout
        this.tabLayout = findViewById(R.id.tabLayoutRestroActivity)

        tabLayout.addTab(tabLayout.newTab().setText("Meals"))
        tabLayout.addTab(tabLayout.newTab().setText("Similar"))
        tabLayout.addTab(tabLayout.newTab().setText("Details"))

//        //change font
//        changeTabsFont();

        viewPager = findViewById(R.id.viewPagerRestroActivity) as ViewPager
        /*  adapter = RestroDescrpAdapter(this.supportFragmentManager, tabLayout.tabCount, response)
          viewPager.adapter = adapter
  */


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

    private fun clickListner() {
        imgGoogleApp.setOnClickListener()
        {
            val intent = Intent(
                android.content.Intent.ACTION_VIEW,
                Uri.parse(
                    "http://maps.google.com/maps?saddr=" + "&daddr=" + latitude + "," + longitude + "(Event Location)"
                )
            )
            startActivity(intent)
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
            val intent = Intent(this@RestroDescrpActivity, MealRatingActivity::class.java)
            startActivity(intent)
        }

        imgInvitation.setOnClickListener()
        {
            val intent = Intent(this@RestroDescrpActivity, InvitationActivity::class.java)
            startActivity(intent)
        }

    }

    fun getMealDetials(restroId: String) {
        restroDescpVM?.doGetRestroDetails(
            sessionManager?.getValue(SessionManager.USER_ID).toString(),
            restroId
        )
    }

    private fun getIntents() {
        restro_id = intent.getStringExtra("RESTAURANT_ID")
        Log.e("resid",restro_id)
    }

    private fun onShare() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_share_restro)

        val tvShare = dialog.findViewById<TextView>(R.id.tvShare)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
//        val tvAlert = dialog.findViewById<TextView>(R.id.tvAlert)
        val tvShowMap = dialog.findViewById<TextView>(R.id.tvShowMap)


        tvShowMap.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "http://maps.google.com/maps?saddr=" + "&daddr=" + 27.480253 + "," + 74.847381 + "(Event Location)"
                )
            )
            startActivity(intent)
        }


        // button_yes clk
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvShare.setOnClickListener()
        {
            onShareSocial()
            dialog.dismiss()
        }

        dialog.show()

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
            sharePostOnFacebook()
            dialog.dismiss()
        }

        imgTwitter.setOnClickListener()
        {
            shareProductOnTwitter()
            dialog.dismiss()
        }

        dialog.show()

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


        //setting dots with viewpager...
        springDotsIndicator.setViewPager(pager)


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

            /*val imageView = itemView.findViewById(R.id.imageView) as ImageView
            val profile_image = itemView.findViewById(R.id.profile_image) as CircleImageView*/

            /*      imageView.setImageResource(mResources[position])
                  profile_image.setImageResource(mResources[position])*/
            container.addView(itemView)

            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as LinearLayout)
        }
    }

    fun getDishDetailsObserver() {

        //observe
        restroDescpVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        restroDescpVM!!.geRestroDetailLiveData.observe(this, Observer { response ->
            if (response != null) {

                Log.e("rspRestro", response.toString())

                if (response.status == 1) {

                    // feeding the image to the list
                    var imageMeal = response.data.restaurant.restaurant_image
                    mResources.add(imageMeal)

                    Log.e("restro_image",""+imageMeal)


                    tvRestroName.setText(response.data.restaurant.name)
                    tvRestroAddress.setText(response.data.restaurant.street)
                    tvRestroReview.setText("(" + response.data.restaurant.no_of_reviews + ")")
                    latitude = response.data.restaurant.latitude
                    longitude = response.data.restaurant.longitude
                    ratingRestro.rating = response.data.restaurant.rating.toFloat()

                    //for swipe images on top
                    initializeviews()


                    //++++++++++++++++++++++++ setting the adapter after the responses come in...
                    adapter = RestroDescrpAdapter(
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
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspGetDishDetailsFail", "else error")

            }
        })
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
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val linkContent = ShareLinkContent.Builder()
                .setQuote("Seekdish")
                .setContentUrl(Uri.parse("https://www.youtube.com/watch?v=K5KAc5CoCuk"))
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
        val statusesService = TwitterCore.getInstance().getApiClient().getStatusesService()
        val tweetCall = statusesService.update("Seekdish:  ", null, false, null, null, null, false, false, null)
        tweetCall.enqueue(object : Callback<Tweet>() {
            override fun success(result: com.twitter.sdk.android.core.Result<Tweet>?) {
                Log.e("TAG", "Twitter Share Success")
//                logoutTwitter()            }
            }

            override fun failure(exception: TwitterException) {
                Log.e("TAG", "Twitter Share Failed with Error: " + exception.getLocalizedMessage())
            }
        })
    }
}


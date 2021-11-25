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
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.pm.PackageInfo
import android.net.Uri
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dish.seekdish.ui.login.CheckUpdateModel
import com.dish.seekdish.ui.login.LoginPresenter
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import com.dish.seekdish.walkthrough.presenter.RegisterFragPresenter
import com.dish.seekdish.walkthrough.presenter.WalkThroughPresenter
import com.dish.seekdish.walkthrough.view.IWalkThrough
import retrofit2.Response
import java.lang.Exception


class WalkThroughActivity : BaseActivity(), IWalkThrough {

    companion object {
        private const val MIN_SCALE = 0.65f
        private const val MIN_ALPHA = 0.3f
    }

    private lateinit var pagerAdapterView: MyPagerAdapter
    private val uiHelper = UiHelper()
    var sessionManager: SessionManager? = null;
    lateinit var walkThroughPresenter: WalkThroughPresenter
    var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_walkthrough)
        sessionManager = SessionManager(this)
        walkThroughPresenter = WalkThroughPresenter(this, this)
        dialog = Dialog(this)
        //setting multi-language code
        var langCode = sessionManager?.getLangValue(SessionManager.LANGUAGE_CODE) ?: "en"
        setLocale(langCode)


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
//                    Log.e("Twitter", "Acitivty onActivityresult called")
                    f.onActivityResult(requestCode, resultCode, data)
                } /*else if (f is CreateEventFragment) {
                      f.onActivityResult(requestCode, resultCode, data)
                  }*/
            }
        }

    }

    override fun checkUpdate(result: Boolean, response: Response<CheckUpdateModel>) {
        if (result == true) {
            val checkUpdateModel = response.body() as CheckUpdateModel
            if (checkUpdateModel.status == 1) {
                Log.e("up22", "resp :  ${response.body().toString()}")
                var version = ""
                try {
                    val pInfo: PackageInfo =
                        this.getPackageManager().getPackageInfo(packageName, 0)
                    version = pInfo.versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }

                if (!version.isNullOrEmpty() && checkUpdateModel.force_update_android != 0) {
                    if (version.toFloat() < checkUpdateModel.Android_version.toFloat()) {
                        showUpdateDialog(getString(R.string.update))
                    } else {
                        if (dialog?.isShowing == true) {
                            dialog?.dismiss()
                        }
                    }
                }
            } else {
                showSnackBar(checkUpdateModel.message)
            }

        } else {
            showSnackBar(resources.getString(R.string.error_occured) + "    ${response.code()}")
        }
    }

    private fun checkIfUpdateAvailable() {
        walkThroughPresenter?.checkUpdate()
    }

    override fun onResume() {
        super.onResume()
        // checking if new version available
        checkIfUpdateAvailable()
    }

    private fun showUpdateDialog(message: String) {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_email_verify)

        val textViewDescrp = dialog?.findViewById<TextView>(R.id.textViewDescrp)
        val btnAccept = dialog?.findViewById<Button>(R.id.btnAccept)
//        textViewDescrp.setText(message)
        textViewDescrp?.setText(message)
        // button_yes clk
        btnAccept?.setText(getString(R.string.update_str))
        btnAccept?.setOnClickListener {
            Toast.makeText(this, getString(R.string.go_playstore), Toast.LENGTH_SHORT).show()
            /*     dialog.dismiss()
                 val appPackageName =
                     packageName // getPackageName() from Context or Activity object
                 try {


                     try {
                         startActivity(
                             Intent(
                                 Intent.ACTION_VIEW,
                                 Uri.parse("market://details?id=$appPackageName")
                             )
                         )
                     } catch (anfe: ActivityNotFoundException) {
                         startActivity(
                             Intent(
                                 Intent.ACTION_VIEW,
                                 Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                             )
                         )
                     }
                 } catch (e: Exception) {

                 }*/
        }
        dialog?.show()
    }


}

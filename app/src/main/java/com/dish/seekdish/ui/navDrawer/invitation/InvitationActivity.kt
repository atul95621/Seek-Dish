package com.dish.seekdish.ui.navDrawer.invitation

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.ui.navDrawer.invitation.settings.InvitationSettingsActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_invitation.*
import android.app.TimePickerDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.IncludeFriendsActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import com.dish.seekdish.ui.navDrawer.settings.myAlerts.InvitationModel
import com.dish.seekdish.util.Global
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_invitation.tvBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class InvitationActivity : BaseActivity() {


    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: InvitationAdapter
    lateinit var date: Calendar

    internal lateinit var apiInterface: APIInterface
    var sessionManager: SessionManager? = null
    var restro_id = ""
    var from = ""
    var userWhoSent = ""
    var validity = ""
    var allow = ""
    var timeDate = ""
    var timedelected = ""
    var dateSelected = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation)

        getIntents()
        // setting up tabLayout
        this.tabLayout = findViewById(R.id.tabLayoutInvitationActivity)

        tabLayout.addTab(tabLayout.newTab().setText("Invited"))
        tabLayout.addTab(tabLayout.newTab().setText("Map"))
        tabLayout.addTab(tabLayout.newTab().setText("Details"))

//        //change font
//        changeTabsFont();

        viewPager = findViewById(R.id.viewPagerInvitationActivity) as ViewPager



        sessionManager = SessionManager(this)

        if (connectionDetector.isConnectingToInternet) {
            if (from.equals("NotificationAdapter")) {
                //hitting api
                linOptionNotifi.visibility = View.VISIBLE
                linBottomOption.visibility = View.GONE

                var time = timePrase(timeDate)
                var date = datePrase(timeDate)

                tvDate.setText(date)
                tvTime.setText(time)

                tvDate.isEnabled=false
                tvTime.isEnabled=false

                getInvitationApiHit()
            } else {
                currentDateTime()

                linOptionNotifi.visibility = View.GONE
                linBottomOption.visibility = View.VISIBLE
                //hitting api
                getInvitationApiHit()
            }
        } else {
            showSnackBar(getString(R.string.check_connection))
        }

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

        tvBack.setOnClickListener()
        {
            finish()
        }

        imgDecline.setOnClickListener()
        {
            acceptOrDeclineInvitation("0")
        }
        imgAccept.setOnClickListener()
        {
            acceptOrDeclineInvitation("1")

        }

        tvSettings.setOnClickListener()
        {
            val intent = Intent(this@InvitationActivity, InvitationSettingsActivity::class.java)
            intent.putExtra("VALIDITY", validity)
            intent.putExtra("ALLOW_INVITATION", allow)

            startActivity(intent)

        }

        linDateTime.setOnClickListener()
        {
            showDateTimePicker()
        }

        imgInviationAdd.setOnClickListener()
        {

            val intent = Intent(this@InvitationActivity, IncludeFriendsActivity::class.java)
            startActivity(intent)

        }
        tvBack.setOnClickListener()
        {
            finish()
        }

        imgInvitaionSend.setOnClickListener()
        {
            var recievers_id = TextUtils.join(",", Global.selectedFriends)

            if (recievers_id != null && recievers_id != "null" && recievers_id != "") {
                postInvitation(recievers_id)
            } else {
                showSnackBar("Please choose friends first.")
            }
        }
    }

    private fun onInvitationSendClick(message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_email_verify)
        val textViewTitle = dialog.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescrp = dialog.findViewById<TextView>(R.id.textViewDescrp)
        val btnAccept = dialog.findViewById<Button>(R.id.btnAccept)
        Global.selectedFriends.clear()
        textViewTitle.setText("Invitation Status")
        textViewDescrp.setText(message)
        // button_yes clk
        btnAccept.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    fun showDateTimePicker() {
        val currentDate = Calendar.getInstance()
        date = Calendar.getInstance()

        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
//                date.getMinimum((System.currentTimeMillis() - 1000).toInt())
                TimePickerDialog(
                    this,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        timedelected = hourOfDay.toString() + ":" + minute
                        tvTime.setText(timedelected)
                        dateSelected =
                            dayOfMonth.toString() + "-" + monthOfYear.plus(1) + "-" + year.toString()
                        tvDate.setText(dateSelected)

                        Log.v("picktime", "The choosen one " + hourOfDay + minute)

                        Log.v("pickdate", "The choosen one " + date.getTime())
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    true
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        ).show()


    }

    fun currentDateTime() {
        val currentDate = Calendar.getInstance()

        var date: String =
            currentDate.get(Calendar.DATE).toString() + "/" + currentDate.get(Calendar.MONTH).plus(1) + "/" + currentDate.get(
                Calendar.YEAR
            )
        var time: String =
            currentDate.get(Calendar.HOUR_OF_DAY).toString() + ":" + currentDate.get(Calendar.MINUTE)
        Log.e("time", time)
        Log.e("date", date)

        tvDate.setText(date)
        tvTime.setText(time)
    }

    fun getInvitationApiHit() {
        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call =
            apiInterface.getInvitation(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                restro_id
            )
        call.enqueue(object : Callback<InvitationModel> {
            override fun onResponse(
                call: Call<InvitationModel>,
                response: Response<InvitationModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                Log.e("respStr", " " + response.body().toString())
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as InvitationModel

                    if (modelObj.status == 0) {

                        showSnackBar(resources.getString(R.string.error_occured));

                    } else {
                        var imageUrl: String = modelObj.data.restaurant_image
                        GlideApp.with(conxt)
                            .load(imageUrl)
                            .placeholder(R.drawable.app_logo)
                            .into(imgInvited)
                        tvRestroName.text = modelObj.data.name
                        tvAddress.text = modelObj.data.street
                        simpleRatingBar.rating = 4.0f
                        validity =
                            modelObj.data.setting_invitation.validity_of_invitation.toString()
                        allow = modelObj.data.setting_invitation.allow_invitation.toString()

                        adapter =
                            InvitationAdapter(supportFragmentManager, tabLayout.tabCount, modelObj)
                        viewPager.adapter = adapter
                        viewPager.addOnPageChangeListener(
                            TabLayout.TabLayoutOnPageChangeListener(
                                tabLayout
                            )
                        )
                        Log.e("heii", "enerd")
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured));
                }
            }

            override fun onFailure(call: Call<InvitationModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }

    fun postInvitation(recieversId: String) {
        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call =
            apiInterface.postInvitationApi(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                restro_id,
                recieversId,
                timedelected, dateSelected
            )
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(
                call: Call<CancelReModel>,
                response: Response<CancelReModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                Log.e("respStr", " " + response.body().toString())
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as CancelReModel
                    if (modelObj.status == 1) {
                        onInvitationSendClick("Your invitation sent successfully.")
                    } else {
                        showSnackBar(modelObj.data.message);
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured));
                }
            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured));
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
            }
        })
    }

    fun acceptOrDeclineInvitation(status: String) {
        ProgressBarClass.progressBarCalling(this)
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call =
            apiInterface.acceptDeclineInviApi(
                userWhoSent,
                restro_id,
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                status
            )

        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(
                call: Call<CancelReModel>,
                response: Response<CancelReModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                Log.e("respStrIniv", " " + response.body().toString())
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as CancelReModel
                    if (modelObj.status == 1) {
                        onInvitationSendClick(modelObj.data.message)
                        linBottomOption.visibility = View.VISIBLE
                        linOptionNotifi.visibility = View.GONE
                    } else {
                        showSnackBar(modelObj.data.message);
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured));
                }
            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured));
                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
            }
        })
    }

    private fun getIntents() {
        restro_id = intent.getStringExtra("RESTAURANT_ID")
        from = intent.getStringExtra("FROM")
        userWhoSent = intent.getStringExtra("USER_WHO_SENT_ID")
        timeDate = intent.getStringExtra("TIME")
    }

}

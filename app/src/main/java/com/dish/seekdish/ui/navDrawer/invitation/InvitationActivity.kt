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
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.ui.navDrawer.invitation.includeFriends.IncludeFriendsActivity
import kotlinx.android.synthetic.main.activity_invitation.tvBack
import java.util.*


class InvitationActivity : BaseActivity() {


    lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var adapter: InvitationAdapter
    lateinit var date: Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation)

        // setting up tabLayout
        this.tabLayout = findViewById(R.id.tabLayoutInvitationActivity)

        tabLayout.addTab(tabLayout.newTab().setText("Invited"))
        tabLayout.addTab(tabLayout.newTab().setText("Map"))
        tabLayout.addTab(tabLayout.newTab().setText("Details"))

//        //change font
//        changeTabsFont();

        viewPager = findViewById(R.id.viewPagerInvitationActivity) as ViewPager
        adapter = InvitationAdapter(this.supportFragmentManager, tabLayout.tabCount)


        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


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


        tvSettings.setOnClickListener()
        {
            val intent = Intent(this@InvitationActivity, InvitationSettingsActivity::class.java)
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
            onInvitationSendClick()
        }


    }

    private fun onInvitationSendClick() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_email_verify)
        val textViewTitle = dialog.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescrp = dialog.findViewById<TextView>(R.id.textViewDescrp)
        val btnAccept = dialog.findViewById<Button>(R.id.btnAccept)


        textViewTitle.setText("Invitation Sent")
        textViewDescrp.setText("Your invitation sent successfully.")
        // button_yes clk
        btnAccept.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()

    }


    fun showDateTimePicker() {
        val currentDate = Calendar.getInstance()
        date = Calendar.getInstance()
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            date.set(year, monthOfYear, dayOfMonth)
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                date.set(Calendar.MINUTE, minute)
                Log.v("time", "The choosen one " + hourOfDay + minute)

                Log.v("Datetime", "The choosen one " + date.getTime())
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show()
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show()

        var date: String =
            currentDate.get(Calendar.DATE).toString() + "/" + currentDate.get(Calendar.MONTH) + "/" + currentDate.get(
                Calendar.YEAR
            )
        var time: String = currentDate.get(Calendar.HOUR_OF_DAY).toString() + ":" + currentDate.get(Calendar.MINUTE)
        Log.e("time", time)
        Log.e("date", date)

        tvDate.setText(date)
        tvTime.setText(date)

    }


}

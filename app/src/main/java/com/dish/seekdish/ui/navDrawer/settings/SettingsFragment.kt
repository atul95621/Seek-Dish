package com.dish.seekdish.ui.navDrawer.settings

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dish.seekdish.util.BaseFragment
import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.activities.MyInformationActivity
import com.dish.seekdish.ui.navDrawer.settings.activity.*

import com.dish.seekdish.ui.navDrawer.settings.myAlerts.MyAlertsActivity
import com.dish.seekdish.ui.navDrawer.settings.presenter.SettingFragPresenter
import com.dish.seekdish.ui.navDrawer.settings.view.ISettingView
import com.dish.seekdish.util.SessionManager

import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

import com.travijuu.numberpicker.library.NumberPicker
import retrofit2.Response
import kotlin.collections.ArrayList
import android.widget.Toast
import com.facebook.FacebookSdk.getApplicationContext
import android.widget.AdapterView
import android.widget.ListView
import com.dish.seekdish.custom.CustomListAdapterDialog
import com.dish.seekdish.ui.navDrawer.settings.dataModel.*
import android.app.Activity


class SettingsFragment : BaseFragment(), ISettingView {


    internal var langArr: ArrayList<String> = ArrayList<String>()
    private lateinit var homeActivity: HomeActivity
    internal var PLACE_PICKER_REQUEST = 1

    var address: String? = null
    var numberPicker: NumberPicker? = null

    lateinit var settingFragPresenter: SettingFragPresenter

    var languageId: Int = 0
    var languageName: String = ""
    var dialog: Dialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        homeActivity = activity as HomeActivity

        settingFragPresenter = SettingFragPresenter(this, homeActivity)

        numberPicker = view.findViewById(R.id.number_picker) as NumberPicker

        dialog = Dialog(homeActivity)


        //geting general setting
        getSettingInfo()

        // set address, language, radius center...
        setAddressRadiusdLang(view)




        view.frameLike.setOnClickListener()
        {
            val intent = Intent(activity, LikeActivity::class.java)
            startActivityForResult(intent, 1);
        }

        view.frameDislike.setOnClickListener()
        {
            val intent = Intent(activity, DislikeActivity::class.java)
            startActivityForResult(intent, 1);
        }

        view.tvRecievedReq.setOnClickListener()
        {
            val intent = Intent(activity, ReceivedRequestActivity::class.java)
            startActivity(intent)
        }

        view.tvSentReq.setOnClickListener()
        {
            val intent = Intent(activity, SentRequestActivity::class.java)
            startActivity(intent)
        }

        view.tvAlert.setOnClickListener()
        {
            val intent = Intent(activity, MyAlertsActivity::class.java)
            startActivity(intent)
        }

        view.linRadiusCenter.setOnClickListener()
        {
            val intent = Intent(activity, RadiusCenterActivity::class.java)
            startActivityForResult(intent, 1)
        }

        view.tvUpdateProfile.setOnClickListener()
        {
            val intent = Intent(activity, MyInformationActivity::class.java)
            startActivity(intent)
        }

        view.linLanguage.setOnClickListener()
        {
            // hittting langugaes api
            getLanguages()

        }

        numberPicker!!.setMax(25)
        numberPicker!!.setMin(1)
        numberPicker!!.setUnit(1)
//        numberPicker.setValue(15)

        homeActivity.tvAdd.setOnClickListener()
        {

            val geoCheck: String
            val pushCheck: String
            val privateCheck: String

            if (switchGeo.isChecked) {
                geoCheck = "1"
            } else {
                geoCheck = "0"
            }
            if (switchPrivate.isChecked) {
                privateCheck = "1"
            } else {
                privateCheck = "0"
            }
            if (switchPush.isChecked) {
                pushCheck = "1"
            } else {
                pushCheck = "0"
            }
            //check connection
            if (homeActivity.connectionDetector.isConnectingToInternet) {

                settingFragPresenter.setGeneralSettingInfo(
                    sessionManager.getValue(SessionManager.USER_ID),
                    geoCheck,
                    pushCheck,
                    privateCheck,
                    numberPicker!!.value
                )

            } else {
                showSnackBar(getString(R.string.check_connection))
            }
        }


        return view
    }

    private fun setAddressRadiusdLang(view: View) {
        var language: String = sessionManager.getValue(SessionManager.LANGUAGE_NAME)
        var radiusCenter: String = sessionManager.getValue(SessionManager.PLACE_SELECTED)

        if (language != null && language != "null" && language != "") {
            view.tvLanguage.setText(language)
        }
        if (radiusCenter != null && radiusCenter != "null" && radiusCenter != "") {
            view.txtPlace.setText(radiusCenter)
        }


        // setting the address name
        if (!sessionManager.getValue(SessionManager.PLACE_SELECTED).equals("") && sessionManager.getValue(SessionManager.PLACE_SELECTED) != null && sessionManager.getValue(
                SessionManager.PLACE_SELECTED
            ) != "null"
        ) {
            Log.e("placelog", " " + sessionManager.getValue(SessionManager.PLACE_SELECTED))
            var placeSelected = sessionManager.getValue(SessionManager.PLACE_SELECTED)
            view.txtPlace.setText(placeSelected)
        } else {
            view.txtPlace.setText(" ")
        }

    }

    private fun getLanguageAndAddress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getSettingInfo() {

        //check connection
        if (homeActivity.connectionDetector.isConnectingToInternet) {

            settingFragPresenter.getGeneralSettingInfo(sessionManager.getValue(SessionManager.USER_ID))

        } else {
            showSnackBar(getString(R.string.check_connection))
        }
    }


    fun showDialog(listData: ArrayList<LangData>) {


        val view: View = getLayoutInflater().inflate(R.layout.dialog_language, null);


        // Change MyActivity.this and myListOfItems to your own values
        val clad: CustomListAdapterDialog = CustomListAdapterDialog(homeActivity, listData);

        val list = view.findViewById<View>(R.id.custom_list) as ListView


        list.setAdapter(clad);

        list.setOnItemClickListener(AdapterView.OnItemClickListener { adapter, view, position, arg ->
            // TODO Auto-generated method stub
//           val tvLanguage = view.findViewById<View>(R.id.tvLanguage) as TextView


            Toast.makeText(
                getApplicationContext(),
                "selected Item Name is " + listData[position].id + "     " + listData[position].name,
                Toast.LENGTH_LONG
            ).show()

            languageId = listData[position].id
            languageName = listData[position].name


            setLanguage()
            /*tvLanguage.setText(languageName)

            dialog.dismiss()*/
        }
        )

        dialog?.setContentView(view);

        dialog?.show();

    }

    private fun setLanguage() {
        //check connection
        if (homeActivity.connectionDetector.isConnectingToInternet) {

            settingFragPresenter.setLanguageSelected(
                sessionManager.getValue(SessionManager.USER_ID),
                languageId.toString()
            )

        } else {
            showSnackBar(getString(R.string.check_connection))
        }
    }

    override fun onGetSettingInfo(result: Boolean, response: Response<SettingDataClass>) {

        if (result) {

            val settingDataClass = response.body() as SettingDataClass
            if (settingDataClass.status == 1) {
                Log.e(
                    "values",
                    "  " + "privateKey:  " + settingDataClass.data.private + "  " + "geoloactionkey:  " + settingDataClass.data.geolocation + "   notifiaction" + settingDataClass.data.push_notification
                )
                try {
                    if (settingDataClass.data.private == 1) {
                        switchPrivate?.isChecked = true
                    } else {
                        switchPrivate?.isChecked = false
                    }
                    if (settingDataClass.data.geolocation == 1) {
                        switchGeo.isChecked = true
                    } else {
                        switchGeo.isChecked = false
                    }
                    if (settingDataClass.data.push_notification == 1) {
                        switchPush.isChecked = true
                    } else {
                        switchPush.isChecked = false
                    }


                    val radius = settingDataClass.data.radius

                    sessionManager.setValues(SessionManager.RADIUS, radius.toString())

                    tvLikeCount.setText(settingDataClass.data.liked_count.toString() + " I Like")
                    tvDisLikeCount.setText(settingDataClass.data.disliked_count.toString() + " I Dislike")

                    numberPicker?.setValue(settingDataClass.data.radius)

                    numberPicker?.isEnabled = true

                } catch (e: Exception) {

                    showSnackBar(this.getResources().getString(R.string.error_occured));

                }

            } else {
                showSnackBar(homeActivity.getResources().getString(R.string.error_occured));
            }
        } else {

            showSnackBar(homeActivity.getResources().getString(R.string.error_occured));

        }
    }

    private fun getLanguages() {

        //check connection
        if (homeActivity.connectionDetector.isConnectingToInternet) {

            settingFragPresenter.getLanguagesInfo(
                sessionManager.getValue(SessionManager.USER_ID)

            )

        } else {
            showSnackBar(getString(R.string.check_connection))
        }

    }


    override fun onSetSettingInfo(result: Boolean, response: Response<SendUserGeneralSetting>) {


        if (result == true) {
            val sendUserGeneralSetting = response.body() as SendUserGeneralSetting

            if (sendUserGeneralSetting.status == 1)

                showSnackBar(homeActivity.getResources().getString(R.string.setting_saved));
        } else {
            showSnackBar(homeActivity.getResources().getString(R.string.error_occured));

        }
    }


    override fun onGetLanguageInfo(result: Boolean, response: Response<LanguageData>) {


        if (result == true) {
            val languageData = response.body() as LanguageData

            if (languageData.status == 1)

                showDialog(languageData.data)

        } else {
            showSnackBar(homeActivity.getResources().getString(R.string.error_occured));

        }

    }

    override fun onSaveLanguageInfo(result: Boolean, response: Response<SaveLanguageModel>) {

        if (result == true) {
            val saveLangData = response.body() as SaveLanguageModel

            if (saveLangData.status == 1)

                sessionManager.setValues(SessionManager.LANGUAGE_ID, languageId.toString())
            sessionManager.setValues(SessionManager.LANGUAGE_NAME, languageName.toString())

            tvLanguage.setText(languageName)

            dialog?.dismiss()

            showSnackBar(saveLangData.data.message);

        } else {
            showSnackBar(homeActivity.getResources().getString(R.string.error_occured));

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {

            //Write your code if there's no result
            if (resultCode == Activity.RESULT_CANCELED) {

                // setting radius senter once selected...
                view?.let { setAddressRadiusdLang(it) }
                //geting general setting refreshing again...
                getSettingInfo()
            }
        }

    }


}

package com.dish.seekdish.ui.language

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.CustomListAdapterDialog
import com.dish.seekdish.walkthrough.WalkThroughActivity
import kotlinx.android.synthetic.main.activity_language.*
import java.util.ArrayList
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LangData
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LanguageData
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SaveLanguageModel
import com.dish.seekdish.ui.navDrawer.settings.presenter.SettingFragPresenter
import com.dish.seekdish.util.SessionManager
import com.facebook.FacebookSdk
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.tvLanguage
import retrofit2.Response


class LanguageActivity : BaseActivity(), ILanguageView {

    internal var langArr = ArrayList<String>()
    var sessionManager: SessionManager? = null;
    lateinit var languagePresenter: LanguagePresenter

    var languageId: Int = 0
    var languageName: String = ""
    var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        sessionManager = SessionManager(this)
        languagePresenter = LanguagePresenter(this, this@LanguageActivity)
        dialog = Dialog(this)

        Log.e("selectLangBefore", sessionManager?.getValue(SessionManager.IS_LANGUAGE_SELECTED))
        if (sessionManager?.getValue(SessionManager.IS_LANGUAGE_SELECTED).equals("1") && sessionManager?.getValue(
                SessionManager.LOGGEDIN
            ).equals("1")
        ) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else if (sessionManager?.getValue(SessionManager.IS_LANGUAGE_SELECTED).equals("1")
        ) {
            val intent = Intent(activity, WalkThroughActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {

            /* // setting languge
             setLanguage()*/


            tvNext.setOnClickListener {

                if (tvLanguageSelect.text.toString() === "Select language")
                    showSnackBar("Please select language")
                else {
                    sessionManager?.setValues(SessionManager.LANGUAGE_ID, languageId.toString())
                    sessionManager?.setValues(SessionManager.LANGUAGE_NAME, languageName.toString())
                    sessionManager?.setValues(SessionManager.IS_LANGUAGE_SELECTED, "1")
                    sessionManager?.savesSessionLang(conxt,SessionManager.LANGUAGE_HOME_ACTIVITY,languageId.toString())

                    Log.e("selectLang", sessionManager?.getValue(SessionManager.IS_LANGUAGE_SELECTED))
                    val intent = Intent(this@LanguageActivity, WalkThroughActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

            }
        }

        tvLanguageSelect.setOnClickListener()
        {
            // hittting langugaes api
            getLanguages()

        }

    }

    private fun getLanguages() {

        //check connection
        if (this.connectionDetector.isConnectingToInternet) {

            languagePresenter.getLanguagesInfo(
                sessionManager?.getValue(SessionManager.USER_ID).toString()

            )

        } else {
            showSnackBar(getString(R.string.check_connection))
        }

    }

    override fun onGetLanguageInfo(result: Boolean, response: Response<LanguageData>) {


        if (result == true) {
            val languageData = response.body() as LanguageData

            if (languageData.status == 1)

                showDialog(languageData.data)

        } else {
            showSnackBar(this.getResources().getString(R.string.error_occured));

        }

    }


    fun showDialog(listData: ArrayList<LangData>) {


        val view: View = getLayoutInflater().inflate(R.layout.dialog_language, null);


        // Change MyActivity.this and myListOfItems to your own values
        val clad: CustomListAdapterDialog = CustomListAdapterDialog(this, listData);

        val list = view.findViewById<View>(R.id.custom_list) as ListView


        list.setAdapter(clad);

        list.setOnItemClickListener(AdapterView.OnItemClickListener { adapter, view, position, arg ->
            // TODO Auto-generated method stub
//           val tvLanguage = view.findViewById<View>(R.id.tvLanguage) as TextView

/*
            Toast.makeText(
                FacebookSdk.getApplicationContext(),
                "selected Item Name is " + listData[position].id + "     " + listData[position].name,
                Toast.LENGTH_LONG
            ).show()*/

            languageId = listData[position].id
            languageName = listData[position].name
            tvLanguageSelect.setText(languageName)

            dialog?.dismiss()
        }
        )

        dialog?.setContentView(view);

        dialog?.show();

    }


}

package com.dish.seekdish.ui.language

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
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
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.login.LoginActivity
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
import java.util.*


class LanguageActivity : BaseActivity(), ILanguageView {

    internal var langArr = ArrayList<String>()
    var sessionManager: SessionManager? = null;
    lateinit var languagePresenter: LanguagePresenter
    var languageId: Int = 0
    var languageName: String = ""
    var dialog: Dialog? = null
    var languageCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        sessionManager = SessionManager(this)
        languagePresenter = LanguagePresenter(this, this@LanguageActivity)
        dialog = Dialog(this)

        var isLanguageSelected = sessionManager?.getLangValue(SessionManager.IS_LANGUAGE_SELECTED)
        var isLoggedIn = sessionManager?.getValue(SessionManager.LOGGEDIN)
        if (isLanguageSelected.equals("1") && isLoggedIn.equals("1")
        ) {
            var langCode = sessionManager?.getLangValue(SessionManager.LANGUAGE_CODE)
            if (langCode.isNullOrBlank() == false) {
                setLocale(langCode)
            } else {
                setLocale("en")
            }
            val intent = Intent(activity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else if (isLoggedIn.equals("0")) {
            // this case when update is available and loggedin is set to 0
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else if (isLanguageSelected.equals("1") && isLoggedIn.equals("")) {
            val intent = Intent(activity, WalkThroughActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {

            /* // setting languge
             setLanguage()*/


            tvNext.setOnClickListener {
                if (tvLanguageSelect.text.toString() == resources.getString(R.string.select_language_D)) {
                    showSnackBar(resources.getString(R.string.select_language_D))
                } else {
                    sessionManager?.setValues(SessionManager.LANGUAGE_ID, languageId.toString())
                    sessionManager?.savesSessionLang(
                        SessionManager.LANGUAGE_NAME,
                        languageName.toString()
                    )
                    sessionManager?.savesSessionLang(SessionManager.IS_LANGUAGE_SELECTED, "1")
                    sessionManager?.savesSessionLang(
                        SessionManager.LANGUAGE_HOME_ACTIVITY,
                        languageId.toString()
                    )
                    sessionManager?.savesSessionLang(
                        SessionManager.LANGUAGE_CODE,
                        languageCode.toString()
                    )
                    // changing the langugae
                    setLocale(languageCode)
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
            languagePresenter.getLanguagesInfo("")
        } else {
            showSnackBar(getString(R.string.check_connection))
        }
    }

    override fun onGetLanguageInfo(result: Boolean, response: Response<LanguageData>) {
        if (result == true) {
            val languageData = response.body() as LanguageData

            if (languageData.status == 1) {
                Log.e("langResp", "  " + response)
                showDialog(languageData.data)
            } else {
                showSnackBar(languageData.message)
            }
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
            languageId = listData[position].id
            languageName = listData[position].name
            languageCode = listData[position].prefix
            tvLanguageSelect.setText(languageName)
            dialog?.dismiss()
        }
        )
        dialog?.setContentView(view);
        dialog?.show();
    }
}

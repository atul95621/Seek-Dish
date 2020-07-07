package com.dish.seekdish.ui.login

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.dish.seekdish.R
import com.dish.seekdish.ui.ForgotActivity
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response
import java.lang.Exception
import java.lang.reflect.Executable


class LoginActivity : BaseActivity(), ILoginView {


    lateinit var loginPresenter: LoginPresenter

    // lateinit var context  : Context
    var sessionManager: SessionManager? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //   context = this;

        //make preseneter
        loginPresenter = LoginPresenter(this, this@LoginActivity)
        sessionManager = SessionManager(this)

        getSavedCred()

        tvValidate.setOnClickListener()
        {
            if (TextUtils.isEmpty(edtEmail!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.enter_email))
                edtEmail!!.requestFocus()
            } else if (emailValidator(edtEmail.text.toString()) == false) {
                showSnackBar(getString(R.string.valid_email))
            } else if (TextUtils.isEmpty(edtPassword!!.text.toString().trim { it <= ' ' })) {
                showSnackBar(getString(R.string.enter_passss))
                edtPassword!!.requestFocus()
            } else {

                if (connectionDetector.isConnectingToInternet) {
                    //calling api
                    loginPresenter.login(
                        edtEmail.text.toString(),
                        edtPassword.text.toString(),
                        sessionManager?.getValue(SessionManager.LANGUAGE_ID).toString(),
                        sessionManager?.getValue(SessionManager.FCM_TOKEN).toString()
                    )
                } else {
                    showSnackBar(resources.getString(R.string.check_connection))
                }
            }
        }
        tvForgot.setOnClickListener()
        {
            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(intent)
        }
        tvBack.setOnClickListener()
        {
            finish()
        }
    }

    private fun getSavedCred() {
        if (sessionManager?.getLangValue(SessionManager.REMEMBER).equals("1")) {
            edtEmail.setText(sessionManager?.getLangValue(SessionManager.REMEMBER_EMAIL).toString())
            edtPassword.setText(
                sessionManager?.getLangValue(SessionManager.REMEMBER_PASSWORD).toString()
            )
        }
    }

    override fun onSetLoggedin(result: Boolean, response: Response<LoginDataClass>) {
        if (result == true) {
            val loginmodel = response.body() as LoginDataClass
            if (loginmodel.status == 1) {
                sessionManager?.setValues(SessionManager.USERNAME, loginmodel.data.username)
                sessionManager?.setValues(SessionManager.FIRST_NAME, loginmodel.data.first_name)
                sessionManager?.setValues(SessionManager.LAST_NAME, loginmodel.data.last_name)
                sessionManager?.setValues(SessionManager.EMAIL, loginmodel.data.email)
                sessionManager?.setValues(SessionManager.PHONE, loginmodel.data.phone)
                sessionManager?.setValues(SessionManager.USER_ID, loginmodel.data.id.toString())
                sessionManager?.setValues(
                    SessionManager.COUNTRY_CODE,
                    loginmodel.data.country_code.toString()
                )
                sessionManager?.setValues(SessionManager.PHOTO_URL, loginmodel.data.photo)
                sessionManager?.setValues(SessionManager.FCM_TOKEN, loginmodel.data.fcm_token)
                sessionManager?.setValues(SessionManager.GENDER, loginmodel.data.gender)
                sessionManager?.setValues(SessionManager.BIO, loginmodel.data.bio)

                if (checkboxRememberMe.isChecked) {
                    sessionManager?.setValues(SessionManager.LOGGEDIN, "1")
                    // saving the credentails
                    sessionManager?.savesSessionLang(SessionManager.REMEMBER, "1")
                    sessionManager?.savesSessionLang(
                        SessionManager.REMEMBER_EMAIL,
                        edtEmail.text.toString()
                    )
                    sessionManager?.savesSessionLang(
                        SessionManager.REMEMBER_PASSWORD,
                        edtPassword.text.toString()
                    )
                } else {
                    sessionManager?.clearOtherSessionValue(SessionManager.REMEMBER)
                    sessionManager?.clearOtherSessionValue(SessionManager.REMEMBER_EMAIL)
                    sessionManager?.clearOtherSessionValue(SessionManager.REMEMBER_PASSWORD)
                }

                sessionManager?.setValues(SessionManager.LOGGEDIN_THROUGH, "0")


                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                showSnackBar(loginmodel.message)
            }

        } else {
            showSnackBar(resources.getString(R.string.error_occured) + "    ${response.code()}")
        }
    }

    override fun checkUpdate(result: Boolean, response: Response<CheckUpdateModel>) {
        if (result == true) {
            val checkUpdateModel = response.body() as CheckUpdateModel
            if (checkUpdateModel.status == 1) {
                var version = ""
                try {
                    val pInfo: PackageInfo =
                        this.getPackageManager().getPackageInfo(packageName, 0)
                    version = pInfo.versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                if (!version.isNullOrEmpty()&& checkUpdateModel.force_update_android!=0) {
                    if (version.toFloat() < checkUpdateModel.Android_version.toFloat()) {
                        showUpdateDialog(getString(R.string.update))
                    }
                }
            } else {
                showSnackBar(checkUpdateModel.message)
            }

        } else {
            showSnackBar(resources.getString(R.string.error_occured) + "    ${response.code()}")
        }
    }

    private fun showUpdateDialog(message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_email_verify)

        val textViewDescrp = dialog.findViewById<TextView>(R.id.textViewDescrp)
        val btnAccept = dialog.findViewById<Button>(R.id.btnAccept)
//        textViewDescrp.setText(message)
        textViewDescrp.setText(message)
        // button_yes clk
        btnAccept.setText(getString(R.string.update_str))
        btnAccept.setOnClickListener {
            dialog.dismiss()
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

            }
        }
        dialog.show()
    }

    private fun checkIfUpdateAvailable() {
        loginPresenter?.checkUpdate()
    }

    override fun onResume() {
        super.onResume()
        // checking if new version available
        checkIfUpdateAvailable()
    }


}

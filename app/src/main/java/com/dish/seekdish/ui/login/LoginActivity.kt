package com.dish.seekdish.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.ui.ForgotActivity
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edtEmail
import kotlinx.android.synthetic.main.activity_login.edtPassword
import kotlinx.android.synthetic.main.activity_login.tvBack
import retrofit2.Response


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
            edtPassword.setText(sessionManager?.getLangValue(SessionManager.REMEMBER_PASSWORD).toString())
        }
    }

    override fun onSetLoggedin(result: Boolean, response: Response<LoginDataClass>) {

        if (result == true) {

//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)

//
//            /* Log.e("responseCode", response.code().toString() + "")
//             Log.e("responseStatus", " " + response.body()?.status)
//             Log.e("responseString", " " + response.body().toString())
//             Log.e("responseerror", " " + response.errorBody().toString())*/
//
////            Log.e("responseUsername", loginmodel.data.username + "")

            val loginmodel = response.body() as LoginDataClass

            if(loginmodel.status==1) {
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


                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            else
            {
                showSnackBar(loginmodel.message)
            }

        } else {
            showSnackBar(resources.getString(R.string.error_occured)  +"    ${response.code()}")
        }
    }


}

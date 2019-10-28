package com.dish.seekdish.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dish.seekdish.R
import com.dish.seekdish.ui.language.LanguageActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val background = object : Thread() {
            override fun run() {

                try {
                    // Thread will sleep for 5 seconds
                    Thread.sleep((2 * 1000).toLong())

//                    if (!sessionManager.getValue(SessionManager.CUSTOMER_ID).toString().equals("")) {
//                        Log.e("sessioncustomer", sessionManager.getValue(SessionManager.CUSTOMER_ID).equals("").toString())
//                        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
//                        startActivity(intent)
//
//                        finish()
//                    } else {
                    val intent = Intent(this@SplashActivity, LanguageActivity::class.java)
                    startActivity(intent)


                    // After 5 seconds redirect to another intent


                    //Remove activity
                    finish()
//                    }

                } catch (e: Exception) {

                }

            }
        }

        // start thread
        background.start()


    }
}

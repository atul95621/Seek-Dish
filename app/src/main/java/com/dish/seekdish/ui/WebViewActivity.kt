package com.dish.seekdish.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dish.seekdish.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val urlToLoad = intent.getStringExtra("url")
        val fromPage = intent.getStringExtra("from")

        if (fromPage.equals("SignupTerms")) {
            tvTitle.setText("Terms of Use")
            loadUrl(urlToLoad)
        }
        if (fromPage.equals("SignupPolicy")) {
            tvTitle.setText("Privacy Policy")
            loadUrl(urlToLoad)
        }

        tvBack.setOnClickListener()
        {
            finish()
        }

    }

    private fun loadUrl(urlToLoad: String) {
        mywebview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        mywebview!!.loadUrl(urlToLoad)
    }
}

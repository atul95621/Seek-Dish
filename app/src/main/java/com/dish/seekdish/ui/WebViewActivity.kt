package com.dish.seekdish.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dish.seekdish.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val urlToLoad = intent.getStringExtra("url").toString()
        val fromPage = intent.getStringExtra("from")

        if (fromPage?.equals("SignupTerms") == true) {
            tvTitle.setText(getString(R.string.terms_of_use))
            loadUrl(urlToLoad)
        } else if (fromPage?.equals("SignupPolicy") == true) {
            tvTitle.setText(getString(R.string.privacy_policy))
            loadUrl(urlToLoad)
        } else if (fromPage?.equals("RestroDescrpActivity") == true) {
            tvTitle.setText(getString(R.string.menu))
            loadUrl(urlToLoad)
        } else if (fromPage?.equals("RestroDetailsFragment") == true) {
            tvTitle.setText(getString(R.string.menu))
            loadUrl("https://docs.google.com/gview?embedded=true&url=$urlToLoad")
        }

        tvBack.setOnClickListener()
        {
            finish()
        }

    }

    private fun loadUrl(urlToLoad: String) {
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.settings.setSupportZoom(true)
        mywebview.getSettings().setLoadWithOverviewMode(true);

        mywebview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressWeb.visibility = View.GONE
            }
        }
        mywebview!!.loadUrl(urlToLoad)
    }
}

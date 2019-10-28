package com.dish.seekdish.ui.navDrawer.dishDescription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dish.seekdish.R
import kotlinx.android.synthetic.main.activity_opinion_details.*

class OpinionDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opinion_details)

        tvBack.setOnClickListener()
        {
            finish()
        }
    }
}

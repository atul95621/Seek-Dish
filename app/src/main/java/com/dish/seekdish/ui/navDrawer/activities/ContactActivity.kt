package com.dish.seekdish.ui.navDrawer.activities

import android.os.Bundle
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        hideKeyBoard()

        tvBack.setOnClickListener()
        {
            finish()
        }
    }
}

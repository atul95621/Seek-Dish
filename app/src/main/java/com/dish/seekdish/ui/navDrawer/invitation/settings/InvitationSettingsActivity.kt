package com.dish.seekdish.ui.navDrawer.invitation.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dish.seekdish.R
import com.travijuu.numberpicker.library.NumberPicker
import kotlinx.android.synthetic.main.activity_invitation_settings.*

class InvitationSettingsActivity : AppCompatActivity() {
    var validity = ""
    var allow = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation_settings)

        getIntents()

        tvBack.setOnClickListener()
        {
            finish()
        }

        if (allow.equals("0")) {
            switchAllow.isEnabled = true
        } else {
            switchAllow.isEnabled = false
        }

        val numberPicker = findViewById(R.id.number_picker) as NumberPicker
        numberPicker.setMax(72)
        numberPicker.setMin(1)
        numberPicker.setUnit(1)
        numberPicker.setValue(2)
    }

    private fun getIntents() {
        validity = intent.getStringExtra("VALIDITY")
        allow = intent.getStringExtra("ALLOW_INVITATION")
    }
}

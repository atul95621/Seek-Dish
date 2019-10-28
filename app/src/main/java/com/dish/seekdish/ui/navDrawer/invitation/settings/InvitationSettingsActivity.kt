package com.dish.seekdish.ui.navDrawer.invitation.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dish.seekdish.R
import com.travijuu.numberpicker.library.NumberPicker
import kotlinx.android.synthetic.main.activity_invitation_settings.*

class InvitationSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation_settings)

        tvBack.setOnClickListener()
        {
            finish()
        }

        val numberPicker = findViewById(R.id.number_picker) as NumberPicker
        numberPicker.setMax(100)
        numberPicker.setMin(15)
        numberPicker.setUnit(2)
        numberPicker.setValue(20)
    }
}

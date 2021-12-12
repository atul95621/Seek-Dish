package com.dish.seekdish.ui.navDrawer.invitation.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.invitation.InvitationAdapter
import com.dish.seekdish.ui.navDrawer.settings.dataModel.CancelReModel
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import com.google.android.material.tabs.TabLayout
import com.travijuu.numberpicker.library.NumberPicker
import kotlinx.android.synthetic.main.activity_invitation.*
import kotlinx.android.synthetic.main.activity_invitation_settings.*
import kotlinx.android.synthetic.main.activity_invitation_settings.tvBack
import kotlinx.android.synthetic.main.fragment_my_friends.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvitationSettingsActivity : BaseActivity() {
    var validity = ""
    var allow = ""
    internal lateinit var apiInterface: APIInterface
    var sessionManager: SessionManager? = null
    var numberPicker: NumberPicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation_settings)

        getIntents()
        sessionManager = SessionManager(this)
        numberPicker = findViewById(R.id.number_picker) as NumberPicker

        tvBack.setOnClickListener()
        {
            finish()
        }

        tvSave.setOnClickListener()
        {
            getInviattionSetting()
        }

        if (allow.equals("0")) {
            switchAllow.isEnabled = true
        } else {
            switchAllow.isEnabled = false
        }

        numberPicker?.setMax(23)
        numberPicker?.setMin(1)
        numberPicker?.setUnit(1)
        numberPicker?.setValue(2)
    }

    private fun getIntents() {
        validity = intent.getStringExtra("VALIDITY").toString()
        allow = intent.getStringExtra("ALLOW_INVITATION").toString()
    }

    fun getInviattionSetting() {
        ProgressBarClass.progressBarCalling(this)
        var allowInti = ""
        if (switchAllow.isChecked == true) {
            allowInti = "1"
        } else {
            allowInti = "0"
        }
        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)
        val call =
            apiInterface.getInvitationSetting(
                sessionManager?.getValue(SessionManager.USER_ID).toString(),
                allowInti, number_picker.value.toString()
            )
        call.enqueue(object : Callback<CancelReModel> {
            override fun onResponse(
                call: Call<CancelReModel>,
                response: Response<CancelReModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()
                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as CancelReModel

                    if (modelObj.status == 0) {
                        showSnackBar(modelObj.message);
                    } else {
                        showSnackBar(modelObj.message);
                    }
                } else {
                    showSnackBar(resources.getString(R.string.error_occured)+"  ${response.code()}");
                }
            }

            override fun onFailure(call: Call<CancelReModel>, t: Throwable) {
                showSnackBar(resources.getString(R.string.error_occured)+"  ${t.message}");

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }
}

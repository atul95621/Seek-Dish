package com.dish.seekdish.ui.navDrawer.myFriends.contactFetch

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.provider.ContactsContract
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.retrofit.APIClient
import com.dish.seekdish.retrofit.APIInterface
import com.dish.seekdish.ui.navDrawer.checkin.adapter.CheckinAdapter
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.util.SessionManager
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.activity_contact_fetch.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler


class ContactFetchActivity : BaseActivity() {

    private var listView: ListView? = null
    private var contFetchAdapter: ContFetchAdapter? = null
    private var contactModelArrayList = ArrayList<ContactModel>()
    private var contactMatchedArr = ArrayList<ContactModel>()

    private var arraylist = ArrayList<Data>()

    val PERMISSION_REQUEST_IMG_CODE = 1
    var sessionManager: SessionManager? = null;
    internal lateinit var apiInterface: APIInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.dish.seekdish.R.layout.activity_contact_fetch)

        listView = findViewById(com.dish.seekdish.R.id.listView) as ListView

        sessionManager = SessionManager(this)

        imgContactFetch.setOnClickListener()
        {
            if (checkImgPermissionIsEnabledOrNot()) {
                contactModelArrayList.clear()
//                fetchContact()
                allFriendsAPi()
            } else {
                requestImagePermission()
            }
        }
    }

    fun fetchContact() {
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactModel()
            contactModel.setNames(name)
            contactModel.setNumbers(phoneNumber.replace("\\s".toRegex(), ""))
            contactModelArrayList!!.add(contactModel)
            Log.e("info>>", name + "  " + phoneNumber)
        }
        phones.close()

        /*    val emails = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            while (emails!!.moveToNext()) {
                val name =
                    emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))

                val contactModel = ContactModel()
                contactModel.setNames(name)
                contactModel.setNumbers(phoneNumber)
                contactModelArrayList!!.add(contactModel)
                Log.e("info222222222>>   {$name}", name + "  " + phoneNumber)
            }
            emails.close()*/

//        compareNumbers()
        val ha = Handler()
        ha.postDelayed(object : Runnable {

            override fun run() {
                //call function
                compareNumbers()
                ha.postDelayed(this, 500)
            }
        }, 500)
//        contFetchAdapter = ContFetchAdapter(this, contactModelArrayList!!)
//        listView!!.adapter = contFetchAdapter

    }

    private fun compareNumbers() {
        val contactModel = ContactModel()

        for (i in 0 until arraylist.size) {
            var searchString = arraylist[i].phone
            for (j in 0 until contactModelArrayList.size) {
                Log.e("resuktt run", "" + j)

                if (contactModelArrayList[j].getNumbers().contains(searchString)) {
                    Log.e("resuktt succ", "" + contactModelArrayList[j].number)
                    contactMatchedArr.add(contactModelArrayList[j])
                } else {
                    Log.e("resuktt", "" + contactModelArrayList[j].number + " not founndd")
                }
            }
        }

        contFetchAdapter = ContFetchAdapter(this, contactModelArrayList!!)
        listView!!.adapter = contFetchAdapter
    }

    private fun checkImgPermissionIsEnabledOrNot(): Boolean {

        val FirstPermissionResult =
            this?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) }
        val SecondPermissionResult =
            this?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_CONTACTS) }

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED

    }

    //LOCATION PERSMISSON
    private fun requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                ), PERMISSION_REQUEST_IMG_CODE
            )
        }


        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            when (requestCode) {


                PERMISSION_REQUEST_IMG_CODE -> {

                    val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val read = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (write && read) {
                        fetchContact()
                    }
                }

                else ->

                    Toast.makeText(
                        this, "Please give permission", Toast.LENGTH_LONG
                    ).show()

            }
        }
    }

    fun allFriendsAPi() {

        ProgressBarClass.progressBarCalling(this)

        apiInterface = APIClient.getClient(this).create(APIInterface::class.java)


        val call =
            apiInterface.getAllUsersDetails(sessionManager?.getValue(SessionManager.USER_ID).toString())
        call.enqueue(object : Callback<ContactsDetailsModel> {
            override fun onResponse(
                call: Call<ContactsDetailsModel>,
                response: Response<ContactsDetailsModel>
            ) {
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

                Log.e("respStr", " " + response.body().toString())

                if (response.code().toString().equals("200")) {

                    var modelObj = response.body() as ContactsDetailsModel
                    if (modelObj.status == 1) {

                        if (modelObj.data.size == 0) {
                            tvAlertContact.visibility == View.VISIBLE
                        } else {
                            arraylist = modelObj.data
                            fetchContact()
                        }

                    }

                } else {
//                    iSignUpView.onSetLoggedin(false, response)
                    showSnackBar(resources.getString(com.dish.seekdish.R.string.error_occured));

                }


            }

            override fun onFailure(call: Call<ContactsDetailsModel>, t: Throwable) {

//                Log.e("responseFailure", " " + t.toString())

                showSnackBar(resources.getString(com.dish.seekdish.R.string.error_occured));

                call.cancel()
                // canceling the progress bar
                ProgressBarClass.dialog.dismiss()

            }
        })
    }
}

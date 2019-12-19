package com.dish.seekdish.ui.navDrawer.myFriends.contactFetch

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.provider.ContactsContract
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dish.seekdish.R
import com.dish.seekdish.util.BaseActivity
import kotlinx.android.synthetic.main.activity_contact_fetch.*

class ContactFetchActivity : BaseActivity() {

    private var listView: ListView? = null
    private var contFetchAdapter: ContFetchAdapter? = null
    private var contactModelArrayList= HashSet<ContactModel>()
    val PERMISSION_REQUEST_IMG_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_fetch)

        listView = findViewById(R.id.listView) as ListView


        imgContactFetch.setOnClickListener()
        {
            if (checkImgPermissionIsEnabledOrNot()) {
                contactModelArrayList.clear()
                fetchContact()
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
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactModel()
            contactModel.setNames(name)
            contactModel.setNumbers(phoneNumber)
            contactModelArrayList!!.add(contactModel)
            Log.e("info>>", name + "  " + phoneNumber)
        }
        phones.close()

        val emails = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (emails!!.moveToNext()) {
            val name = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))

            val contactModel = ContactModel()
            contactModel.setNames(name)
            contactModel.setNumbers(phoneNumber)
            contactModelArrayList!!.add(contactModel)
            Log.e("info222222222>>   {$name}", name + "  " + phoneNumber)
        }
        emails.close()

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


        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
}

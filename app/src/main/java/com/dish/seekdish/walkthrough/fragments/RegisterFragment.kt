package com.dish.seekdish.walkthrough.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64.encode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dish.seekdish.R
import com.dish.seekdish.custom.ProgressBarClass
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.login.LoginActivity
import com.dish.seekdish.ui.signup.SignUpModel
import com.dish.seekdish.ui.signup.SignupActivity
import com.dish.seekdish.util.BaseFragment
import com.dish.seekdish.util.SessionManager
import com.dish.seekdish.walkthrough.WalkThroughActivity
import com.dish.seekdish.walkthrough.presenter.RegisterFragPresenter
import com.dish.seekdish.walkthrough.view.IRegisterFragView
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.iid.FirebaseInstanceId
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.fragment_register.view.*
import org.json.JSONException
import retrofit2.Response
import java.net.URLEncoder.encode
import java.security.MessageDigest
import java.util.*

class RegisterFragment : BaseFragment(), IRegisterFragView {


    // facebook
    internal lateinit var callbackManager: CallbackManager
    val PERMISSION_REQUEST_IMG_CODE = 1

    // twitter
    private var client: TwitterAuthClient? = null

    lateinit var mcontext: WalkThroughActivity

    lateinit var registerFragPresenter: RegisterFragPresenter
    var langId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // initializing the facebook and twitter
        setUpTwitter()
        setUpFacebook()

//        printHashKey()
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)

        //activity context
        mcontext = activity as WalkThroughActivity

//        generateSSHKey(mcontext)

        //make preseneter
        registerFragPresenter = RegisterFragPresenter(this, mcontext)
        sessionManager = SessionManager(mcontext)

        //getting FCM
        firebaseToken()

        if (sessionManager.getValue(SessionManager.LANGUAGE_ID).isNullOrEmpty()) {
            var langCode = sessionManager.getLangValue(SessionManager.LANGUAGE_CODE)

            if (langCode.isNullOrBlank() == false) {
                setLocale(langCode)
            } else {
                setLocale("en")
            }
            langId = sessionManager.getLangValue(SessionManager.LANGUAGE_HOME_ACTIVITY)
            Log.e("langii", langId)
            sessionManager.setValues(SessionManager.LANGUAGE_ID, langId)
        }

        if (sessionManager.getValue(SessionManager.LOGGEDIN).equals("1")) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {

            if (checkImgPermissionIsEnabledOrNot()) {
                // initialising the facebook an twitter again
                setUpFacebook()
                setUpTwitter()

               /* FacebookSdk.sdkInitialize(context);
                Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(context));*/
            } else {
                requestImagePermission()
            }

            view.linFacebookLogin.setOnClickListener()
            {
                ProgressBarClass.progressBarCalling(mcontext)
                LoginManager.getInstance()
                    .logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))

            }

            view.linTwitterLogin.setOnClickListener()
            {
                ProgressBarClass.progressBarCalling(mcontext)
                customLoginTwitter()
            }


            view.linSignupManual.setOnClickListener()
            {
                val intent = Intent(activity, SignupActivity::class.java)
                startActivity(intent)
            }

            view.tvLogin.setOnClickListener()
            {

                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }

//        view.tvlogout.setOnClickListener()
//        {
//            TwitterCore.getInstance().getSessionManager().clearActiveSession()
//            Log.e("Twitter", "logout")
//        }

        }
        return view

    }


    private fun setUpTwitter() {

        val config = TwitterConfig.Builder(context)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    getResources().getString(R.string.CONSUMER_KEY),
                    getResources().getString(R.string.CONSUMER_SECRET)
                )
            )
            .debug(true)
            .build()

        Twitter.initialize(config)

//        Log.e("TwitterClient1", "" + client.toString())
        client = TwitterAuthClient()
    }

    private fun setUpFacebook() {

        // facebook login code
        callbackManager = CallbackManager.Factory.create()

        // Callback registration
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    //method to fetch user FACEBOOK  all inforamtion
                    setFacebookData(loginResult)
                }

                override fun onCancel() {
                    ProgressBarClass.dialog.dismiss()
                    showSnackBar(getResources().getString(R.string.error_occured));
                }

                override fun onError(exception: FacebookException) {
                    ProgressBarClass.dialog.dismiss()
                    showSnackBar(exception.message.toString());
                    if (exception is FacebookAuthorizationException) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut()
                        }
                    }
                }
            })


        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // make instance for twitter in ACTIVITY onActivityResult
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            if (client != null) {
//                Log.e("TwitterOnactivity", "Twitter on activity called")
                client?.onActivityResult(requestCode, resultCode, data)
            }
        } else {
//            Log.e("FacebookOnActivity", "Facebook on activity called")
            // Use Facebook callback manager here
            callbackManager.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun setFacebookData(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { `object`, response ->
            try {
                Log.i("Response", response.toString())
                val email: String? = `object`.getString("email")
                val firstName: String? = `object`.getString("first_name")
                val lastName: String? = `object`.getString("last_name")
                val facebookUserId: String? = `object`.getString("id")

                val mProfile: Profile? = Profile.getCurrentProfile()
              /*  val firstName = mProfile?.firstName
                val lastName = mProfile?.lastName
                val facebookUserId = mProfile?.id.toString()
*/
                val imageUrl =
                    "https://graph.facebook.com/" + facebookUserId + "/picture?type=large"

                val profileImage = mProfile?.getProfilePictureUri(250, 250).toString()
             /*   Log.e("LoginFacebook", "Email" + email)
                Log.e("LoginFacebook", "FirstName" + firstName)
                Log.e("LoginFacebook", "LastName" + lastName)
                Log.e("LoginFacebook", "image" + profileImage)
                Log.e("LoginFacebook", "imageurl" + imageUrl)*/

                // hitting api for facebook
                registerFragPresenter.facebookSigin(
                    email.toString(),
                    firstName.toString(),
                    lastName.toString(),
                    imageUrl,
                    sessionManager.getValue(SessionManager.FCM_TOKEN),
                    facebookUserId.toString(),
                    sessionManager.getLangValue(SessionManager.LANGUAGE_HOME_ACTIVITY)
                )

//                ProgressBarClass.dialog.dismiss()


            } catch (e: JSONException) {
                e.printStackTrace()
                ProgressBarClass.dialog.dismiss()
                this.showSnackBar(e.message.toString());
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,email,first_name,last_name,gender")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun checkImgPermissionIsEnabledOrNot(): Boolean {

        val FirstPermissionResult =
            context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        val SecondPermissionResult =
            context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED

    }

    //LOCATION PERSMISSON
    private fun requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_IMG_CODE
            )
        }

    }

    fun customLoginTwitter() {

        //check if user is already authenticated or not
        if (getTwitterSession() == null) {

//            Log.e("TwitterClient2", "" + client.toString())
            //if user is not authenticated start authenticating
            client?.authorize(activity, object : Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>) {

                    val twitterSession = result.data
                    TwitterCore.getInstance().sessionManager.activeSession
                    val authToken = twitterSession.authToken
                    val token = authToken.token
                    val secret = authToken.secret
                    val twitterName = twitterSession.userName


                    fetchTwitterEmail(twitterSession)

                }

                override fun failure(e: TwitterException) {

                    // dismissing if the user has backed without loggin
                    ProgressBarClass.dialog.dismiss()

//                    Log.e("TwitterException", "" + e.toString() + "    " + e.printStackTrace());
                    showSnackBar(resources.getString(R.string.error_occured)+  " "+e.message)

                }
            })
        } else {
            //if user is already authenticated direct call fetch twitter email api
            showSnackBar("User already authenticated")
            fetchUserDeatils()
        }
    }


    fun fetchTwitterEmail(twitterSession: TwitterSession) {
        client?.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>) {
                val user = result.data

                /*      Log.e("LoginTwitterAl" + "user id", twitterSession.userId.toString())
                      Log.e("LoginTwitterAl" + "name", twitterSession.userName)
                      Log.e("LoginTwitterAl" + "Email", result.data)*/

                fetchUserDeatils()

            }

            override fun failure(exception: TwitterException) {
                showSnackBar(resources.getString(R.string.error_occured)+  " "+exception.message)
            }
        })
    }


    fun fetchUserDeatils() {
        //check if user is already authenticated or not
        if (getTwitterSession() != null) {

            //initialize twitter api client
            val twitterApiClient = TwitterCore.getInstance().apiClient

            //pass includeEmail : true if you want to fetch Email as well
            val call = twitterApiClient.accountService.verifyCredentials(true, false, true)
            call.enqueue(object : Callback<User>() {
                override fun success(result: Result<User>) {
                    val user = result.data
                  /*  Log.e("LoginTwImage", "user id" + user.id.toString())
                    Log.e("LoginTwImage", "name" + user.name)
                    Log.e("LoginTwImage", "Email" + user.email)*/
                    val imageProfileUrl = user.profileImageUrl.replace("_normal", "")
//                    Log.e("LoginTwImage", "ProfileUrl $imageProfileUrl")
                    //Link : https://developer.twitter.com/en/docs/accounts-and-users/user-profile-images-and-banners


                    // hitting api for TWITTER
                    registerFragPresenter.twitterSigin(
                        user.email,
                        user.name,
                        imageProfileUrl,
                        sessionManager.getValue(SessionManager.FCM_TOKEN),
                        user.id.toString(),
                        sessionManager.getLangValue(SessionManager.LANGUAGE_HOME_ACTIVITY)
                    )

                }

                override fun failure(exception: TwitterException) {

                    showSnackBar(resources.getString(R.string.error_occured)+  " "+exception.message)

                }
            })
        } else {
            //if user is not authenticated first ask user to do authentication
            showSnackBar("Failed to authenticate. Please try again.")
        }

    }

    public fun getTwitterSession(): TwitterSession? {
        return TwitterCore.getInstance().sessionManager.activeSession
    }


    override fun onFacebookSiginDetails(result: Boolean, response: Response<SignUpModel>) {
        if (result == true) {

            var signUpModel = response.body() as SignUpModel
            if (signUpModel.status == 1) {
              /*  Log.e(
                    "respFacebook",
                    "" + response.body().toString()
                )*/
                // saving that user is already logged in
                sessionManager?.setValues(SessionManager.LOGGEDIN, "1")
                sessionManager.setValues(SessionManager.USERNAME, signUpModel.data.username)
                sessionManager.setValues(SessionManager.FIRST_NAME, signUpModel.data.first_name)
                sessionManager.setValues(SessionManager.LAST_NAME, signUpModel.data.last_name)
                sessionManager.setValues(SessionManager.EMAIL, signUpModel.data.email)
                sessionManager.setValues(SessionManager.USER_ID, signUpModel.data.id.toString())
                sessionManager.setValues(SessionManager.PHOTO_URL, signUpModel.data.photo)
                sessionManager.setValues(SessionManager.RADIUS, signUpModel.data.radius.toString()) // if new user, by default will be = 4
                sessionManager.setValues(SessionManager.LATITUDE, signUpModel.data.latitude.toString())
                sessionManager.setValues(SessionManager.LONGITUDE,  signUpModel.data.longitude.toString())
                sessionManager.setValues(SessionManager.LOCATION_SAVED,  signUpModel.data.radius_center_location.toString())
                sessionManager.setValues(SessionManager.IS_CURRENT_LOCATION_SELECTED,  signUpModel.data.isCurrentLocation.toString())

                sessionManager.setValues(SessionManager.LOGGEDIN_THROUGH, "1")



                val intent = Intent(mcontext, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                mcontext.startActivity(intent)

            } else {
                showSnackBar(signUpModel.message)
            }

        } else {
            showSnackBar(getResources().getString(R.string.error_occured) + "  ${response.code()}");
        }
    }

    override fun onTwitterSiginDetails(result: Boolean, response: Response<SignUpModel>) {
        if (result == true) {
            var signUpModel = response.body() as SignUpModel
            if (signUpModel.status == 1) {

// saving that user is already logged in
                sessionManager?.setValues(SessionManager.LOGGEDIN, "1")
                sessionManager.setValues(SessionManager.USERNAME, signUpModel.data.username)
                sessionManager.setValues(SessionManager.FIRST_NAME, signUpModel.data.first_name)
                sessionManager.setValues(SessionManager.LAST_NAME, signUpModel.data.last_name)
                sessionManager.setValues(SessionManager.EMAIL, signUpModel.data.email)
                sessionManager.setValues(SessionManager.USER_ID, signUpModel.data.id.toString())
                sessionManager.setValues(SessionManager.PHOTO_URL, signUpModel.data.photo)
                sessionManager.setValues(SessionManager.RADIUS, signUpModel.data.radius.toString()) // if new user, by default will be = 4

                sessionManager.setValues(SessionManager.LOGGEDIN_THROUGH, "1")
                sessionManager.setValues(SessionManager.LATITUDE, signUpModel.data.latitude.toString())
                sessionManager.setValues(SessionManager.LONGITUDE,  signUpModel.data.longitude.toString())
                sessionManager.setValues(SessionManager.LOCATION_SAVED,  signUpModel.data.radius_center_location.toString())
                sessionManager.setValues(SessionManager.IS_CURRENT_LOCATION_SELECTED,  signUpModel.data.isCurrentLocation.toString())

                val intent = Intent(mcontext, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                mcontext.startActivity(intent)
            } else {
                showSnackBar(signUpModel.message)
            }

        } else {
            showSnackBar(getResources().getString(R.string.error_occured) + "  ${response.code()}");
        }
    }

    fun firebaseToken() {
        //get firebase token
        //if not registered get token
        if (!registeredToFCM()) {
//            if (connectionDetector?.isConnectingToInternet!!) {

            FirebaseInstanceId.getInstance().instanceId
                .addOnSuccessListener(mcontext) { instanceIdResult ->
                    val token = instanceIdResult.token

//                    Log.d("LoginActvty fcm new ", "Refreshed token in log: $token")

                    //save in session manager device token
                    sessionManager.setValues(SessionManager.FCM_TOKEN, token)
                }


//            } else {
//                //utilities.showSnackBar(resources.getString(R.string.check_connection))
//
//            }

        } else {
            // Log.d("LogChoicActvty fcm old ", "Refreshed token in log: " + sessionManager.getValue((SessionManager.FCM_TOKEN)));

        }

    }

    private fun registeredToFCM(): Boolean {
        return if (sessionManager.getValue(SessionManager.FCM_TOKEN) != null && !TextUtils.isEmpty(
                sessionManager.getValue(
                    SessionManager.FCM_TOKEN
                )
            )
        ) {
            true
        } else {
            false
        }
    }

/*    fun  printHashKey() {
        try {
            val info = conxt.packageManager.getPackageInfo("com.dish.seekdish", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }*/

/*    fun generateSSHKey(context: Context){
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.getEncoder().encode(md.digest()))
                Log.i("AppLog", "key:$hashKey=")
            }
        } catch (e: Exception) {
            Log.e("AppLog", "error:", e)
        }

    }*/
}


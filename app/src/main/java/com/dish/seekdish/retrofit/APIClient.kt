package com.dish.seekdish.retrofit

import android.content.Context
import com.dish.seekdish.util.Global
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieHandler
import okhttp3.CookieJar
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import java.net.CookiePolicy.ACCEPT_ALL
import okhttp3.JavaNetCookieJar


internal object APIClient {
    private var retrofit: Retrofit? = null
    private val REQUEST_TIMEOUT = 60
    private var okHttpClient: OkHttpClient? = null

    fun getClient(context: Context): Retrofit {

        if (okHttpClient == null)
            initOkHttp(context)

        if (retrofit == null) {

         /* var cookjar=  okHttpClient!!.cookieJar()

            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

            val javaNetCookieJar = JavaNetCookieJar(cookieManager)
            cookjar.*/

            val jncj = JavaNetCookieJar(CookieHandler.getDefault())
            OkHttpClient.Builder()
                .cookieJar(jncj)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(Global.BASE_URL)
                .client(okHttpClient!!)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private fun initOkHttp(context: Context) {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()/* .addHeader("Accept", "application/x-www-form-urlencoded")*/
                .addHeader("Content-Type", "application/json")

            // Adding Authorization token (API Key)
            // Requests will be denied without API key
            /*    if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {
    requestBuilder.addHeader("Authorization", PrefUtils.getApiKey(context));
}*/
            //requestBuilder.addHeader("Authorization", "srishti");

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        okHttpClient = httpClient.build()
    }
}

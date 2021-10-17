package com.dish.seekdish.retrofit

import com.dish.seekdish.BuildConfig
import com.dish.seekdish.util.Global
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClientMvvm {


    companion object {

        var retofit: Retrofit? = null


        val client: Retrofit
            get() {

                val logging = HttpLoggingInterceptor()
                logging.level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

                val httpClient = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                httpClient.addInterceptor(logging)

                if (retofit == null) {
                    retofit = Retrofit.Builder()
                        .baseUrl(Global.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build()
                }
                return retofit!!
            }
    }
}
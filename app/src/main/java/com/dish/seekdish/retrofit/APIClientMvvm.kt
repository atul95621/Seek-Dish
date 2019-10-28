package com.dish.seekdish.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClientMvvm {


    companion object {

        val BASE_URL = "http://seekdish.com/seekdish_android/public/api/v1/"

        var retofit: Retrofit? = null

        val client: Retrofit
            get() {
                if (retofit == null) {
                    retofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retofit!!
            }
    }
}
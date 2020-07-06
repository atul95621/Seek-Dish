package com.dish.seekdish.retrofit

import com.dish.seekdish.util.Global
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClientMvvm {


    companion object {

        var retofit: Retrofit? = null

        val client: Retrofit
            get() {
                if (retofit == null) {
                    retofit = Retrofit.Builder()
                        .baseUrl(Global.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retofit!!
            }
    }
}
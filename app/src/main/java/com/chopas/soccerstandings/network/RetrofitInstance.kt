package com.chopas.soccerstandings.network

import com.chopas.soccerstandings.utils.APIUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofitInstance by lazy {
            Retrofit.Builder().baseUrl(APIUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
        }

        val api: Service by lazy {
            retrofitInstance.create(Service::class.java)
        }
    }
}
package com.v4u.networkcall

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Nehalkumar Maheshwari on 6/2/2019.
 */

object ApiClient {
    private var requestInterface: IRequestInterface? = null
    /**
     * Create retrofit instance
     * retrofit instance create IRequestInterface instance
     * IRequestInterface is used for calling rest api
     * @return IRequestInterface
     */
    fun getClient(headerMap: HashMap<String, String>): IRequestInterface? {

        if (requestInterface == null) {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor(headerMap))
                .connectTimeout(BuildConfig.CONNECT_TIMOUT, TimeUnit.MILLISECONDS)
                .readTimeout(BuildConfig.READ_TIMEOUT, TimeUnit.MILLISECONDS)

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()

            requestInterface = retrofit.create(IRequestInterface::class.java)
        }
        return requestInterface
    }
}
package com.v4u.networkcall

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * Created by Nehalkumar Maheshwari on 6/2/2019.
 */

class HeaderInterceptor(private val headerMap: HashMap<String, String>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val reqBuilder = request().newBuilder()
        headerMap.forEach {
            reqBuilder.addHeader(it.key, it.value)
        }
        proceed(reqBuilder.build())
    }
}
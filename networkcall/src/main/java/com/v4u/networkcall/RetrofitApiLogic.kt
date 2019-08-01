package com.v4u.networkcall

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

/**
 * Created by Nehalkumar Maheshwari on 6/2/2019.
 */

class RetrofitApiLogic(private val networkResponseListener: NetworkResponseListener) {

    /**
     * calling post api with request parameter
     * and return response using NetworkResponseListener
     * @param apiName is your url name. Easy to bifurcate which context calling which url
     * @param url is api name
     * @param jsonObject for request parameter
     * @param headerMap if you have to add your custom header then and only use this parameter
     */
    fun callingPostApi(apiName: String, url: String, jsonObject: JsonObject, headerMap: HashMap<String, String> =
        hashMapOf("Accept" to "application/json", "Content-Type" to "application/json")) {
        try {
            ApiClient.getClient(headerMap)!!.callingPostRequest(url, jsonObject).apply {
                enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        networkResponseListener.onFailureResponse(apiName, t.message)
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            networkResponseListener.onSuccessResponse(apiName, response.body().toString())
                        } else {
                            networkResponseListener.onErrorResponse(apiName, response.errorBody().toString())
                        }
                    }
                })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * calling get api
     * and return response using NetworkResponseListener
     * @param apiName is your url name. Easy to bifurcate which context calling which url
     * @param url is api name
     * @param headerMap if you have to add your custom header then and only use this parameter
     */
    fun callingGetApi(apiName: String, url: String, headerMap: HashMap<String, String> = hashMapOf(
            "Accept" to "application/json", "Content-Type" to "application/json")) {
        try {
            ApiClient.getClient(headerMap)!!.callingGetRequest(url).apply {
                enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        networkResponseListener.onFailureResponse(apiName, t.message)
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            networkResponseListener.onSuccessResponse(apiName, response.body().toString())
                        } else {
                            networkResponseListener.onErrorResponse(apiName, response.errorBody().toString())
                        }
                    }
                })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * calling put api with request parameter
     * and return response using NetworkResponseListener
     * @param apiName is your url name. Easy to bifurcate which context calling which url
     * @param url is api name
     * @param jsonObject for request parameter
     * @param headerMap if you have to add your custom header then and only use this parameter
     */
    fun callingPutApi(apiName: String, url: String, jsonObject: JsonObject, headerMap: HashMap<String, String> =
            hashMapOf("Accept" to "application/json", "Content-Type" to "application/json")) {
        try {
            ApiClient.getClient(headerMap)!!.callingPutRequest(url, jsonObject).apply {
                enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        networkResponseListener.onFailureResponse(apiName, t.message)
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            networkResponseListener.onSuccessResponse(apiName, response.body().toString())
                        } else {
                            networkResponseListener.onErrorResponse(apiName, response.errorBody().toString())
                        }
                    }
                })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * calling delete api with request parameter
     * and return response using NetworkResponseListener
     * @param apiName is your url name. Easy to bifurcate which context calling which url
     * @param url is api name
     * @param jsonObject for request parameter
     * @param headerMap if you have to add your custom header then and only use this parameter
     */
    fun callingDeleteApi(apiName: String, url: String, jsonObject: JsonObject, headerMap: HashMap<String, String> =
        hashMapOf("Accept" to "application/json", "Content-Type" to "application/json")) {
        try {
            ApiClient.getClient(headerMap)!!.callingDeleteRequest(url, jsonObject).apply {
                enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        networkResponseListener.onFailureResponse(apiName, t.message)
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            networkResponseListener.onSuccessResponse(apiName, response.body().toString())
                        } else {
                            networkResponseListener.onErrorResponse(apiName, response.errorBody().toString())
                        }
                    }
                })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * calling delete api
     * and return response using NetworkResponseListener
     * @param apiName is your url name. Easy to bifurcate which context calling which url
     * @param url is api name
     * @param headerMap if you have to add your custom header then and only use this parameter
     */
    fun callingDeleteApi(apiName: String, url: String, headerMap: HashMap<String, String> = hashMapOf(
        "Accept" to "application/json", "Content-Type" to "application/json")) {
        try {
            ApiClient.getClient(headerMap)!!.callingDeleteRequest(url).apply {
                enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        networkResponseListener.onFailureResponse(apiName, t.message)
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            networkResponseListener.onSuccessResponse(apiName, response.body().toString())
                        } else {
                            networkResponseListener.onErrorResponse(apiName, response.errorBody().toString())
                        }
                    }
                })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}


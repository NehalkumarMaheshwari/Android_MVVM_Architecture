package com.v4u.networkcall

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Nehalkumar Maheshwari on 6/2/2019.
 */

interface IRequestInterface {

    @POST
    fun callingPostRequest(@Url url: String, @Body request: JsonObject): Call<JsonObject>

    @GET
    fun callingGetRequest(@Url url: String): Call<JsonObject>

    @PUT
    fun callingPutRequest(@Url url: String, @Body request: JsonObject): Call<JsonObject>

    @DELETE
    fun callingDeleteRequest(@Url url: String) : Call<JsonObject>

    @DELETE
    fun callingDeleteRequest(@Url url: String, @Body request: JsonObject) : Call<JsonObject>


}
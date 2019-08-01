package com.v4u.networkcall

/**
 * Created by Nehalkumar Maheshwari on 6/2/2019.
 */

interface NetworkResponseListener {
    fun onSuccessResponse(apiName: String, response: String)
    fun onErrorResponse(apiName: String, response: String)
    fun onFailureResponse(apiName: String, message: String?)
}
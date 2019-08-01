package com.v4u.mvvm.signup.model

import com.google.gson.annotations.SerializedName

data class SignupModel(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("token")
	val token: String? = null
)
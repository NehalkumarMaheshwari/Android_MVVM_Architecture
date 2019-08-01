package com.v4u.mvvm.login.viewmodel

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.v4u.mvvm.BuildConfig
import com.v4u.mvvm.R
import com.v4u.mvvm.login.model.LoginModel
import com.v4u.mvvm.signup.SignUpActivity
import com.v4u.networkcall.NetworkResponseListener
import com.v4u.networkcall.RetrofitApiLogic
import com.v4u.utils.fromJson
import com.v4u.utils.isValidEmail
import com.v4u.utils.isValidPassword
import com.v4u.utils.toast
import org.json.JSONObject

class LoginViewModel(application: Application) : AndroidViewModel(application), NetworkResponseListener {

    private val context = getApplication<Application>().applicationContext

    var emailId: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var emailIdError: ObservableField<String> = ObservableField("")
    var passwordError: ObservableField<String> = ObservableField("")
    var isProgressVisible: ObservableField<Int> = ObservableField(View.GONE)
    var isOutsideClickable: ObservableField<Boolean> = ObservableField(false)

    var userLiveData: MutableLiveData<LoginModel> = MutableLiveData()

    companion object {
        @JvmStatic
        @BindingAdapter("error")
        fun setError(textInputLayout: TextInputLayout, error: String) {
            textInputLayout.error = error
        }
    }

    fun signUpClick() {
        val intent = Intent(context, SignUpActivity::class.java)
        context.startActivity(intent)
    }

    fun loginApiCalling() {
        when {
            !emailId.get().toString().isValidEmail() -> emailIdError.set(context.resources.getString(R.string.error_email))
            !password.get().toString().isValidPassword(
                forceLetter = true, forceSpecialChar = true,
                forceCapitalLetter = true, forceNumber = true, minLength = 6, maxLength = 15
            ) -> {
                emailIdError.set("")
                passwordError.set(context.resources.getString(R.string.error_password))
            }
            else -> {
                emailIdError.set("")
                passwordError.set("")

                isProgressVisible.set(View.VISIBLE)
                isOutsideClickable.set(true)

                val jsonObject = JsonObject().apply {
                    addProperty("email", "eve.holt@reqres.in")
                    addProperty("password", "cityslicka")
                }

                RetrofitApiLogic(this).callingPostApi(
                    apiName = "Login",
                    url = BuildConfig.BASE_URL + "login", jsonObject = jsonObject
                )
            }
        }
    }

    override fun onSuccessResponse(apiName: String, response: String) {
        if (apiName.equals("Login", true)) {
            userLiveData.value = response.fromJson(LoginModel::class.java)
            isProgressVisible.set(View.GONE)
            isOutsideClickable.set(false)
        }
    }

    override fun onErrorResponse(apiName: String, response: String) {
        if (apiName.equals("Login", true)) {
            val jsonObject = JSONObject(response)
            context.toast(jsonObject.optString("message"))
            isProgressVisible.set(View.GONE)
            isOutsideClickable.set(false)
        }
    }

    override fun onFailureResponse(apiName: String, message: String?) {
        context.toast(message.toString())
        isProgressVisible.set(View.GONE)
        isOutsideClickable.set(false)
    }

    fun getLoginUser(): MutableLiveData<LoginModel> {
        return userLiveData
    }
}
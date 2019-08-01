package com.v4u.mvvm.signup.viewmodel

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.v4u.mvvm.BuildConfig
import com.v4u.mvvm.login.LoginActivity
import com.v4u.mvvm.signup.model.SignupModel
import com.v4u.networkcall.NetworkResponseListener
import com.v4u.networkcall.RetrofitApiLogic
import com.v4u.utils.*
import org.json.JSONObject

class SignUpViewModel (application: Application) : AndroidViewModel(application), NetworkResponseListener {

    private val context = getApplication<Application>().applicationContext

    var firstName: ObservableField<String> = ObservableField("")
    var lastName: ObservableField<String> = ObservableField("")
    var mobile: ObservableField<String> = ObservableField("")
    var emailId: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var retypePassword: ObservableField<String> = ObservableField("")
    var isProgressVisible: ObservableField<Int> = ObservableField(View.GONE)
    var isOutsideClickable: ObservableField<Boolean> = ObservableField(false)

    var userLiveData: MutableLiveData<SignupModel> = MutableLiveData()

    fun loginClick() {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    fun signUpClick() {
        when {
            firstName.get()!!.isEmpty() -> context.toast("Please enter your first name...!")
            lastName.get()!!.isEmpty() -> context.toast("Please enter your last name...!")
            !emailId.get().toString().isValidEmail() -> context.toast("Please enter your valid email address...!")
            !mobile.get()!!.isValidPhoneNumber() -> context.toast("Please enter your valid mobile number...!")
            !password.get().toString().isValidPassword(forceLetter = true, forceSpecialChar = true,
                forceCapitalLetter = true, forceNumber = true, minLength = 6, maxLength = 15) -> {
                context.toast(
                    "Please enter your valid password with one capital letter, " +
                            "one special symbol, one digit, and length is 5 to 15 character...!")
            }
            retypePassword.get().toString() != password.get().toString() -> context.toast("Your retype password is not match with your password...!")
            else -> {
                isProgressVisible.set(View.VISIBLE)
                isOutsideClickable.set(true)

                val jsonObject = JsonObject().apply {
                    addProperty("email", "eve.holt@reqres.in")
                    addProperty("password", "pistol")
                }

                RetrofitApiLogic(this).callingPostApi(apiName = "Register",
                    url = BuildConfig.BASE_URL+"register", jsonObject = jsonObject)
            }
        }
    }

    override fun onSuccessResponse(apiName: String, response: String) {
        if(apiName.equals("Register", true)){
            userLiveData.value = response.fromJson(SignupModel::class.java)
            isProgressVisible.set(View.GONE)
            isOutsideClickable.set(false)
        }
    }

    override fun onErrorResponse(apiName: String, response: String) {
        if(apiName.equals("Register", true)){
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

    fun getRegisterUser(): MutableLiveData<SignupModel> {
        return userLiveData
    }
}
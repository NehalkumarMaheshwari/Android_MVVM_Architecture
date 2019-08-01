package com.v4u.mvvm.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.v4u.mvvm.R
import com.v4u.mvvm.databinding.ActivitySignupBinding
import com.v4u.mvvm.login.LoginActivity
import com.v4u.mvvm.signup.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        val viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.getRegisterUser().observe(this, Observer {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}
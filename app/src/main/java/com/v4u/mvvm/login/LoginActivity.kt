package com.v4u.mvvm.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.v4u.mvvm.R
import com.v4u.mvvm.databinding.ActivityLoginBinding
import com.v4u.mvvm.listing.view.UserListingActivity
import com.v4u.mvvm.login.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.getLoginUser().observe(this, Observer {
            val intent = Intent(this@LoginActivity, UserListingActivity::class.java)
            startActivity(intent)
        })
    }
}
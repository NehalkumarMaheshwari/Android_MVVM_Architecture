package com.v4u.mvvm.listing.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.v4u.mvvm.R
import com.v4u.mvvm.databinding.ActivityUserlistingBinding
import com.v4u.mvvm.listing.viewmodel.UserListViewModel
import com.v4u.utils.toast

class UserListingActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, UserListingActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityUserlistingBinding = DataBindingUtil.setContentView(this, R.layout.activity_userlisting)
        val viewModel = ViewModelProviders.of(this).get(UserListViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.fetchUserList()

        viewModel.getSelectedUserData().observe(this, Observer {
            this.toast(it.firstName.toString() + " " + it.lastName.toString())
        })
    }
}

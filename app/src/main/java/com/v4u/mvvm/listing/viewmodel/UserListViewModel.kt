package com.v4u.mvvm.listing.viewmodel

import android.app.Application
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.v4u.mvvm.BuildConfig
import com.v4u.mvvm.R
import com.v4u.mvvm.listing.model.UserData
import com.v4u.mvvm.listing.model.UserList
import com.v4u.mvvm.listing.view.UserListingAdapter
import com.v4u.networkcall.NetworkResponseListener
import com.v4u.networkcall.RetrofitApiLogic
import com.v4u.utils.fromJson
import com.v4u.utils.toast
import org.json.JSONObject


class UserListViewModel(application: Application) : AndroidViewModel(application), NetworkResponseListener {

    private val context = getApplication<Application>().applicationContext

    var userDataList: ArrayList<UserData> = ArrayList()
    private var userList: UserList = UserList()
    private val adapter: UserListingAdapter = UserListingAdapter(R.layout.view_user_list, this)
    var userData: MutableLiveData<UserData> = MutableLiveData()
    var loading: ObservableField<Int> = ObservableField(View.GONE)
    var showEmpty: ObservableField<Int> = ObservableField(View.GONE)


    companion object {
        @JvmStatic
        @BindingAdapter("setAdapter")
        fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = adapter
        }

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun bindRecyclerViewAdapter(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView).load(imageUrl).into(imageView)
        }
    }

    fun getAdapter(): UserListingAdapter {
        return adapter
    }

    fun getSelectedUserData(): MutableLiveData<UserData> {
        return userData
    }

    fun onItemClick(position: Int) {
        userData.value = userList.data!![position]
    }

    fun fetchUserList() {

        loading.set(View.VISIBLE)

        RetrofitApiLogic(this).callingGetApi(
            apiName = "UserList",
            url = BuildConfig.BASE_URL + "users?page=2")
    }

    fun getUserData(position: Int): UserData? {
        return userList.data!![position]
    }

    override fun onSuccessResponse(apiName: String, response: String) {
        if (apiName.equals("UserList", true)) {
            userList = response.fromJson(UserList::class.java)
            if (userList.data!!.isNotEmpty()) {
                userDataList.addAll(userList.data as ArrayList<UserData>)

                adapter.setUserList(userDataList)
                adapter.notifyDataSetChanged()
                showEmpty.set(View.GONE)
            } else {
                showEmpty.set(View.VISIBLE)
            }

            loading.set(View.GONE)
        }
    }

    override fun onErrorResponse(apiName: String, response: String) {
        if (apiName.equals("UserList", true)) {
            val jsonObject = JSONObject(response)
            context.toast(jsonObject.optString("message"))
            showEmpty.set(View.VISIBLE)
            loading.set(View.GONE)
        }
    }

    override fun onFailureResponse(apiName: String, message: String?) {
        context.toast(message.toString())
        loading.set(View.GONE)
        showEmpty.set(View.VISIBLE)
    }
}
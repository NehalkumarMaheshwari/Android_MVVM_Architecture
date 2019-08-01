package com.v4u.mvvm.listing.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.v4u.mvvm.BR
import com.v4u.mvvm.listing.model.UserData
import com.v4u.mvvm.listing.viewmodel.UserListViewModel


class UserListingAdapter(@LayoutRes val layoutId: Int, private val userViewModel: UserListViewModel) :
    RecyclerView.Adapter<UserListingAdapter.UserListViewHolder>() {

    private var userData: ArrayList<UserData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, viewType,
            parent, false
        )

        return UserListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userData.size
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(userViewModel, position)
    }

    private fun getLayoutIdForPosition(position: Int): Int {
        return layoutId
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    fun setUserList(userData: ArrayList<UserData>) {
        this.userData.addAll(userData)
        notifyDataSetChanged()
    }

    class UserListViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userViewModel: UserListViewModel, position: Int) {
            binding.setVariable(BR.viewModel, userViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}
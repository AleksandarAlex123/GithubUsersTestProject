package com.aleksandar.userstasktest.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksandar.userstasktest.data.remote.GitHubApi
import com.aleksandar.userstasktest.data.repository.UserRepository
import com.aleksandar.userstasktest.utils.NetworkConnectivityObserver

class ViewModelFactory(
    private val api: GitHubApi,
    private val userRepository: UserRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(api, userRepository, networkConnectivityObserver) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

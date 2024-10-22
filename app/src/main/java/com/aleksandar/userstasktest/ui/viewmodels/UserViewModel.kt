package com.aleksandar.userstasktest.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandar.userstasktest.data.model.User
import com.aleksandar.userstasktest.data.remote.GitHubApi
import com.aleksandar.userstasktest.data.repository.UserRepository
import com.aleksandar.userstasktest.utils.NetworkConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val api: GitHubApi,
    private val userRepository: UserRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        viewModelScope.launch {
            networkConnectivityObserver.isConnected.collect { isConnected ->
                if (isConnected) {
                    fetchUsers()
                }
            }
        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            if (networkConnectivityObserver.hasInternetConnection()) {
                fetchUsersFromApi()
            } else {
                fetchUsersFromDatabase()
            }
        }
    }

    private suspend fun fetchUsersFromApi() {
        _isLoading.value = true
        try {
            val userList = api.getUsers()
            _users.value = userList
            saveUsersToDatabase(userList)
            _isLoading.value = false
        } catch (e: Exception) {
            Log.i("ttd", "1 c: ")
            handleError(true)
            _isLoading.value = false
        }
    }

    private suspend fun fetchUsersFromDatabase() {
        val usersFromDb = userRepository.getAllUsers()
        if (usersFromDb.isNotEmpty()) {
            _users.value = usersFromDb
            _isLoading.value = false
        } else {
            _error.value = SEARCH_NOT_WORKING_MESSAGE
            _isLoading.value = false
            Log.i("ttd", "2 c: ")
            handleError(true)
        }
    }

    private fun saveUsersToDatabase(userList: List<User>) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.insert(userList)
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "saveUsersToDatabase error: " + e.message)
        }
    }

    private fun handleError(usersListEmpty: Boolean) {
        if (usersListEmpty) {
            _error.value = SEARCH_NOT_WORKING_MESSAGE
        }
    }

    companion object {
        private const val SEARCH_NOT_WORKING_MESSAGE = "Search will not work"
    }
}

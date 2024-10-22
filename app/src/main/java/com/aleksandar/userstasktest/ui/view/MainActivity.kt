package com.aleksandar.userstasktest.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.aleksandar.userstasktest.UserApplication
import com.aleksandar.userstasktest.data.remote.RetrofitInstance
import com.aleksandar.userstasktest.ui.theme.UsersTaskTestTheme
import com.aleksandar.userstasktest.ui.viewmodels.UserViewModel
import com.aleksandar.userstasktest.ui.viewmodels.ViewModelFactory
import com.aleksandar.userstasktest.utils.NetworkConnectivityObserver

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory(
            RetrofitInstance.api,
            (application as UserApplication).userRepository,
            NetworkConnectivityObserver(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UsersTaskTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserList(
                        modifier = Modifier.padding(innerPadding),
                        userViewModel = userViewModel
                    )
                }
            }
        }
        userViewModel.fetchUsers()
    }
}

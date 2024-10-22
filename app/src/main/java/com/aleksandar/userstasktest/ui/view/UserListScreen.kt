package com.aleksandar.userstasktest.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.aleksandar.userstasktest.R
import com.aleksandar.userstasktest.data.model.User
import com.aleksandar.userstasktest.ui.viewmodels.UserViewModel

@Composable
fun UserList(
    modifier: Modifier,
    userViewModel: UserViewModel
) {
    val users by userViewModel.users.collectAsState(initial = emptyList())
    val error by userViewModel.error.collectAsState(initial = null)
    val isLoading by userViewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = users.filter { user ->
        user.login.contains(searchQuery, ignoreCase = true)
    }

    val context = LocalContext.current

    error?.let { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    Column(modifier = modifier) {
        // Search Field
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(stringResource(R.string.search_by_nickname)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = Color.Black)
        )

        // Loading Indicator
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (filteredUsers.isEmpty() && error == null) {
            Text(
                text = stringResource(R.string.no_users_found),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // List of users
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filteredUsers) { user ->
                    UserItem(user = user)
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = Color.Black)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(
                data = user.avatarUrl,
                builder = {
                    placeholder(R.drawable.ic_avatar_placeholder)
                    error(R.drawable.ic_avatar_placeholder)
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 12.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = user.login,
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

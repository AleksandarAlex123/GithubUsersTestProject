package com.aleksandar.userstasktest.data.remote

import com.aleksandar.userstasktest.data.model.User
import retrofit2.http.GET

interface GitHubApi {
    @GET("users")
    suspend fun getUsers(): List<User>
}

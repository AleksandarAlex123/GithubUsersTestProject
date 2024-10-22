package com.aleksandar.userstasktest.data.repository

import com.aleksandar.userstasktest.data.db.UserDao
import com.aleksandar.userstasktest.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(users: List<User>) {
        withContext(Dispatchers.IO) {
            userDao.insertAll(users)
        }
    }

    suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            userDao.getAllUsers()
        }
    }
}

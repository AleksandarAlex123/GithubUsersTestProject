package com.aleksandar.userstasktest

import android.app.Application
import com.aleksandar.userstasktest.data.db.AppDatabase
import com.aleksandar.userstasktest.data.repository.UserRepository

class UserApplication : Application() {
    lateinit var userRepository: UserRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())
    }
}

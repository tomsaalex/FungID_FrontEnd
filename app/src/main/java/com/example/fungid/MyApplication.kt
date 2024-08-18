package com.example.fungid

import android.app.Application
import android.util.Log
import com.example.fungid.core.data.AppContainer
import com.example.fungid.util.TAG

class MyApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "init")
        container = AppContainer(this)
    }
}
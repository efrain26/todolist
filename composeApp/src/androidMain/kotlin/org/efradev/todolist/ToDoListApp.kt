package org.efradev.todolist

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.efradev.todolist.di.appModule
import org.efradev.todolist.di.viewModelModule

class ToDoListApp: Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidPlatform.init(this)

        startKoin {
            androidLogger()
            androidContext(this@ToDoListApp)
            modules(
                appModule,
                viewModelModule
            )
        }
    }
}
package net.ohnaoki.habitreminder

import android.app.Application
import di.KoinKMPStarter
import org.koin.android.ext.koin.androidContext

class HabitApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinKMPStarter().init {
            androidContext(this@HabitApp)
        }
    }
}

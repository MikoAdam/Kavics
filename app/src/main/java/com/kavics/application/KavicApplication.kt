package com.kavics.application

import android.app.Application
import androidx.room.Room
import com.kavics.database.KavicDatabase

class KavicApplication : Application() {

    companion object {
        lateinit var kavicDatabase: KavicDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        kavicDatabase = Room.databaseBuilder(
            applicationContext,
            KavicDatabase::class.java,
            "kavic_database"
        ).build()
    }

}
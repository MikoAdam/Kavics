package com.kavics.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kavics.model.Kavic

@Database(entities = [Kavic::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val kavicDAO: KavicDAO?
}

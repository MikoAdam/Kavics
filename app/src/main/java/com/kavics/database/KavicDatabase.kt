package com.kavics.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomKavic::class], version = 1)
abstract class KavicDatabase : RoomDatabase() {
    abstract fun kavicDAO(): KavicDAO
}

package com.kavics.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kavics.model.KavicItem

@Database(entities = [KavicItem::class], version = 1)
abstract class KavicDatabase : RoomDatabase() {
    abstract fun kavicDAO(): KavicDAO
}

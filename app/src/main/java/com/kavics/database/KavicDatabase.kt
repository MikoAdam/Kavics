package com.kavics.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem

@Database(
    version = 1,
    exportSchema = false,
    entities = [OneTimeKavicItem::class, RepeatingKavicItem::class]
)
abstract class KavicDatabase : RoomDatabase() {

    abstract fun kavicDAO(): KavicDAO

}

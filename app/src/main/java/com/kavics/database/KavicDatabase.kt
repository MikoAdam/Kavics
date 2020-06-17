package com.kavics.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kavics.database.one_time_kavics.OneTimeKavicDAO
import com.kavics.database.repeating_kavics.RepeatingKavicDAO
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem

@Database(entities = [OneTimeKavicItem::class, RepeatingKavicItem::class], version = 1)
abstract class KavicDatabase : RoomDatabase() {

    companion object {
        fun getDatabase(applicationContext: Context): KavicDatabase {
            return Room.databaseBuilder(
                applicationContext,
                KavicDatabase::class.java,
                "repeating_kavics"
            ).build()
        }
    }

    abstract fun repeatingKavicItemDao(): RepeatingKavicDAO
    abstract fun oneTimeKavicDAO(): OneTimeKavicDAO
}

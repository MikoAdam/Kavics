package com.kavics.database.repeating_kavics

import androidx.room.*
import com.kavics.model.RepeatingKavicItem

@Dao
interface RepeatingKavicDAO {

    @Query("SELECT * FROM repeatingKavicsDatabase")
    suspend fun getAll(): List<RepeatingKavicItem>

    @Insert
    suspend fun insert(repeatingKavicItem: RepeatingKavicItem)

    @Update
    suspend fun update(repeatingKavicItem: RepeatingKavicItem)

    @Delete
    suspend fun delete(repeatingKavicItem: RepeatingKavicItem)

}
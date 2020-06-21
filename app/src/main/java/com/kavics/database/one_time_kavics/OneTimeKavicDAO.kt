package com.kavics.database.one_time_kavics

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kavics.model.OneTimeKavicItem

@Dao
interface OneTimeKavicDAO {

    @Insert
    fun insert(oneTimeKavicItem: OneTimeKavicItem)

    @Update
    fun update(oneTimeKavicItem: OneTimeKavicItem)

    @Delete
    fun delete(oneTimeKavicItem: OneTimeKavicItem)

    @Query("SELECT * FROM kavicsDatabase ORDER BY deadline")
    fun getKavicsAll(): LiveData<List<OneTimeKavicItem>>

    @Query("SELECT * FROM kavicsDatabase WHERE done = 0 AND isArchive = 0 ORDER BY deadline")
    fun getKavics(): LiveData<List<OneTimeKavicItem>>

    @Query("SELECT * FROM kavicsDatabase WHERE id = :id")
    fun getKavicsById(id: Int): OneTimeKavicItem?

    @Query("SELECT * FROM kavicsDatabase WHERE done = 0")
    fun getNotDoneKavics(): List<OneTimeKavicItem?>?

    @Query("Update kavicsDatabase SET done = 1 WHERE id = :id")
    fun setDoneKavics(id: Int)

    @Query("UPDATE kavicsDatabase SET isArchive = 1 WHERE deadline <= :today")
    fun setArchiveAllOfOneTimeKavics(today: String)

}

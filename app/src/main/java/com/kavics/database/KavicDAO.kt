package com.kavics.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem

@Dao
interface KavicDAO {

    @Insert
    fun insert(oneTimeKavicItem: OneTimeKavicItem)

    @Update
    fun update(oneTimeKavicItem: OneTimeKavicItem)

    @Delete
    fun delete(oneTimeKavicItem: OneTimeKavicItem)

    @Query("SELECT * FROM kavicsDatabase")
    fun getKavicsAll(): LiveData<List<OneTimeKavicItem>>

    //@Query("SELECT * FROM kavicsDatabase ORDER BY deadline")
    //fun getKavicsAll(): LiveData<List<OneTimeKavicItem>>

    @Query("SELECT * FROM kavicsDatabase WHERE done = 0 AND isArchive = 1")
    fun getArchiveKavics(): LiveData<List<OneTimeKavicItem>>

    @Query("SELECT * FROM kavicsDatabase WHERE done = 0 AND isArchive = 0")
    fun getKavics(): LiveData<List<OneTimeKavicItem>>

    @Query("SELECT * FROM kavicsDatabase WHERE id = :id")
    fun getKavicsById(id: Int): OneTimeKavicItem?

    @Query("SELECT * FROM kavicsDatabase WHERE done = 0")
    fun getNotDoneKavics(): List<OneTimeKavicItem?>?

    @Query("Update kavicsDatabase SET done = 1 WHERE id = :id")
    fun setDoneKavics(id: Int)

    //@Query("UPDATE kavicsDatabase SET isArchive = 1 WHERE deadline < :today")
    //fun setArchiveAllOfOneTimeKavics(today: String)

    @Query("SELECT * FROM repeatingKavicsDatabase")
    suspend fun getAllRepeatingKavics(): List<RepeatingKavicItem>

    @Insert
    suspend fun insert(repeatingKavicItem: RepeatingKavicItem)

    @Update
    suspend fun update(repeatingKavicItem: RepeatingKavicItem)

    @Delete
    suspend fun delete(repeatingKavicItem: RepeatingKavicItem)


}

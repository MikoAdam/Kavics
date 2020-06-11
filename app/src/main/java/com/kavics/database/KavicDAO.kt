package com.kavics.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kavics.model.KavicItem

@Dao
interface KavicDAO {
    @Insert
    fun insert(kavicItem: KavicItem)

    @Update
    fun update(kavicItem: KavicItem)

    @Delete
    fun delete(kavicItem: KavicItem)

    @Query("SELECT * FROM kavicsDatabase ORDER BY deadline")
    fun getKavicsAll(): LiveData<List<KavicItem>>

    @Query("SELECT * FROM kavicsDatabase WHERE done = 0 ORDER BY deadline")
    fun getKavics(): LiveData<List<KavicItem>>

    @Query("SELECT * FROM kavicsDatabase WHERE id = :id")
    fun getKavicsById(id: Int): KavicItem?

    @Query("SELECT * FROM kavicsDatabase WHERE done = 0")
    fun getNotDoneKavics(): List<KavicItem?>?

    @Query("Update kavicsDatabase set done = 1 WHERE id = :id")
    fun setDoneKavics(id: Int)

    @Query("DELETE FROM kavicsDatabase WHERE deadline <= :today")
    fun deleteOld(today: String)

}

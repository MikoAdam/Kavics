package com.kavics.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KavicDAO {
    @Insert
    fun insert(kavic: RoomKavic)

    @Update
    fun update(kavic: RoomKavic)

    @Delete
    fun delete(kavic: RoomKavic)

    @Query("SELECT * FROM Kavics")
    fun getKavics(): LiveData<List<RoomKavic?>>

    @Query("SELECT * FROM Kavics WHERE id = :id")
    fun getKavicsById(id: Int): RoomKavic?

    @Query("SELECT * FROM Kavics WHERE done = 0")
    fun getNotDoneKavics(): List<RoomKavic?>?

    @Query("Update Kavics set done = 1 WHERE id = :id")
    fun setDoneKavics(id: Int)
}

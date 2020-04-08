package com.kavics.database

import androidx.room.*
import com.kavics.model.Kavic

@Dao
interface KavicDAO {
    @Insert
    fun insert(kavic: Kavic)

    @Update
    fun update(kavic: Kavic)

    @Delete
    fun delete(kavic: Kavic)

    @Query("SELECT * FROM Kavics")
    fun getKavics(): List<Kavic?>?

    @Query("SELECT * FROM Kavics WHERE id = :id")
    fun getKavicsById(id: Int): Kavic?

    @Query("SELECT * FROM Kavics WHERE done = 0")
    fun getNotDoneKavics(): List<Kavic?>?

    @Query("Update Kavics set done = 1 WHERE id = :id")
    fun setDoneKavics(id: Int)
}

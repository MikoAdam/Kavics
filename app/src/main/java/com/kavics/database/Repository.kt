package com.kavics.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kavics.model.Kavic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val kavicDAO: KavicDAO) {

    //transform RoomKavic to Kavic
    fun getAllKavics(): LiveData<List<Kavic>> {
        return kavicDAO.getKavics()
            .map { RoomKavics ->
                RoomKavics.map { RoomKavic ->
                    RoomKavic!!.toDomainModel()
                }
            }
    }

    // insert with corutine
    suspend fun insert(kavic: Kavic) = withContext(Dispatchers.IO) {
        kavicDAO.insert(kavic.toRoomModel())
    }

    suspend fun delete(kavic: Kavic) = withContext(Dispatchers.IO) {
        val roomKavic = kavicDAO.getKavicsById(kavic.id) ?: return@withContext
        kavicDAO.delete(roomKavic)
    }

    //extension function: from RoomKavic to Kavic
    private fun RoomKavic.toDomainModel(): Kavic {
        return Kavic(
            id = id,
            title = title,
            dueDate = dueDate,
            labels = labels,
            done = done,
            now = now,
            description = description
        )
    }

    //extension function: from Kavic to RoomKavic
    private fun Kavic.toRoomModel(): RoomKavic {
        return RoomKavic(
            id = id,
            title = title,
            dueDate = dueDate,
            labels = labels,
            done = done,
            now = now,
            description = description
        )
    }
}
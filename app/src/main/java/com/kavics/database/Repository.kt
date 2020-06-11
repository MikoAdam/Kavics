package com.kavics.database

import androidx.lifecycle.LiveData
import com.kavics.model.KavicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val kavicDAO: KavicDAO) {

    fun getAllKavics(): LiveData<List<KavicItem>> {
        return kavicDAO.getKavics()
    }


    suspend fun insert(kavicItem: KavicItem) = withContext(Dispatchers.IO) {
        kavicDAO.insert(kavicItem)
    }

    suspend fun delete(kavicItem: KavicItem) = withContext(Dispatchers.IO) {
        val roomKavic = kavicDAO.getKavicsById(kavicItem.id) ?: return@withContext
        kavicDAO.delete(roomKavic)
    }

    suspend fun update(kavicItem: KavicItem) = withContext(Dispatchers.IO) {
        kavicDAO.update(kavicItem)
    }

    fun deleteOld(today: String) {
        kavicDAO.deleteOld(today)
    }

}
package com.kavics.database

import androidx.lifecycle.LiveData
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryOneTimeKavic(private val kavicDAO: KavicDAO) {

    fun getAllKavics(): LiveData<List<OneTimeKavicItem>> {
        return kavicDAO.getKavics()
    }

    fun getAllArchiveKavics(): LiveData<List<OneTimeKavicItem>> {
        return kavicDAO.getArchiveKavics()
    }

    suspend fun insertOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) =
        withContext(Dispatchers.IO) {
            kavicDAO.insert(oneTimeKavicItem)
        }

    suspend fun deleteOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) =
        withContext(Dispatchers.IO) {
            val roomKavic = kavicDAO.getKavicsById(oneTimeKavicItem.id) ?: return@withContext
            kavicDAO.delete(roomKavic)
        }

    suspend fun updateOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) =
        withContext(Dispatchers.IO) {
            kavicDAO.update(oneTimeKavicItem)
        }

    /*suspend fun setArchiveAllOfOneTimeKavics(today: String) = withContext(Dispatchers.IO) {
        kavicDAO.setArchiveAllOfOneTimeKavics(today)
    }*/

    suspend fun getAllRepeatingKavics(): List<RepeatingKavicItem> =
        withContext(Dispatchers.IO) {
            return@withContext kavicDAO.getAllRepeatingKavics()
        }

    suspend fun insertRepeatingKavic(repeatingKavicItem: RepeatingKavicItem) =
        withContext(Dispatchers.IO) {
            kavicDAO.insert(repeatingKavicItem)
        }

}
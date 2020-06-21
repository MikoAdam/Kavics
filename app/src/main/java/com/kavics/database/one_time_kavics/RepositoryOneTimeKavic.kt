package com.kavics.database.one_time_kavics

import androidx.lifecycle.LiveData
import com.kavics.model.OneTimeKavicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryOneTimeKavic(private val oneTimeKavicDAO: OneTimeKavicDAO) {

    fun getAllKavics(): LiveData<List<OneTimeKavicItem>> {
        return oneTimeKavicDAO.getKavics()
    }

    suspend fun insertOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) =
        withContext(Dispatchers.IO) {
            oneTimeKavicDAO.insert(oneTimeKavicItem)
        }

    suspend fun deleteOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) =
        withContext(Dispatchers.IO) {
            val roomKavic = oneTimeKavicDAO.getKavicsById(oneTimeKavicItem.id) ?: return@withContext
            oneTimeKavicDAO.delete(roomKavic)
        }

    suspend fun updateOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) =
        withContext(Dispatchers.IO) {
            oneTimeKavicDAO.update(oneTimeKavicItem)
        }

    suspend fun setArchiveAllOfOneTimeKavics(today: String) = withContext(Dispatchers.IO) {
        oneTimeKavicDAO.setArchiveAllOfOneTimeKavics(today)
    }

}
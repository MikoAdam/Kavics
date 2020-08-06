package com.kavics.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavics.application.KavicApplication
import com.kavics.database.RepositoryOneTimeKavic
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem
import kotlinx.coroutines.launch

class KavicViewModel : ViewModel() {

    private val repositoryOneTimeKavic: RepositoryOneTimeKavic

    var allOneTimeKavics: LiveData<List<OneTimeKavicItem>>
    var allArchiveOneTimeKavics: LiveData<List<OneTimeKavicItem>>

    init {
        val oneTimeKavicDao = KavicApplication.kavicDatabase.kavicDAO()
        repositoryOneTimeKavic =
            RepositoryOneTimeKavic(oneTimeKavicDao)
        allOneTimeKavics = repositoryOneTimeKavic.getAllKavics()

        allArchiveOneTimeKavics = repositoryOneTimeKavic.getAllArchiveKavics()

    }

    fun insertOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) = viewModelScope.launch {
        repositoryOneTimeKavic.insertOneTimeKavic(oneTimeKavicItem)
    }

    fun deleteOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) = viewModelScope.launch {
        repositoryOneTimeKavic.deleteOneTimeKavic(oneTimeKavicItem)
    }

    fun updateOneTimeKavic(oneTimeKavicItem: OneTimeKavicItem) = viewModelScope.launch {
        repositoryOneTimeKavic.updateOneTimeKavic(oneTimeKavicItem)
    }

    fun setArchiveAllOfOneTimeKavics(today: String) = viewModelScope.launch {
        repositoryOneTimeKavic.setArchiveAllOfOneTimeKavics(today)
    }

    fun insertRepeatingKavic(repeatingKavicItem: RepeatingKavicItem) = viewModelScope.launch {
        repositoryOneTimeKavic.insertRepeatingKavic(repeatingKavicItem)
    }

}
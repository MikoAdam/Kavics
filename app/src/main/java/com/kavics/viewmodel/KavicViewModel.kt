package com.kavics.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavics.application.KavicApplication
import com.kavics.database.Repository
import com.kavics.model.KavicItem
import kotlinx.coroutines.launch

class KavicViewModel : ViewModel() {

    private val repository: Repository

    var allKavics: LiveData<List<KavicItem>>

    init {
        val kavicDao = KavicApplication.kavicDatabase.kavicDAO()
        repository = Repository(kavicDao)
        allKavics = repository.getAllKavics()
    }

    fun insert(kavicItem: KavicItem) = viewModelScope.launch {
        repository.insert(kavicItem)
    }

    fun delete(kavicItem: KavicItem) = viewModelScope.launch {
        repository.delete(kavicItem)
    }

    fun update(kavicItem: KavicItem) = viewModelScope.launch {
        repository.update(kavicItem)
    }
}
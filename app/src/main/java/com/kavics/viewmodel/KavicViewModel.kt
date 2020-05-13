package com.kavics.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavics.KavicApplication
import com.kavics.database.Repository
import com.kavics.model.Kavic
import kotlinx.coroutines.launch

class KavicViewModel : ViewModel() {

    private val repository: Repository

    val allKavics: LiveData<List<Kavic>>

    init {
        val kavicDao = KavicApplication.kavicDatabase.kavicDAO()
        repository = Repository(kavicDao)
        allKavics = repository.getAllKavics()
    }

    fun insert(kavic: Kavic) = viewModelScope.launch {
        repository.insert(kavic)
    }

    fun delete(kavic: Kavic) = viewModelScope.launch {
        repository.delete(kavic)
    }

}
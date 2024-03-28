package com.example.trackerapp.presentation.home

import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun getUserIdFromPref() {
        val flow = dataStoreRepository.getValue(stringPreferencesKey("user_id"))
        viewModelScope.launch {
            flow.first()
        }
    }
}
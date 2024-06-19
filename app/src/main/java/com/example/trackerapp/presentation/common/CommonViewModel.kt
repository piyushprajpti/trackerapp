package com.example.trackerapp.presentation.common

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    ) : ViewModel() {
    fun setValueInPref(key: String, value: String) {
        viewModelScope.launch {
            dataStoreRepository.setValue(stringPreferencesKey(key), value)
        }
    }

    suspend fun getValueFromPref(key: String): String? {
        val flow = dataStoreRepository.getValue(stringPreferencesKey(key))
        return flow.firstOrNull()

    }
}
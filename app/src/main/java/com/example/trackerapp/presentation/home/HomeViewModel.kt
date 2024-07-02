package com.example.trackerapp.presentation.home

import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.data.repository.DataStoreRepository
import com.example.trackerapp.domain.repository.HomeRepository
import com.example.trackerapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun getUserInfo(callBack: (Boolean) -> Unit) {
        viewModelScope.launch {
            val number = getValueFromPref("number").firstOrNull()

            val response = repository.getUserInfo(number.toString())

            if (response is Response.Success) {
                setValueInPref("name", response.data.profile?.name.toString())
                setValueInPref("firmName", response.data.profile?.firm.toString())
                setValueInPref("appId", response.data.profile?.appId.toString())
                setValueInPref("signature", response.data.profile?.signature.toString())
                setValueInPref("vehicleNumber", response.data.profile?.vehicleNumber.toString())

                callBack(true)
            } else {
                callBack(false)
            }
        }
    }


    private suspend fun setValueInPref(key: String, value: String) {
            dataStoreRepository.setValue(stringPreferencesKey(key), value)

    }

    fun getValueFromPref(key: String): Flow<String?> {
        return dataStoreRepository.getValue(stringPreferencesKey(key))
    }
}
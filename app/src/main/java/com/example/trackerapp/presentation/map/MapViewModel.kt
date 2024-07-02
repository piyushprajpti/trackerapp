package com.example.trackerapp.presentation.map

import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.data.repository.DataStoreRepository
import com.example.trackerapp.domain.model.mapModels.deviceList.DeviceListResponse
import com.example.trackerapp.domain.model.mapModels.historyPlayback.HistoryPlaybackResponse
import com.example.trackerapp.domain.model.mapModels.liveLocation.LiveLocationResponse
import com.example.trackerapp.domain.repository.MapRepository
import com.example.trackerapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MapRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun generateAccessToken(
        appId: String,
        signature: String,
        callBack: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val response = repository.generateAccessToken(appId, signature)

            if (response is Response.Success) {
                if (response.data.code == 0) {
                    setValueInPref("access_token", response.data.accessToken)
                    callBack(true)
                } else if (response.data.code == 10004) {
                    callBack(false)
                }
            } else {
                callBack(false)
            }
        }
    }

    fun getDeviceList(
        atPage: Int = 1,
        pageSize: Int = 100,
        needCount: Boolean = true,
        callBack: (Response<DeviceListResponse>) -> Unit
    ) {
        viewModelScope.launch {
            val accessToken = getValueFromPref("access_token")
            val response = repository.getDeviceList(
                accessToken.toString(),
                atPage,
                pageSize,
                needCount
            )

            callBack(response)
        }
    }

    fun getLiveLocation(callBack: (Response<LiveLocationResponse>) -> Unit) {
        viewModelScope.launch {
            val accessToken = getValueFromPref("access_token")
            val imei = getValueFromPref("imei")

            val response = repository.getLiveLocation(accessToken.toString(), imei.toString())
            callBack(response)
        }
    }

    fun getHistoryPlayback(
        imei: String,
        startTime: Long,
        endTime: Long,
        callBack: (Response<HistoryPlaybackResponse>) -> Unit
    ) {
        viewModelScope.launch {
            val accessToken = getValueFromPref("access_token")

            val response = repository.getHistoryPlayback(
                accessToken.toString(),
                imei,
                startTime,
                endTime
            )
            callBack(response)
        }
    }


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
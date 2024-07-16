package com.example.trackerapp.presentation.auth

import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.data.repository.DataStoreRepository
import com.example.trackerapp.domain.model.authModels.RegisterResponse
import com.example.trackerapp.domain.model.authModels.SendOtpResponse
import com.example.trackerapp.domain.model.authModels.VerifyOtpResponse
import com.example.trackerapp.domain.model.mapModels.firmList.FirmListResponse
import com.example.trackerapp.domain.repository.AuthRepository
import com.example.trackerapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun onSendOTP(number: String, callback: (Response<SendOtpResponse>) -> Unit) {
        viewModelScope.launch {
            val response = repository.sendOTP(number)
            if (response is Response.Success) {
                setValueInPref("number", number)
            }
            callback(response)
        }
    }

    fun onVerifyOTP(number: String, otp: String, callback: (Response<VerifyOtpResponse>) -> Unit) {
        viewModelScope.launch {
            val response = repository.verifyOTP(number, otp)
            callback(response)
        }
    }

    fun onRegisterUser(
        name: String,
        firmName: String,
        appId: String,
        signature: String,
        vehicleNumber: String,
        callback: (Response<RegisterResponse>) -> Unit
    ) {
        viewModelScope.launch {
            var number = getValueFromPref("number")
            if (number == null) number = ""

            val response = repository.registerUser(
                number,
                name,
                firmName,
                appId,
                signature,
                vehicleNumber
            )
            Log.d("TAG", "onRegisterUser: $response")
            if (response is Response.Success) {
                setValueInPref("name", name)
                setValueInPref("firmName", firmName)
                setValueInPref("appId", appId)
                setValueInPref("signature", signature)
                setValueInPref("vehicleNumber", vehicleNumber)

                callback(response)
            }
        }
    }

    fun getFirmList(callBack: (Response<FirmListResponse>) -> Unit) {
        viewModelScope.launch {
            val response = repository.getFirmList()
            callBack(response)
        }
    }

    suspend fun setValueInPref(key: String, value: String) {
            dataStoreRepository.setValue(stringPreferencesKey(key), value)
    }

    suspend fun getValueFromPref(key: String): String? {
        return dataStoreRepository.getValue(stringPreferencesKey(key)).first()
    }

}
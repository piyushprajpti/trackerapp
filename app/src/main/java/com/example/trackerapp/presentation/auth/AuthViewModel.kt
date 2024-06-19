package com.example.trackerapp.presentation.auth

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.data.repository.DataStoreRepository
import com.example.trackerapp.domain.model.authModels.OtpResponse
import com.example.trackerapp.domain.model.authModels.RegisterResponse
import com.example.trackerapp.domain.model.mapModels.firmList.FirmListResponse
import com.example.trackerapp.domain.repository.AuthRepository
import com.example.trackerapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun onSendOTP(number: String, callback: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            val response = repository.sendOTP(number)
            callback(response)
        }
    }

    fun onVerifyOTP(number: String, otp: String, callback: (Response<OtpResponse>) -> Unit) {
        viewModelScope.launch {
            val response = repository.verifyOTP(number, otp)
            if (response is Response.Success) {
                response.data.existingUser?.let { setUserIDInPref(it.id) }
                response.data.newUser?.let { setUserIDInPref(it.id) }
            }
            callback(response)
        }
    }

    fun onRegisterUser(
        number: String,
        name: String,
        firmName: String,
        vehicleNumber: String,
        callback: (Response<RegisterResponse>) -> Unit
    ) {
        viewModelScope.launch {
            val response = repository.registerUser(number, name, firmName, vehicleNumber)
            if (response is Response.Success) {
                response.data.category?.let { setUserIDInPref(it.id) }
            }
            callback(response)
        }
    }

    fun getFirmList(callBack: (Response<FirmListResponse>) -> Unit) {
        viewModelScope.launch {
            val response = repository.getFirmList()
            callBack(response)
        }
    }

    private fun setUserIDInPref(id: String) {
        viewModelScope.launch {
            dataStoreRepository.setValue(stringPreferencesKey("user_id"), id)
        }
    }

}
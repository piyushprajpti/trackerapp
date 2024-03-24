package com.example.trackerapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.domain.repository.AuthRepository
import com.example.trackerapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    fun onSendOTP(number: String, callback: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            val response = repository.sendOTP(number)
            callback(response)
        }
    }

    fun onVerifyOTP(number: String, otp: String, callback: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            val response = repository.verifyOTP(number, otp)
            callback(response)
        }
    }

}
package com.example.trackerapp.presentation.home

import android.content.Context
import android.location.LocationManager
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    fun getUserIdFromPref(callback: (String?) -> Unit) {
        val flow = dataStoreRepository.getValue(stringPreferencesKey("user_id"))
        viewModelScope.launch {
            callback(flow.firstOrNull())
        }
    }

    fun fetchUserInfo() {

    }

    val isLocationEnabled = MutableStateFlow(false)

    init {
        updateLocationServiceStatus()
    }

    private fun updateLocationServiceStatus() {
        isLocationEnabled.value = locationHelper.isConnected()
    }

}

@Singleton
class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun isConnected(): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
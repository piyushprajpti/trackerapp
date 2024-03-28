package com.example.trackerapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {
    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    fun <T> getValue(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map {
            it[key]
        }.catch {
            if (it is IOException) emit(null)
            else throw it
        }
    }
}
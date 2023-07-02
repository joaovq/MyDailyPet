package br.com.joaovq.mydailypet.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import javax.inject.Inject

const val DARKMODE_PREFERENCE_KEY = "dark-mode"

interface DataStorePreferences {
    suspend fun getBooleanValue(key: String): Boolean
    suspend fun setBooleanValue(key: String, value: Boolean): Boolean
}

class PreferencesManager @Inject constructor(
    private val preferences: DataStore<Preferences>,
) : DataStorePreferences {
    override suspend fun getBooleanValue(key: String): Boolean {
        val booleanPreferenceKey = booleanPreferencesKey(key)
        return preferences.data.first()[booleanPreferenceKey] ?: false
    }

    override suspend fun setBooleanValue(key: String, value: Boolean): Boolean {
        val booleanPreferenceKey = booleanPreferencesKey(key)
        preferences.edit { settings ->
            settings[booleanPreferenceKey] = value
        }
        return getBooleanValue(key)
    }
}

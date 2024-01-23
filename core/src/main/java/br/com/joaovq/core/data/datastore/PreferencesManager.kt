package br.com.joaovq.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import javax.inject.Inject

const val DARKMODE_PREFERENCE_KEY = "dark-mode"
const val IS_NEW_USER_PREFERENCE_KEY = "new-user"

interface DataStorePreferences {
    suspend fun getBooleanValue(key: String, defaultValue: Boolean = false): Boolean
    suspend fun setBooleanValue(key: String, value: Boolean): Boolean
}

class PreferencesManager @Inject constructor(
    private val preferences: DataStore<Preferences>,
) : DataStorePreferences {
    override suspend fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        val booleanPreferenceKey = booleanPreferencesKey(key)
        return preferences.data.first()[booleanPreferenceKey] ?: defaultValue
    }

    override suspend fun setBooleanValue(key: String, value: Boolean): Boolean {
        val booleanPreferenceKey = booleanPreferencesKey(key)
        preferences.edit { settings ->
            settings[booleanPreferenceKey] = value
        }
        return getBooleanValue(key)
    }
}

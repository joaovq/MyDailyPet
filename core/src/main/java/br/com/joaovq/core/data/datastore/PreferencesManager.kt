package br.com.joaovq.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val DARKMODE_PREFERENCE_KEY = "dark-mode"
const val IS_NEW_USER_PREFERENCE_KEY = "new-user"

interface DataStorePreferences {
    fun getBooleanValue(key: String, defaultValue: Boolean = false): Flow<Boolean>
    suspend fun setBooleanValue(key: String, value: Boolean): Boolean
}

class PreferencesManager @Inject constructor(
    private val preferences: DataStore<Preferences>,
) : DataStorePreferences {
    override fun getBooleanValue(key: String, defaultValue: Boolean): Flow<Boolean> {
        val booleanPreferenceKey = booleanPreferencesKey(key)
        return preferences.data.map { it[booleanPreferenceKey] ?: defaultValue }
    }

    override suspend fun setBooleanValue(key: String, value: Boolean): Boolean {
        val booleanPreferenceKey = booleanPreferencesKey(key)
        preferences.edit { settings ->
            settings[booleanPreferenceKey] = value
        }
        return getBooleanValue(key).first()
    }
}

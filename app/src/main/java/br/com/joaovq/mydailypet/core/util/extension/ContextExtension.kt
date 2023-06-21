package br.com.joaovq.mydailypet.core.util.extension

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

const val USER_PREFERENCES = "user-preferences"
const val SETTINGS_DATASTORE = "settings"

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES)
val Context.settingsDatastore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATASTORE)

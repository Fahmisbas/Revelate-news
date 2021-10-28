package com.revelatestudio.revelate.util

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


val Context.dataStore by preferencesDataStore("app_preferences")

object Preferences {
    val COUNTRY_PREF_KEY = stringPreferencesKey("country_pref")
    // write your keys here
}

suspend fun Context.getPreferenceCountry(): String? {
    val countryKey = Preferences.COUNTRY_PREF_KEY
    val countryPreference = dataStore.data.first()
    return countryPreference[countryKey]
}




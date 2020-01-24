package com.miguelmartin.consumii.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.miguelmartin.consumii.Common.ApplicationLanguageHelper
import com.miguelmartin.consumii.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        val lIdiomas = findPreference(getString(R.string.idioma)) as Preference?


    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase!!, "fa"))
    }
}
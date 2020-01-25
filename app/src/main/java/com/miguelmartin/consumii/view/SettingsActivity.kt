package com.miguelmartin.consumii.view

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.miguelmartin.consumii.Common.ApplicationLanguageHelper.Companion.IDIOMA
import com.miguelmartin.consumii.Common.BaseActivity
import com.miguelmartin.consumii.R

class SettingsActivity : BaseActivity() {

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

            val lIdiomas = findPreference(getString(R.string.idioma_id)) as Preference?

            lIdiomas!!.setOnPreferenceChangeListener { preference, newValue ->
                IDIOMA = newValue.toString()
                activity!!.recreate()
                return@setOnPreferenceChangeListener true
            }
        }

    }


}
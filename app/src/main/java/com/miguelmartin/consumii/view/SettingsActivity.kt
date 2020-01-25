package com.miguelmartin.consumii.view

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.miguelmartin.consumii.Common.ApplicationLanguageHelper.Companion.IDIOMA
import com.miguelmartin.consumii.Common.BaseActivity
import com.miguelmartin.consumii.Common.toDate
import com.miguelmartin.consumii.R
import kotlinx.android.synthetic.main.settings_activity.*
import java.util.*


class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_activity_settings)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val lIdiomas = findPreference(getString(R.string.idioma_id)) as Preference?
            val btnAplicacion = findPreference(getString(R.string.aplicacion_id)) as Preference?
            val btnProgramador = findPreference(getString(R.string.programador_id)) as Preference?

            lIdiomas?.setOnPreferenceChangeListener { _, newValue ->
                IDIOMA = newValue.toString()
                activity?.recreate()
                return@setOnPreferenceChangeListener true
            }

            btnAplicacion?.setOnPreferenceClickListener{
                
                return@setOnPreferenceClickListener true
            }

            btnProgramador?.setOnPreferenceClickListener{

                val fechaNacimiento = "1997-05-04".toDate("yyyy-MM-dd")
                val fechaActual = Date()

                val diff = fechaActual.time - fechaNacimiento.time
                val years = diff / (1000*60*60*24*365.25)

                AlertDialog.Builder(activity).apply {
                    setTitle(R.string.sobre_mi_titulo)
                    setMessage(getString(R.string.sobre_mi, years.toInt().toString()))
                    setPositiveButton(R.string.ver_web){_,_ ->
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(getString(R.string.url))
                        startActivity(intent)
                    }
                    setNegativeButton(R.string.cancelar){_,_ ->}
                }.create().show()
                return@setOnPreferenceClickListener true
            }
        }
    }




}
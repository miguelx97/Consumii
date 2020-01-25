package com.miguelmartin.consumii.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import com.miguelmartin.consumii.Common.ApplicationLanguageHelper
import com.miguelmartin.consumii.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        },1000)
    }

    private lateinit var sharedPreferences: SharedPreferences
    override fun attachBaseContext(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        ApplicationLanguageHelper.IDIOMA = sharedPreferences.getString(context.getString(R.string.idioma_id),"")

        if(ApplicationLanguageHelper.IDIOMA.isNotEmpty()) super.attachBaseContext(
            ApplicationLanguageHelper.wrap(context, ApplicationLanguageHelper.IDIOMA))
        else super.attachBaseContext(context)
    }
}

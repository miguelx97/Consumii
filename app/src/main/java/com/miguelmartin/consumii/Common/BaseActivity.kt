package com.miguelmartin.consumii.Common

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {
    override fun attachBaseContext(context: Context) {
        if(ApplicationLanguageHelper.IDIOMA.isNotEmpty()) super.attachBaseContext(ApplicationLanguageHelper.wrap(context,
            ApplicationLanguageHelper.IDIOMA
        ))
        else super.attachBaseContext(context)
    }
}
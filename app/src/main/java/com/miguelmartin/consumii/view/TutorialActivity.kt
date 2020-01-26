package com.miguelmartin.consumii.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.miguelmartin.consumii.Common.BaseActivity
import com.miguelmartin.consumii.R
import kotlinx.android.synthetic.main.settings_activity.*

class TutorialActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_activity_tutorial)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }
}

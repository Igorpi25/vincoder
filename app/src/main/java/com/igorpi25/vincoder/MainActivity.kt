package com.igorpi25.vincoder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.igorpi25.vincoder.ui.details.DetailsFragment
import com.igorpi25.vincoder.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }


    override fun onBackPressed() {
        if (supportFragmentManager!!.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager!!.popBackStack()
        }
    }
}
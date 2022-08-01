package xyz.miyayu.android.weatherapp.activities

import androidx.appcompat.app.AppCompatActivity
import xyz.miyayu.android.weatherapp.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onBackPressed() {
        if (canBack) {
            super.onBackPressed()
        }
    }

    companion object {
        var canBack: Boolean = true
    }
}
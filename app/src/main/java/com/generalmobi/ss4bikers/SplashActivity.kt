package com.generalmobi.ss4bikers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            MapsActivity.startActivity(this@SplashActivity)
            finish()
        }, 2000)
    }
}

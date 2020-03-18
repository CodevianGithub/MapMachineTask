package com.codevian.campgladiator.ui.splash

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.codevian.campgladiator.R
import com.codevian.campgladiator.ui.home.HomeActivity
import com.codevian.campgladiator.utils.CheckPermissions
import com.google.android.libraries.places.api.Places
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.map_key), Locale.US)
        }
        val placesClient = Places.createClient(this)

        if(CheckPermissions.askPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION) && CheckPermissions.askPermissions(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            Handler().postDelayed({

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }
}

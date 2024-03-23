package com.example.trackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.trackerapp.ui.theme.TrackerappTheme
import com.mappls.sdk.maps.Mappls
import com.mappls.sdk.services.account.MapplsAccountManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapplsAccountManager.getInstance().restAPIKey = "33180935d13e0c57688c458317139508"
        MapplsAccountManager.getInstance().mapSDKKey = "33180935d13e0c57688c458317139508"
        MapplsAccountManager.getInstance().atlasClientId = "96dHZVzsAuuWUEMINHbhZ674DbfHmRytqJmEDIBV0W6IFVMkqov0-CrMs35Ea99pewv3TvbBzqWlF2XnI0rUjQ=="
        MapplsAccountManager.getInstance().atlasClientSecret = "lrFxI-iSEg8Vp_-BAuk76N73Pn-54eTqD4PTSszr6Ej-aNI1mZl2IUzwrhTJ3WInZIHHjYrfjB7Fj6zRwTbSodGqs8MTbzxr"
        Mappls.getInstance(applicationContext)

        setContent {
            TrackerappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    TrackerApp()
                }
            }
        }
    }
}
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

//        MapplsAccountManager.getInstance().restAPIKey = "86f8f3653f24500c3b466a19a9698bf0"
//        MapplsAccountManager.getInstance().mapSDKKey = "86f8f3653f24500c3b466a19a9698bf0"
//        MapplsAccountManager.getInstance().atlasClientId = "96dHZVzsAusKW90dGFm6FO262HwMHZYJ_A7dCBC_pPr-o6fQfrkaa5c8YWWH7H7Eg61Y4V2awGkN65mjlfhabCzsfeMcq3L4"
//        MapplsAccountManager.getInstance().atlasClientSecret = "lrFxI-iSEg8yg6kdM3XIDsnrzt62lLBWOdW-hCW1WMk856iLwoARCbgi6feWcaf0opGobnZodyIgDE71KazuAijQPzgKuf1JF9M8Rvzsuw0="
//
//        Mappls.getInstance(this)

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
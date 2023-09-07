package com.barran.sample.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class TestCompostActivity:AppCompatActivity() {

    val naviPages = NaviPages()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme() {
                Surface(modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background){

                    naviPages.StartNaviPages()
                }
            }
        }
    }


}
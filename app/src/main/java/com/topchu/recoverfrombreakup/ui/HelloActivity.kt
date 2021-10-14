package com.topchu.recoverfrombreakup.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.topchu.recoverfrombreakup.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelloActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
    }
}
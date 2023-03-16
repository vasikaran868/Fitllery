package com.example.fitpeoassignment

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.fitpeoassignment.ui.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val runnable = Runnable {
            val intent = Intent(this, ImageListing::class.java)
            startActivity(intent)
            this.finish()
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 2500)


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }
}
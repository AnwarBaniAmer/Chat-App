package com.legend.techtask.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.content.Intent
import android.os.Handler

import android.os.Looper
import com.bumptech.glide.Glide
import com.legend.techtask.R
import com.legend.techtask.databinding.ActivitySplashBinding
import com.legend.techtask.utils.Utils

class SplashActivity : AppCompatActivity() {

    private val delay: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Utils.isConnected(this))
            Glide.with(this).load(R.drawable.no_internet_connection).into(binding.animationView)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, delay)
        return super.onCreateView(name, context, attrs)
    }
}
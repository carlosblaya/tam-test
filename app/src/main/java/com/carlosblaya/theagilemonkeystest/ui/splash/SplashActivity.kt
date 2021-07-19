package com.carlosblaya.theagilemonkeystest.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.carlosblaya.theagilemonkeystest.databinding.ActivitySplashBinding
import com.carlosblaya.theagilemonkeystest.ui.main.MainActivity
import com.carlosblaya.theagilemonkeystest.util.Konsts
import com.carlosblaya.theagilemonkeystest.util.createIntent
import com.carlosblaya.theagilemonkeystest.util.viewBinding

class SplashActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivitySplashBinding::inflate)

    private var mHandler = Handler()
    private var mRunnable: Runnable = Runnable {
        startActivity(createIntent<MainActivity>())
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mHandler.postDelayed(mRunnable, Konsts.SPLASH_TIME_OUT)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mHandler.removeCallbacks(mRunnable)
    }
}
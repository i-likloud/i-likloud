package com.ssafy.likloud

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ssafy.likloud.ApplicationClass.Companion.USER_EMAIL
import com.ssafy.likloud.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
import com.ssafy.likloud.base.BaseActivity
import com.ssafy.likloud.databinding.ActivityMainBinding
import com.ssafy.likloud.databinding.ActivitySplashBinding
import com.ssafy.likloud.util.makeAnimationFade
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SplashActivity_싸피"
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sharedPreferences.getString(X_ACCESS_TOKEN) != null && sharedPreferences.getString(X_ACCESS_TOKEN)!!
                .isNotEmpty()) {
            mainActivityViewModel.isLogined = true
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(4400)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("isLogined", mainActivityViewModel.isLogined)
            })
        }
    }
}
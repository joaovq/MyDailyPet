package br.com.joaovq.mydailypet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import br.com.joaovq.mydailypet.databinding.ActivityCustomSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class CustomSplashScreenActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCustomSplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, 2000)
    }
}

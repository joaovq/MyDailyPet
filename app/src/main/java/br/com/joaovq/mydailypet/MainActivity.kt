package br.com.joaovq.mydailypet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.joaovq.mydailypet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.PUBLICATION) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}

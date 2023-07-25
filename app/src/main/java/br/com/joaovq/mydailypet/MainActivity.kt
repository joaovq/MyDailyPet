package br.com.joaovq.mydailypet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import br.com.joaovq.mydailypet.data.datastore.IS_NEW_USER_PREFERENCE_KEY
import br.com.joaovq.mydailypet.data.datastore.PreferencesManager
import br.com.joaovq.mydailypet.databinding.ActivityMainBinding
import br.com.joaovq.mydailypet.home.presentation.view.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var preferencesManager: PreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        lifecycleScope.launch {
            if (
                preferencesManager.getBooleanValue(
                    IS_NEW_USER_PREFERENCE_KEY,
                    defaultValue = true,
                )
            ) {
                navHost.navController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToOnBoardingFragment(),
                )
            }
        }
    }
}

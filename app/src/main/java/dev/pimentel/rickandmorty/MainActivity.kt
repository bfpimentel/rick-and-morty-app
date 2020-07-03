package dev.pimentel.rickandmorty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.databinding.MainActivityBinding
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)!!
            .findNavController()

        NavigationUI.setupWithNavController(binding.navigationView, navController)

        navigator.bind(navController)
    }

    override fun onPause() {
        super.onPause()
        navigator.unbind()
    }
}

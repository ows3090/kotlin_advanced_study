package ows.kotlinstudy.subway_application.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import ows.kotlinstudy.subway_application.R
import ows.kotlinstudy.subway_application.databinding.ActivityMainBinding
import ows.kotlinstudy.subway_application.extension.toGone
import ows.kotlinstudy.subway_application.extension.toVisible
import ows.kotlinstudy.subway_application.presenter.stationsArrivals.StationArrivalsFragment
import ows.kotlinstudy.subway_application.presenter.stationsArrivals.StationArrivalsFragmentArgs

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        bindViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViews(){
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navigationController)
    }

    private fun bindViews(){
        navigationController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.station_arrivals_dest){
                title = StationArrivalsFragmentArgs.fromBundle(arguments!!).station.name
                binding.toolbar.toVisible()
            }else{
                binding.toolbar.toGone()
            }
        }
    }
}
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

    // NagController 객체를 사용하여 특정 목적지로 이동, 이 객체는 NavHost 내에서 앱 탐색 관리
    // NavHost에 자체 NavController가 있다.
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        bindViews()
    }


    // 1. NavController navigateUp 호출
    // 2. NavController navigateUp이 false일 경우 super.onSupportNavigateUp 호출 -> MainActivity 종료
    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    // NoActionBar 테마로 ToolBar를 ActionBar로 지정
    private fun initViews(){
        setSupportActionBar(binding.toolbar)

        // ActionBar와 NavController를 함께 사용하기 위해서 호출
        // 자동으로 title은 탐색할 목적지 네임으로 변경
        setupActionBarWithNavController(navigationController)
    }

    private fun bindViews(){
        // NavContoller의 현재 대상 또는 인수가 변경될 때 호출되는 리스너 등록
        navigationController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.station_arrivals_dest){
                // SafeArgs 이용 시 각 작업의 안전 클래스와 메서드 포함
                title = StationArrivalsFragmentArgs.fromBundle(arguments!!).station.name
                binding.toolbar.toVisible()
            }else{
                binding.toolbar.toGone()
            }
        }
    }
}
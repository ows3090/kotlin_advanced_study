package ows.kotlinstudy.movierank_application.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ows.kotlinstudy.movierank_application.R
import ows.kotlinstudy.movierank_application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /**
     * Navigtaion 2.3.5와 BottomNavigation 연동 시 문제점 존재
     * 탭 간의 이동 후 다시 돌아왔을 떄 기존의 Fragment가 onDestory까지 호출된 후 새로 생성
     * ViewModel 사용하면 onCleared까지 호출됨.
     */
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews(){
        /**
         * appBarConfiguration의 id는 nav_graph와 menu가 동일해야 함. 또는 id는 top level view만 입력
         * top level이기에 up button이 생기지 않음. 즉 다른 탭으로 이동 시 Stack으로 쌓이지 않음
         */
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_mypage))
        binding.navView.setupWithNavController(navigationController)
        binding.toolbar.setupWithNavController(navigationController, appBarConfiguration)
    }
}
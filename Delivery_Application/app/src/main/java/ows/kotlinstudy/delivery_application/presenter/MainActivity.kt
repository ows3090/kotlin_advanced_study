package ows.kotlinstudy.delivery_application.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ows.kotlinstudy.delivery_application.R
import ows.kotlinstudy.delivery_application.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        /**
         * 기존에 액티비티에 프래그먼트를 호스팅 하는 일반적인 방법 -> FrameLayout
         * androidx.fragment 1.2.0 이후 -> FragmentContainerView
         *
         * FragmentContainerView는 1회성 작업 수행
         * 1. 프래그먼트의 새로운 인스턴스 생성
         * 2. Fragment.onInflate(Context, AttributeSet, Bundle) 호출
         * 3. 프래그먼트를 프래그먼트 매니저에 추가하기 위한 트랙잭션 수행
         *
         * 따라서 findFragmentById로 바로 검색 가능
         */
        val navigationController =
            (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController

        binding.toolbar.setupWithNavController(navigationController)
    }
}
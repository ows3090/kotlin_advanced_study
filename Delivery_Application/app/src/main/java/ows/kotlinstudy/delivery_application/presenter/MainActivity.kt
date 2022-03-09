package ows.kotlinstudy.delivery_application.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import ows.kotlinstudy.delivery_application.R
import ows.kotlinstudy.delivery_application.databinding.ActivityMainBinding
import ows.kotlinstudy.delivery_application.work.TrackingCheckWorker
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initWorker()
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

    private fun initWorker(){
        val workerStartTime = Calendar.getInstance()
        workerStartTime.set(Calendar.HOUR_OF_DAY, 16)
        val initialDelay = workerStartTime.timeInMillis - System.currentTimeMillis()


        /**
         * 주기적으로 workRequest ->  1일 단위로 반복
         * 1. setInitailDealy : 초기 지연 시간, 16시에 실행
         * 2. BackOff 정책 : 실패 후 다시 실행되는 정책
         */
        val dailyTrackingCheckRequest =
            PeriodicWorkRequestBuilder<TrackingCheckWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        /**
         * Unique한 이름을 가진 작업 실행
         * 1. Unique Name
         * 2. 이름 충돌 시 정책
         * 3. 작업 단위 객체 Worker
         */
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "DailyTrackingCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                dailyTrackingCheckRequest
            )
    }
}
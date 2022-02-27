package ows.kotlinstudy.sns_application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ows.kotlinstudy.sns_application.chatlist.ChatFragment
import ows.kotlinstudy.sns_application.databinding.ActivityMainBinding
import ows.kotlinstudy.sns_application.home.HomeFragment
import ows.kotlinstudy.sns_application.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    // lazy, lateinit var 상관 없다 : 늦은 초기화를 위해서 사용
    // MainActivity의 인스턴스가 생기고 힙 영역에 할당되는 순간 초기화가 되는 것이 아니다.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatFragment()
        val myPageFragment = MyPageFragment()

        replaceFragment(homeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        // setOnNavigationItemSelectedListener deprecated
        bottomNavigationView.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.chatList -> replaceFragment(chatListFragment)
                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        // 기존의 모든 fragment를 replace
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}
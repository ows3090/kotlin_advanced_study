package ows.kotlinstudy.subway_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ows.kotlinstudy.subway_application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
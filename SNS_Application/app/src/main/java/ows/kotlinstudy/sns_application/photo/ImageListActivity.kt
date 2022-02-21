package ows.kotlinstudy.sns_application.photo

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import ows.kotlinstudy.camera_application.adapter.ImageViewPagerAdapter
import ows.kotlinstudy.camera_application.util.PathUtil
import ows.kotlinstudy.sns_application.R
import ows.kotlinstudy.sns_application.databinding.ActivityImageListBinding
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class ImageListActivity : AppCompatActivity() {

    companion object{
        private const val URI_LIST_KEY = "uriList"

        fun newIntent(activity: Activity, uriList: List<Uri>) =
            Intent(activity, ImageListActivity::class.java).apply {
                putExtra(URI_LIST_KEY, ArrayList<Uri>().apply { uriList.forEach{ add(it)} })
            }
    }

    private lateinit var binding: ActivityImageListBinding
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    private val uriList by lazy<List<Uri>> { intent.getParcelableArrayListExtra(URI_LIST_KEY)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        setSupportActionBar(binding.toolbar)
        setupImageList()
    }

    private fun setupImageList() = with(binding){
        if(::imageViewPagerAdapter.isInitialized.not()){
            imageViewPagerAdapter = ImageViewPagerAdapter(uriList)
        }

        imageViewPager.adapter = imageViewPagerAdapter
        indicator.setViewPager(imageViewPager)
        imageViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                toolbar.title = getString(R.string.image_page, position + 1, imageViewPagerAdapter.uriList.size)
            }
        })

        deleteButton.setOnClickListener {
            removeImage(uriList.get(imageViewPager.currentItem))
        }

        confirmButton.setOnClickListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra(URI_LIST_KEY, ArrayList<Uri>().apply { imageViewPagerAdapter.uriList.forEach{ add(it)} })
            })
            finish()
        }
    }

    private fun removeImage(uri: Uri){
        val file = File(PathUtil.getPath(this, uri) ?: throw FileNotFoundException())
        file.delete()
        imageViewPagerAdapter.uriList.let {
            val imageList = it.toMutableList()
            imageList.remove(uri)
            imageViewPagerAdapter.uriList = imageList
            imageViewPagerAdapter.notifyDataSetChanged()
        }

        // 갤러리는 갱신이 되지 않음 -> MediaSannerConnection을 이용하여 갱신
        MediaScannerConnection.scanFile(this, arrayOf(file.path), arrayOf("image/jpeg"), null)
        binding.indicator.setViewPager(binding.imageViewPager)
        if(imageViewPagerAdapter.uriList.isEmpty()){
            Toast.makeText(this, "삭제할 이미지가 없습니다", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
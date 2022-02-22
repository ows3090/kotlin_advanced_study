package ows.kotlinstudy.sns_application.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import ows.kotlinstudy.sns_application.DBKey.Companion.DB_ARTICLES
import ows.kotlinstudy.sns_application.databinding.ActivityArticleAddBinding
import ows.kotlinstudy.sns_application.photo.CameraActivity
import ows.kotlinstudy.sns_application.photo.PhotoListAdapter

class ArticleAddActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 1000
        const val GALLERY_REQUEST_CODE = 1001
        const val CAMERA_REQUEST_CODE = 1002

        private const val URI_LIST_KEY = "uriList"
    }

    private var imageUriList: ArrayList<Uri> = arrayListOf()

    // Firebase Auth
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    // 사진 저장 스토리지
    private val storage: FirebaseStorage by lazy { Firebase.storage }

    // 실시간 데이터베이스
    private val articleDB: DatabaseReference by lazy { Firebase.database.reference.child(DB_ARTICLES) }

    private val photoListAdapter = PhotoListAdapter{ uri -> removePhoto(uri)}
    private lateinit var binding: ActivityArticleAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticleAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        photoRecyclerView.adapter = photoListAdapter

        imageAddButton.setOnClickListener {
            showPictureUploadDialog()
        }

        submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val sellerId = auth.currentUser?.uid.orEmpty()

            showProgress()

            if (imageUriList.isNotEmpty()) {


                uploadPhoto(imageUriList.first(),
                    successHandler = { uri ->
                        uploadArticle(sellerId, title, content, uri)
                    },
                    errorHandler = {
                        Toast.makeText(this@ArticleAddActivity, "사진 업로드에 실패했습니다", Toast.LENGTH_LONG)
                            .show()
                        hideProgress()
                    }
                )
            } else {
                uploadArticle(sellerId, title, content, "")
            }
        }
    }

    private suspend fun uploadPhoto(uriList: List<Uri>) = withContext(Dispatchers.IO){
        val uploadDeferrend: List<Deferred<Any>> = uriList.mapIndexed{ index, uri ->
            lifecycleScope.async {
                try{
                    val fileName = "image_${index}.png"
                    return@async storage
                        .reference
                        .child("article/photo")
                        .child(fileName)
                        .putFile(uri)
                        .await()
                        .storage
                        .downloadUrl
                        .await()
                        .toString()
                }catch (e: Exception){
                    e.printStackTrace()
                    return@async Pair(uri, e)
                }
            }
        }

        return@withContext uploadDeferrend.awaitAll()
        val fileName = "${System.currentTimeMillis()}.png"
        // storage에 파일 쓰기
        storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    // downloadUrl 가져오기
                    storage.reference.child("article/photo").child(fileName)
                        .downloadUrl
                        .addOnSuccessListener {
                            successHandler(it.toString())
                        }.addOnFailureListener {
                            errorHandler()
                        }

                } else {
                    errorHandler()
                }
            }
    }

    private fun uploadArticle(sellerId: String, title: String, content: String, imageUri: String) {
        val model = ArticleModel(sellerId, title, System.currentTimeMillis(), content, imageUri)
        articleDB.push().setValue(model)
        hideProgress()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startContentProvider()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startGalleryScreen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun startCameraScreen() {
        startActivityForResult(
            CameraActivity.newIntent(this),
            CAMERA_REQUEST_CODE
        )
    }

    private fun showProgress() {
        binding.progressbar.isVisible = true
    }

    private fun hideProgress() {
        binding.progressbar.isVisible = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return

        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                val uri = data?.data
                if (uri != null) {
                    imageUriList.add(uri)

                    photoListAdapter.setPhotoList(imageUriList)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_LONG).show()
                }
            }
            CAMERA_REQUEST_CODE -> {
                data?.let{ intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    uriList?.let { list ->
                        imageUriList.addAll(list)

                        photoListAdapter.setPhotoList(imageUriList)
                    }
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this@ArticleAddActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                //startContentProvider()
                uploadAction()
            }

            // SDK > M(23)
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }
            else -> {
                // SDK > M(23)
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun showPictureUploadDialog() {
        AlertDialog.Builder(this)
            .setTitle("사진첨부")
            .setMessage("사진첨부할 방식을 선택하세요")
            .setPositiveButton("카메라") { _, _ ->
                checkExternalStoragePermission {
                    startCameraScreen()
                }
            }
            .setNegativeButton("갤러리") { _, _ ->
                startGalleryScreen()
            }
            .create()
            .show()
    }

    private fun removePhoto(uri: Uri){
        imageUriList.remove(uri)
        photoListAdapter.setPhotoList(imageUriList)
    }
}
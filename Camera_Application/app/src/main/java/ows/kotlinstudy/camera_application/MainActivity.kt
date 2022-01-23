package ows.kotlinstudy.camera_application

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ows.kotlinstudy.camera_application.databinding.ActivityMainBinding
import ows.kotlinstudy.camera_application.util.PathUtil
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var cameraExecutor: ExecutorService
    private val cameraMainExecutor by lazy { ContextCompat.getMainExecutor(this) }
    private val cameraProvideFuture by lazy { ProcessCameraProvider.getInstance(this)} // 카메라 얻어오면 이후 실행 리스너 등록

    private lateinit var imageCapture: ImageCapture

    private val displayManager by lazy {
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private var displayId: Int = -1

    private var camera: Camera? = null

    private var isCapturing: Boolean = false

    private var contentUri: Uri? = null

    private val displayListener = object: DisplayManager.DisplayListener{
        override fun onDisplayAdded(displayId: Int) = Unit

        override fun onDisplayRemoved(displayId: Int) = Unit

        override fun onDisplayChanged(displayId: Int) {
            if(this@MainActivity.displayId == displayId){

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(allPermissionsGranted()){
            startCamera(binding.viewFinder)
        }else{
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera(viewFinder: PreviewView){
        displayManager.registerDisplayListener(displayListener, null)
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewFinder.postDelayed({
            displayId = viewFinder.display.displayId
            bindCameraUseCase()
        }, 10)
    }

    private fun bindCameraUseCase() = with(binding){
        val rotation = viewFinder.display.rotation
        val cameraSelector = CameraSelector.Builder().requireLensFacing(LENS_FACING).build()    // 카메라 설정(후면)

        cameraProvideFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProvideFuture.get()
            val preview = Preview.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_4_3)
                setTargetRotation(rotation)
            }.build()

            val imageCaptureBuilder = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(rotation)
                .setFlashMode(FLASH_MODE_AUTO)

            imageCapture = imageCaptureBuilder.build()

            try{
                cameraProvider.unbindAll() // 기존에 바인딩 되어 있는 카메라는 해제를 해준다.
                camera = cameraProvider.bindToLifecycle(
                    this@MainActivity, cameraSelector, preview, imageCapture
                )
                preview.setSurfaceProvider(viewFinder.surfaceProvider)
                bindCaptureListener()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }, cameraMainExecutor)
    }

    private fun bindCaptureListener() = with(binding){
        captureButton.setOnClickListener {
            if(isCapturing.not()){
                isCapturing = true
                captureCamera()
            }
        }
    }

    private fun updateSavedImageContent() {
        contentUri?.let {
            isCapturing = try{
                val file = File()
                false
            } catch (e: Exception){

                false
            }
        }
    }

    private fun captureCamera(){
        if(::imageCapture.isInitialized.not()) return

        val photoFile = File(
            PathUtil.getOutputDirectory(this),
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.KOREA
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions, cameraExecutor, object: ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                contentUri = savedUri

            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                isCapturing = false
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera(binding.viewFinder)
            }else{
                Toast.makeText(this, "카메라 권한이 없습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object{
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private val LENS_FACING: Int = CameraSelector.LENS_FACING_BACK
    }
}
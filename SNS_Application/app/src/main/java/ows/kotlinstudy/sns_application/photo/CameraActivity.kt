package ows.kotlinstudy.sns_application.photo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.core.impl.ImageOutputConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import ows.kotlinstudy.camera_application.extensions.loadCenterCrop
import ows.kotlinstudy.camera_application.util.PathUtil
import ows.kotlinstudy.sns_application.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    // ThreadPoolExecutor 상위 인터페이스
    private lateinit var cameraExecutor: ExecutorService
    private val cameraMainExecutor by lazy { ContextCompat.getMainExecutor(this) }
    private val cameraProvideFuture by lazy { ProcessCameraProvider.getInstance(this)} // 카메라 얻어오면 이후 실행 리스너 등록

    private lateinit var imageCapture: ImageCapture

    private val displayManager by lazy {
        // context 메소드 중 하나 : 시스템 서비스 호출
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private var displayId: Int = -1

    private var camera: Camera? = null
    private var root: View? = null

    private var isCapturing: Boolean = false
    private var isFlashEnabled: Boolean = false

    private var contentUri: Uri? = null

    private val displayListener = object: DisplayManager.DisplayListener{
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit

        // 디스플레이가 변경될 시 호출 콜백
        override fun onDisplayChanged(displayId: Int) {
            if(this@CameraActivity.displayId == displayId){
                if(::imageCapture.isInitialized && root != null){
                    imageCapture.targetRotation = root?.display?.rotation ?: ImageOutputConfig.INVALID_ROTATION
                }
            }
        }
    }

    private var uriList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        root = binding.root
        setContentView(binding.root)

        if(allPermissionsGranted()){
            startCamera(binding.viewFinder)
        }else{
            // api 23(M) 이전에는 런타임 권한이 없고 무조건 권한이 있음.
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        // api 23(M) 이전 권한 요청을 하지 않아도 됨.
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera(viewFinder: PreviewView){
        displayManager.registerDisplayListener(displayListener, null)
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Handler.postDelayed  vs View.postDelayed : View는 attach된 이후에만 runnable 실행
        // View와 관련한 미묘한 타이밍 이슈가 있을 경우를 대비
       viewFinder.postDelayed({
            displayId = viewFinder.display.displayId
            bindCameraUseCase()
        }, 10)
    }

    private fun bindCameraUseCase() = with(binding){
        val rotation = viewFinder.display.rotation  // 카메라 회전
        val cameraSelector = CameraSelector.Builder().requireLensFacing(LENS_FACING).build()    // 카메라 설정(후면)

        cameraProvideFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProvideFuture.get()

            // preview 설정
            val preview = Preview.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_4_3)
                setTargetRotation(rotation)
            }.build()

            // 카메라의 이미지를 캡처할 수 있는 imageCapture Builder 초기화
            val imageCaptureBuilder = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)     // 지연 초기화 모드
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(rotation)
                .setFlashMode(FLASH_MODE_AUTO)      // 플래쉬

            imageCapture = imageCaptureBuilder.build()

            try{
                cameraProvider.unbindAll() // 기존에 바인딩 되어 있는 카메라는 해제를 해준다.
                camera = cameraProvider.bindToLifecycle(
                    this@CameraActivity, cameraSelector, preview, imageCapture
                )

                // preview가 보여질 Surface 설정
                preview.setSurfaceProvider(viewFinder.surfaceProvider)
                bindCaptureListener()
                bindZoomListener()
                bindLightSwitchListener()
                bindPreviewImageViewClickListener()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }, cameraMainExecutor)
    }

    // Touch 이벤트 시 Click 이벤트는 performClick을 호출해야 접근성이 높아짐
    // performClick 메소드를 호출하지 않을 시는 Lint Warning 출
    @SuppressLint("ClickableViewAccessibility")
    private fun bindZoomListener() = with(binding){
        // 드래그/확대 리스너
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1f
                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio*delta)
                return true
            }
        }

        // 드래그/확대 detector에 리스너 등록
        val scaleGestureDetector = ScaleGestureDetector(this@CameraActivity, listener)
        viewFinder.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }
    }

    private fun bindLightSwitchListener() = with(binding){
        val hasFlash = camera?.cameraInfo?.hasFlashUnit() ?: false
        flashSwitch.isGone = hasFlash.not()
        if(hasFlash){
            flashSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                isFlashEnabled = isChecked
            }
        }else{
            isFlashEnabled = false
            flashSwitch.setOnCheckedChangeListener(null)
        }
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
                val file = File(PathUtil.getPath(this, it) ?: throw FileNotFoundException())
                Log.d("msg","updateSavedImageContent ${file.path}")

                // 찍은 사진 반영
                MediaScannerConnection.scanFile(this, arrayOf(file.path), arrayOf("image/jpeg"),null)
                Handler(mainLooper).post{
                    binding.previewImageVIew.loadCenterCrop(url = it.toString(), corner = 4f)
                }
                if(isFlashEnabled) flashLight(false)
                uriList.add(it)
                false
            } catch (e: Exception){
                e.printStackTrace()
                false
            }
        }
    }

    private fun captureCamera(){
        // imageCapture이 중간이 null일 경우 방지
        if(::imageCapture.isInitialized.not()) return

        // 저장될 파일
        val photoFile = File(
            PathUtil.getOutputDirectory(this),
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.KOREA
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        if(isFlashEnabled) flashLight(true)
        imageCapture.takePicture(outputOptions, cameraExecutor, object: ImageCapture.OnImageSavedCallback{
            // MediaStore를 통해 저장한다면 outputFileResults not null
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d("msg","outputFilErsult : ${outputFileResults.savedUri}")
                Log.d("msg","Uri fromFile : ${Uri.fromFile(photoFile)}")
                val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                contentUri = savedUri
                updateSavedImageContent()
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                isCapturing = false
                flashLight(false)
            }
        })

    }

    private fun flashLight(light: Boolean){
        val hasFlash = camera?.cameraInfo?.hasFlashUnit() ?: false
        if(hasFlash){
            camera?.cameraControl?.enableTorch(light)
        }
    }

    private fun bindPreviewImageViewClickListener() = with(binding){
        previewImageVIew.setOnClickListener {
            startActivityForResult(
                ImageListActivity.newIntent(this@CameraActivity, uriList),
                CONFIRM_IMAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CONFIRM_IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            setResult(RESULT_OK, data)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

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
        const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private val LENS_FACING: Int = CameraSelector.LENS_FACING_BACK  // 카메라 후

        const val CONFIRM_IMAGE_REQUEST_CODE = 3000
        private const val URI_LIST_KEy = "uriList"
        fun newIntent(activity: Activity) = Intent(activity, CameraActivity::class.java)
    }
}
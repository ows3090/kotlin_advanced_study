## CameraX
CameraX는 카메라 앱 개발을 빠르고 쉽게 만들 수 있도록 만들어진 라이브러리로, Camera2의 기능을 활용하면서 수명 주기를 인식합니다.
Camera2의 지원 기기별로 호환성 문제도 해결되어 보일러 플레이트 코드도 방지할 수 있습니다.

### CameraX 구조
- Preview
- Image analysis
- Image Capture
- Video Capture

해당 애플리케이션에서는 Preview, Image Capture 를 사용하였습니다.

#### PreviewView
1. 선택사항으로 CameraXConfig.Provider 구성
```kotlin
class Camera_Application: Application(), CameraXConfig.Provider {
    override fun getCameraXConfig(): CameraXConfig = Camera2Config.defaultConfig()
}
```
CameraX가 초기화되는 시점을 세밀히 하기 위해서 설정하지만, 대부분의 앱에서는 필요하지 않습니다.

2. PreviewView 레이아웃에 추가
```
        <androidx.camera.view.PreviewView
            android:id="@+id/viewFinder"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:clickable="false"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintDimensionRatio="h,4:3"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHeight_default="percent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
```

3. CameraProvider 요청
```kotlin
	private val cameraProvideFuture by lazy { ProcessCameraProvider.getInstance(this)} // 카메라 얻어오면 이후 실행 리스너 등록
```
ProcessCameraProvider를 요청하면 CameraProvider Future 객체가 반환

4. CameraProvider 사용 가능 여부 확인
```kotlin
	cameraProvideFuture.addListener({
        	val cameraProvider: ProcessCameraProvider = cameraProvideFuture.get()
		...
```
Future 객체는 get() 메소드를 통해 CameraProvider를 가져올 수 있으며, View에 연결하기 전에 초기화 성공했는지 확인합니다.

5. 카메라 선택 및 수명 주기
```kotlin
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
                    this@MainActivity, cameraSelector, preview, imageCapture
                )

                // preview가 보여질 Surface 설정
                preview.setSurfaceProvider(viewFinder.surfaceProvider)
                bindCaptureListener()
                bindZoomListener()
                initFlashAndAddListener()
                bindPreviewImageViewClickListener()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }, cameraMainExecutor)
    }
```
카메라의 전면/후면, 회전을 설정하고, preview도 설정합니다.
이후 기존에 바인딩 되어 있던 카메라를 해제 후 다시 lifecycleowner와 연결합니다.
preview가 보여질 surface는 레이아웃에 추가된 PreviewView로 설정하면 됩니다.

#### Image Capture
```kotlin
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
```
이미지 캡처는 takePicture 메소드를 사용하여 캡처가 가능하며 메소드의 파라미터에 따라 캡처사진을
사용하는 방법이 달라집니다.
해당 메소드는 제공된 파일 위치에 저장하게 됩니다.






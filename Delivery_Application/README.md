## WorkManager

### Worker 클래스로 작업 정의
```kotlin
class TrackingCheckWorker(
    val context: Context,
    workerParams: WorkerParameters,
    private val trackingItemRepository: TrackingItemRepository,
    private val dispatcher: CoroutineDispatcher
): CoroutineWorker(context, workerParams) {

	...
}
```
- 해당 프로젝트에서는 Coroutine을 사용하기에 CoroutineWorker를 사용
- 기본적으로는 Worker 클래스를 사용하여 작업을 정의
- Worker 클래스는 doWork() 메소드 필수 구현, doWork 메소드가 WorkManager에서 제공하는 백그라운드 스레드에서 실행
- doWork()에서 반환된 Result는 success, failure, retry를 통해 WorkManager에게 알림

<br>

## 작업 실행 방법 및 시기 구성
- Worker는 작업 단위를 정의하고 WorkRequest는 작업이 언제 어떻게 실행되어야 하는지 정의
- 작업은 일회성(OneTimeWorkRequest)이거나 주기적(PeriodicWorkRequest)으로 반복될 수 있음
- WorkRequest에는 작업 실행의 제약조건, 작업 입력, 지연, 작업 재시도를 위한 백오프 정책 같은 추가 정보 포함

```kotlin
val dailyTrackingCheckRequest =
            PeriodicWorkRequestBuilder<TrackingCheckWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
```
<br>

## 시스템에 작업 전달하기
WorkRequest를 정의했다면 WorkManager의 enqueue() 메서드를 사용하여 작업을 스케줄링
```kotlin
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "DailyTrackingCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                dailyTrackingCheckRequest
	)
```
- 작업자가 실행될 정확한 시간은 WorkRequest에 사용되는 제약조건, 시스템 최적화에 따라 달라짐.

<br>

## Custom WorkManager 구성
기본적으로 WorkManager는 앱이 시작될 때 합리적인 옵션에 맞게 구성되어 실행되지만 맞춤 초기화 방법도 별도로 제공
<br>

### 초기화
```kotlin
<provider
    android:name="androidx.work.impl.WorkManagerInitializer"
    android:authorities="${applicationId}.workmanager-init"
    tools:node="remove" />
```
자체 구성을 제공하려면 기본 프로그램을 삭제해야 합니다.

<br>

### Configuration.Provider 구현
Application 클래스에서 Configuration.Provider를 구현하도록 하고 getWorkManagerConfiguration 메소드를 구현하도록 합니다.
<br>

```kotlin
    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(
                if(BuildConfig.DEBUG){
                    android.util.Log.DEBUG
                }else{
                    android.util.Log.INFO
                }
            )
            .setWorkerFactory(workerFactory)
            .build()
```
- 여기서 setWorkerFactory 메소드에 직접 생성한 workerFactory를 대입하여 호출하면 됩니다.


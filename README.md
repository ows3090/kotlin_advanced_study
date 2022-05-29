
## Why we use Architecture?
1. Consistent code write -> maintenance, cooperation good
2. Increased productivity
3. Ease of testing
4. Provides direction for application development.

<br>

## Clean Architecture
1. Presentation Layer
	- View : Activity, Fragment
	- Presenter : Controller, Presenter, ViewModel

2. Domain Layer
	- UseCase
	- Translater(Entity -> Model)

3. Data Layer
	- Repository : Domain과 Data Store, Remote Layer를 연결
	- Entitiy : 최소단위 비즈니스 객체

<br>

## Todo_Application
1. Clean Architecture
2. Test Driven Development(Mockito)
3. Coroutine
4. Koin
5. Jetpack(LiveData, ViewModel, Room)

<br>

## Shopping_Application
1. Clean Architecture
2. OAuth(Firebase, Google Login)
3. Coroutine
4. Glide
5. Koin
6. Jetpack(LiveData, ViewModel, Room)
7. Retrofit, Okhttp3
8. SwipeLayout

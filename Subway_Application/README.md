
## MVP
### Model
- 애플리케이션의 비즈니스 로직과 사용되는 데이터를 다루는 영역
- View를 알지 못함
- 안드로이드에서는 데이터베이스의 Entity를 담당하는 POJO 클래스를 포함한 SQLite, Room이 될 수 있다.
- MVP 아키텍처에서는 Repository 패턴으로 따로 정의하여 Presenter에 포함
<br>

### View
- 사용자에게 보여지는 부분으로 상호작용할 수 있음
- MVP 아키텍처에서 Activity, Fragment가 View 역할
<br>

### Presenter
- MVC의 단점을 보완하기 위한 View와 Model을 연결해주는 Presenter 계층 생성
- MVC에서는 View와 Controller가 Activity, Fragment로 합쳐진 형태라 UI와 비즈니스 로직이 공존 -> 유닛테스트의 어려움 초래
- View에는 UI 관련 코드, Model에서 비즈니스 로직이 존재하고 Presenter가 둘 사이를 연결
<br>

### MVP의 특징
1. Presenter는 View와 Model의 인스턴스를 가지며 둘을 연결해주는 역할을 하기에 Presenter와 View는 1:1 관계
2. View와 Model의 의존성은 없어 유닛테스트가 쉬워짐
3. 하지만 View와 Presenter의 의존성이 높아 View가 많아지면 Presenter 클래스도 많아진다
<br>

## Jetpack Navigation
- Nav Graph : 모든 탐색 관련 정보가 하나의 중심 위치에 모여 있는 XML 리소스, 앱 내의 모든 개별적 콘텐츠 영역에 갈 수 있음
- NavHost : Nav Graph에서 대상을 표시하는 빈 컨테이너
- NavController : NavHost에서 앱 탐색을 관리하는 객

앱을 탐색하는 동안 Nav Graph에서 특정 경로(액션)을 따라 이동할지, 특정 대상으로 직접 이동할지 NavController에게 전달
, NavController가 NavHost에 대상을 표시하는 방식

### 장점
- 프래그먼트 트랜잭션을 직접 처리하지 않아도 된다.
- 목적지 간 이동 및 복귀 올바르게 수행
- 기본적인 화면 전환 애니메이션 제공
- 딥 링크 구현 및 처리 제공
- 목적지 간 인자 전달 시 타입 안정성(type-safety)를 위한 Safe Args 플러그인 제공
- ViewModel 범위를 Nav Graph로 지정하여 그래프 내 목적지 간 UI 관련 데이터 공유

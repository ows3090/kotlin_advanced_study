## OAuth

![image](https://user-images.githubusercontent.com/34837583/150627562-167a8e33-6165-4de5-9509-d476587b6655.png)
출처 : https://velog.io/@denmark-choco/OAuth-2
<br>


### OAuth란?

 간단하게 말하자면 자신의 서비스에서 제 3자의 서비스(페이스북, 구글, 네이버 ..) 의 기능들을 이용하게 해줄 수 있는 기술이다.
 그렇다면 제 3자의 서비스를 이용할 때는 어떻게 이용을 해야할까?
 가장 쉬운 방법으로는 제 3자의 서비스로 로그인을 하여서 회원임을 밝히고 기능들을 이용하는 방법으로 ID와 PW를 자체 서비스에서 기록해두어서 사용할 수 있다.
 하지만 3자의 서비스 입장에서는 자신들의 회원들의 정보를 넘겨준다는 것이 있을 수가 없고, 기능을 이용하려는 서비스에서도 회원 정보들을 손실했을 때의 위험을 부담하고 싶지 않을 것이다.
 이처럼 제 3자의 서비스의 회원 정보들을 노출하지 않고 해당 서비스의 권한을 부여해 줄 수 있는 프로토콜이 OAuth이다.
<br>


### OAuth의 구성

1) Client -> 3자 서비스를 이용하려는 나의 서비스
2) Authorization Server -> 3자 서비스 인증 서버
3) Resource Server -> 3자 서비스의 기능을 하용할 수 있는 API 서버
4) Resource Owner -> 3자 서비스를 이용하게 할 수 있는 주체, 즉 나의 서비스를 이용하는 유저
<br>


### OAuth 인증 흐름
![image](https://user-images.githubusercontent.com/34837583/150628393-96204ba1-812c-4f8d-ac38-48996ff2aec2.png)

Authorization Server에서 Resource Server로 요청 시 파라미터로 Client ID, Client Password, Redirect URL을 전달하게 됩니다.
그림 상 Client ID, Client Password를 구하는 시점이 나오지 않았는데 이것은 개발자가 제 3장의 서비스의 특정 기능들을 이용하기 위해서 미리 발급받은 정보입니다.


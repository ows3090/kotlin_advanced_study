
## DI(Dependency Injection)
하나의 객체에 다른 객체의 의존성을 주입하는 기술.

ex) No Dependency Injection Use
```java
public class Operation_System {}

public class Phone {
	private Operation_System os;

	public Phone(){
		os = new Operations_System();
	}
}
```
<br>

ex) Dependency Injection Use
```java
public class Phone{
	private Operation_System os;
	
	public void setOS(Operations_System os){
		this.os = os;
	}
}
```

기본적으로 의존성이 깊은 컴포넌트 사이에서 하나의 클래스만 변경되더라도 의존성이 있는 컴포넌트도 변경이 필요할 수 있다.
이를 방지하기 위해서 DI를 사용함으로써 프레임워크에 주입을 하도록 하여 관심사를 분리할 수 있습니다.
<br>

```java
public static void main(String[] args){
	Operation_System os1 = new Android();
	Operation_System os2 = new MacOS();

	Phone p1 = new Phone(os1);
	Phone p2 = new Phone(os2);
}
```

Main 함수에서 직접 Phone 클래스에 생성자가 메소드를 통해서 관련있는 클래스를 제공하였습니다.
처음 코드에서는 Phone에서 직접 Operation_System을 관리했지만, DI를 사용하여 외부에서 직접 제공할 수 있도록 변경하였는데
이를 "제어의 역전"이라고 합니다. 즉 제어권을 개발자가 아닌 제 3자(프레임워크, 외부 파일)이 가지게 하는 것입니다.
<br><br>

## DI 장단점
장점
1.  주입되는 객체를 stub이나 Mock 객체를 생성하여 테스트 사용, 단위테스트 용이
2. 클래스 간의 결합도를 낮춘다. 즉, 유지보수성이 높아짐
<br>

단점
1. 의존성 주입을 사용하여 코드 분석과 디버깅이 어렵고 가독성 좋지 않음
2. 프레임워크마다 다르지만 컴파일 타임에 Annotation Processer를 사용하여 빌드 타임이 길어짐
<br>

## Koin
1. 경량화된 DI 프레임워크로 Kotlin DSL로 만들어짐
2. 다른 프레임워크에 비해 Annotation이 없어서 컴파일 시간이 영향을 주진 않음
3. 런타임에 의존성을 주입하기 때문에 앱 성능이 저하, Application 시작 시 오브젝트 그래프 생성
4. Service Locator 패턴과 유사
<br>

## TDD(Test Driven Development)
소프트웨어 개발 5단계 : 요구사항 분석 - 설계 - 구현 - 테스트 - 유지보수 
하루 빨리 변화는 시대에 애자일 방법론이 대두되고 있으며 5단계를 거치지 않고 빠르게 개발 후 피드백 수용하는 방식으로 변화
이와 더불어 테스트의 중요성이 커져가서 TDD 방식으로 개발 진행
실패된 Test 코드를 작성한 후 수정하고 구현하는 방식으로 Test가 개발을 주도적으로 이끌어 나가는 방식

1. To DO 아이템 데이터 넣고 리스트 확인
2. 아이템 Update, Delete 동작 확인
3. 상세화면 데이터 확인
4. 상세화면 데이터 수정 및 삭제 동작 확인

다음과 같은 사항은 미리 테스트로 검증 후 개발을 하는 방식이 TDD 입니다.
<br>



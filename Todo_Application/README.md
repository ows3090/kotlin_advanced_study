
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
이를 "제어의 역전"이라고 합니다.
<br>

## DI 장단점
장점
1.  주입되는 객체를 stub이나 Mock 객체를 생성하여 테스트 사용, 단위테스트 용이
2. 클래스 간의 결합도를 낮춘다. 즉, 유지보수성이 높아짐
<br>

단점
1. 의존성 주입을 사용하여 코드 분석과 디버깅이 어렵고 가독성 좋지 않음
2. 프레임워크마다 다르지만 컴파일 타임에 Annotation Processer를 사용하여 빌드 타임이 길어짐
<br>


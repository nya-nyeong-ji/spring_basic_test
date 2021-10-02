# Step-07 Embedded
일전에 사용되었던 Embedded 에 대하여 좀 더 정리해 보겠습니다.<br>

## Point
* 자료형의 통일
* 풍부한 객체들
* 재사용성

## 자료형의 통일
```java
class Example {
    ...
    @Embedded
    private Email email;
    ...
}

class Email {
    @Email
    @Colum(name = "email", nullable = false, unique = true)
    private String email;
}
```

위 과정처럼 email 을 단순한 String 에서 Email 객체로 변경할 경우 email 의 안정성이 높아집니다.<br>
개발에 있어 단순 String 을 사용하는 것이 편리할 수 있으나 직전에 언급된 안정성이 달라집니다.<br>

## 풍부한 객체들
```java
class Email {
    ...
    public String getHost() {
        int idx = value.indexOf("@");
        return value.substring(idx);
    }
    
    public String getId() {
        int idx = value.indexOf("@");
        return value.substring(0, idx);
    }
}
```

이메일을 사용한다면 String 을 사용하던 Email 을 사용하던 위 예시처럼 Id, Host 를 가져오는 메서드가 필요합니다.<br>
그렇다고 이를 위 Email 을 사용할 객체(Example) 등 에서 설명하게 될 경우 해당 객체의 책임이 과하게 집중될 것입니다.<br>
Email 객체에서 이를 실행할 경우 getId(), getHost() 등의 메서드를 Email 이 사용되는 곳 어디서든 사용이 가능해집니다.

## 재사용성
홈쇼핑 거래를 생각해 봅시다<br>
홈쇼핑 거래에 사용되는 송금 정보, 받는 정보, 물건 정보 등이 존재할 겁니다<br>
송금 정보는 다시 주소, 금액, 보낸 이 등으로 나누어질 겁니다.<br>
이렇듯 간단한 객체를 하나 만드는 것에도 수많은 객체들이 반복적으로 사용됩니다.<br>
하지만 Embedded 를 사용하면 재사용이 용이해집니다.
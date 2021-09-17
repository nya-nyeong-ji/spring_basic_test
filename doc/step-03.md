# step-03 Embedded, 예외 처리
이전 포스팅에서는 @Valid 를 이용한 예외처리를 했습니다.<br>
하지만 해당 방법만을 사용할 경우 몇가지 단점이 존재했습니다.<br>
@Embedded 를 사용하여 이 단점을 해결해 보겠습니다.

## step-02 의 단점
* 모든 RequestDto 에 대해 어노테이션 검사가 진행됩니다.
* 유효성 검사 로직이 변경될 경우 모든 곳에서 변경이 일어납니다.

## Point
* @Embedded/@Embeddable
* DTO 변경

## @Embedded/@Embeddable
### @Embedded/@Embeddable 적용
```java

public class MemberEntity extends TimeEntity{
    @Embedded
    private Email email;
}

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    @org.hibernate.validator.constraints.Email
    @Column(name = "email", nullable = false, unique = true)
    private String address;

    @Builder
    public Email(String address) {
        this.address = address;
    }
}
```
임베디드 키워드를 통해서 새로운 값 타입을 정의해서 사용할 수 있습니다.<br>
Email 클래스를 생성하고 거기에 Email 칼럼을 매핑하였습니다.

## DTO 변경
### MemberDto.class
```java
public static class SignUpReq {
    @NotEmpty
    private String id;
    @NotEmpty
    private String nickname;
    @Valid
    private Email email;
    @NotEmpty
    private String password;

    @Builder
    public SignUpReq(String id, String nickname, Email email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
    
    public MemberEntity toEntity() {
         return MemberEntity.builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();
    }
}
```
모든 Request Dto에 대한 반복적인 유효성 검사의 어노테이션이 필요했지만 새로운 Email클래스를 바라보게 변경하면 해당 클래스의 이메일 유효성 검사를 바라보게 됩니다.<br>
그 결과 이메일에 대한 유효성 검사는 Embeddable 타입의 Email클래스가 관리하게 됩니다.<br>
비교적 email의 경우는 입력 형태가 변경될 일이 적지만 비밀번호의 조건은 쉽게 변할 수 있습니다. 그럴 때마다 모든 DTO의 로직을 변경시키는 것은 매우 불안한 구조를 야기합니다.



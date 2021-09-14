# Step-01
Spring Boot + JPA를 활용한 Member 생성, 조회, 수정 API를 간단하게 만들어 보겠습니다.

## Point
* 도메인 클래스의 작성
* DTO클래스를 이용한 Request, Response
* Setter 사용 금지

## 도메인 클래스의 작성
```java
@Entity
@Table(name = "member")
@Getter
@NoArgConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    private String id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder
    public MemberEntity(String id, String nickname, String email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public void updateMember(MemberDto.MemberReq dto) {
        this.nickname = dto.getNickname();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
    }
}
```
```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
```

### 제약 조건을 고려할 것
해당 entity와 매칭될 table의 조건을 최대한 고려해서 작성합니다.<br>
null 가능 여부와 길이제한등이 이에 속합니다.

### 생성 일자와 수정 일자의 관리
@CreatedDate와 @ModifiedDate를 사용하면 이를 쉽게 작성할 수 있습니다.<br>
기본적으로 update를 불가능하게 만든뒤 해당 어노테이션들을 이용하여 자동적으로만 수정되도록 만드는 것이 좋습니다.<br>
위 예제에선 Member에서만 사용되었으나 해당 부분은 어디서든 사용될 수 있기 때문에 따로 클래스를 만들어 상속받는 형식으로 만들었습니다.

### 객체 생성의 제약
@NoArgConstructor(access = AccessLevel.PROTECTED)를 사용하여 외부에서의 직접 생성을 방지합니다.<br>
@Builder 어노테이션을 사용한 경우에만 생성되도록 합니다.<br>
빌더 패턴을 이용한 제작은 아래와 같은 장점이 있습니다.
#### 객체를 유연하게 생성할 수 있다.
```java
MemberEntity.builder()
        .id(id)
        .nickname(nickname)
        .email(email)
        .password(password)
        .build();
```
* 입력되는 인자값의 순서는 상관없습니다.
* 입력되는 인자값이 무엇인 지 한눈에 확인이 됩니다.
* 하나의 생성자로 대체가 됩니다.

## DTO 클래스를 이용한 Request, Response
### DTO 클래스
```java
public class MemberDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReq {
        private String id;
        private String nickname;
        private String email;
        private String password;

        @Builder
        public SignUpReq(String id, String nickname, String email, String password) {
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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberReq {
        private String nickname;
        private String email;
        private String password;

        @Builder
        public MemberReq(String nickname, String email, String password) {
            this.nickname = nickname;
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    public static class Res {
        private String id;
        private String nickname;
        private String email;
        private String password;

        public Res(MemberEntity me) {
            this.id = me.getId();
            this.nickname = me.getNickname();
            this.email = me.getEmail();
            this.password = me.getPassword();
        }
    }
}
```

### DTO 클래스가 필요한 이유
Entity의 정보를 변경하는 API가 존재할 때 다음과 같은 문제를 방지할 수 있습니다
* 데이터의 안전성
  * 변경을 원하는 속성이 2개라도 DTO를 사용하지 않는다면 다른 모든 속성까지 넘겨받기 때문에 원치 않는 변경이 발생할 수 있습니다.
  * 그렇기 때문에 해당 속성 이외의 값을 전달해 주지 않는것이 바람직합니다.
  * Response의 경우에도 마찬가지로 원치 않는 정보까지 공개하는 것을 방지하기 위해 이를 따로 설정해 두는 것이 바람직합니다.
* 명확해지는 요구사항
  * 변경이나 공개를 원하는 API에서 해당 Request, Response 가 무엇을 변경하고, 조회하는 지를 확실히 알 수 있습니다.
    
## Setter 사용 금지
JPA에서는 영속성이 있는 객체에 Setter 메서드를 사용하여 데이터베이스의 DML이 가능합니다.<br>
하지만 이를 무분별하게 사용하게 될 경우 의도에 없던 변경이 일어날 가능성이 있으며 이는 곧 안정성이 사라지는 것을 의미합니다.<br>
또한 무제가 발생하였을 때 이를 확인하는 것이 어렵습니다.<br>
이러한 점에서 DTO를 사용할 경우 안정성 확보 및 수정 위치 확인등이 용이합니다.
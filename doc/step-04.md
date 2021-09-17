# step-04 : Embedded 를 이용한 Password 처리
지난 시간 이용했던 @Embedded 와 BCryptPasswordEncoder 를 이용하여 비밀번호를 구현해 보겠습니다.

## Point
* Embedded 타입의 class 정의

## Embedded 타입의 class 정의
### 사전 요구사항
* 비밀번호를 3회 이상 틀렸을 시에는 더이상 시도할 수 없다.
```java
public class Password {

    @Column(name = "password", nullable = false)
    private String value;

    @Column(name = "failed_cnt", nullable = false)
    private int failed_cnt;

    @Builder
    public Password (final String value) {
        this.value = encodePassword(value);
    }

    public void changePassword(final String newPassword, final String oldPassword) {
        if (isMatched(oldPassword)) {
            value = encodePassword(newPassword);
        }
    }

    public boolean isMatched(final String rawPassword) {
        if (failed_cnt >= 3) {
            throw new PasswordFailedException();
        }
        final boolean matches = isMatches(rawPassword);
        updateFailedCount(matches);
        return matches;
    }

    private void updateFailedCount(boolean matches) {
        if (matches) {
            resetFailedCount();
        }
        else {
            increaseFailedCount();
        }
    }
}
```
객체의 변경이나 질의는 객체 내부에서 해결되어야 합니다.<br>
위 요구사항을 만족시키는 로직은 전부 Password class 내부에 있고 Password 객체를 통하여 모든 작업이 이루어집니다.<br>
이로써 Password Class 책임이 명확해집니다.<br>
@Embedded 를 사용하지 않고 MemberEntity 에서 해당 작업을 진행할 경우 MemberEntity 의 책임과 비중이 커지게 됩니다.

## 결론
핵심 도메인을 @Embedded 로 분리하여 책임을 분산하고 재사용성을 높여줄 수 있다.
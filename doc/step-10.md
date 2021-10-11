# Step-10 Properties
개발 중 Properties 설정값을 가져와야 할 상황이 생깁니다.<br>
실제로 Properties 의 설정값을 가져오는 방법은 여러가지가 있습니다.<br>
그 방법들 중 일부를 소개해 비교해 보도록 하겠습니다.<br>

## Point
* Properties
* Environment
* ConfigurationProperties

## Properties
```yml
user:
  name: park
  age: 26
  email: nickname@domain.net
  address: somewhere_in_korea
  amount: 1000
```

위 예시는 application.yml 의 properties 부분입니다.<br>
properties 의 속성은 위의 것과 같습니다.

## Environment
```java
public class EnvSamplePropertiesRunner implements ApplicationRunner {

    private final Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final String name = env.getProperty("user.name");
        final int age = Integer.valueOf(env.getProperty("user.age"));
        final String email = env.getProperty("user.email");
        final String address = env.getProperty("user.address");
        final int amount = Integer.valueOf(env.getProperty("user.amount"));

        log.info("=======================================================");
        log.info(name);
        log.info(String.valueOf(age));
        log.info(email);
        log.info(address);
        log.info(String.valueOf(amount));
        log.info("=======================================================");
    }
}
```

일반적으로 가장 쉽게 사용할 수 있는 Environment 를 활용한 방법입니다.<br>
Properties 에 정의된 것을 key 값으로 찾아옵니다.<br>

위 Environment 방식은 쉬운 만큼 분명한 단점이 존재합니다.

### 정확한 자료형의 확인이 어려움
key 값으로 선언하는 시점에서 어느 정도 유추할 수 있지만 어디까지나 유추하는 것입니다.<br>
하지만 어디까지나 유추하는 입장이며 정확하게 확인하기 위해서는 application.yml 을 확인해야 합니다.<br>
또한 amount 의 경우 값이 1000이였기 때문에 int 형을로 바인딩시켰지만 이 값이 소수로 변경될 수 도 있습니다.<br>
이렇게 자료형을 임의로 정해야 하는 상황 역시 이러한 문제에서 비롯되었다고 할 수 있습니다.<br>

### 변경 시 관리의 어려움
email 의 킷값이 email-address 로 변경되었을 시 getProperty() 메서드로 바인딩 시킨 부분들은 모두 email-address 로 변경해야 합니다<br>
일일이 변경하는 것도 문제지만 실수가 있었을 경우 이를 runtime 이후에 알 수 있습니다.<br>
NullPointException 이 발생하기 전까지는 확인이 어렵습니다.<br>

## ConfigurationProperties
```java
@Configuration
@ConfigurationProperties(prefix = "user")
@Validated
@Getter
@Setter
public class SampleProperties {
    @NotEmpty
    private String name;
    private int age;
    @Email
    private String email;
    private String address;
    private double amount;
}

@Component
@AllArgsConstructor
@Slf4j
public class SamplePropertiesRunner implements ApplicationRunner {

    private final SampleProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final String name = properties.getName();
        final int age = properties.getAge();
        final String email = properties.getEmail();
        final String address = properties.getAddress();
        final double amount = properties.getAmount();

        log.info("=======================================================");
        log.info(name);
        log.info(String.valueOf(age));
        log.info(email);
        log.info(address);
        log.info(String.valueOf(amount));
        log.info("=======================================================");
    }
}
```

위 Environment 방식의 단점을 보안할 수 있는 방법은 ConfigurationProperties 를 이용한 POJO 객체를 이용한 것입니다.<br>
ConfigurationProperties 방법은 다음과 같은 장점을 가집니다.<br>

### Validation
```yaml
user:
  name:   //필수 값 -> @NotEmpty
  email:  //이메일 형식 준수 -> @Email
```
Validate 검사가 진행이 가능합니다.<br>

### 빈으로 등록되어 재사용성이 높습니다
SamplePropertiesRunner 클래스의 경우 SampleProperties 를 의존성 주입을 받아서 다른 빈에서 재사용성이 높습니다<br>
또한 user 의 응집력또한 높습니다.<br>
이렇게 한 객체에 사용될 name, email, ... 등을 캡슐화하여 응집력을 높이는 겁니다.

### 그 외
* Relaxed Binding 으로 properties 킷값을 유연하게 지정할 수 있습니다.
* SampleProperties 에 새로운 properties 를 추가될 경우에도 사용하기 용이합니다.
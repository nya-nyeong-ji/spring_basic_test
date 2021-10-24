# Step-11 Properties Environment 설정

properties.yml 설정 파일을 이용하여 environment 를 설정할 수 있습니다.<br>
설정 정보는 어플리 케이션 코드와 분리되어 관리되고 각 환경에 따라 달라지는 정보들은 각 properties 파일에서 관리되는 것이 좋습니다.

<table>
    <thead>
        <tr>
            <th>environments</th>
            <th>설명</th>
            <th>파일명</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>local</td>
            <td>로컬 개발 환경</td>
            <td>application-local.yml</td>
        </tr>
    </tbody>
    <tbody>
        <tr>
            <td>dev</td>
            <td>개발 환경</td>
            <td>application-dev.yml</td>
        </tr>
    </tbody>
    <tbody>
        <tr>
            <td>prod</td>
            <td>운영</td>
            <td>application-prod.yml</td>
        </tr>
    </tbody>
</table>

## Point
* application.yml
* application-{env}.yml
* env 설정 방법

## application.yml
```yml
server:
  port: 8080
```
모든 환경에서 공통적으로 사용할 정보들을 작성하게 됩니다.<br>
모든 환경에 적용될 정보들이기 때문에 코드의 중복, 변경에 이점이 있습니다<br>

## application-{env}.yml
```yml
user:
  name: park
  age: 26
  email: nickname@domain.net
  address: somewhere_in_korea
  amount: 1000

spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/test
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    open-in-view: false
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        show_sql: true
        format_sql: true

    jackson:
    serialization:
      WRITE_DATES_AS_TIMESTAMPS: false

logging:
  level:
    root: info
    org:
      hibernate:
        type: trace
```

각 개발환경에 맞는 properties 설정을 정의합니다<br>
대표적으로는 데이터베이스, 외부 설정 정보 등이 있습니다.<br>
application.yml 에서 정의된 설정들은 자동으로 적용됩니다.

## env 설정 방법
```yml
spring:
  properties:
    active: local

server:
  port: 8080
```
* properties.active 에 원하는 env 를 작성합니다.

## 우선 순위
외부 환경 설정에 대한 우선순위는 [Spring-Boot Document](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config) 에 표기되어있습니다.<br>
실제 사용시 반드시 유념해 주시기 바랍니다.
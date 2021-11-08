# Step-13 Query DSL 을 이용한 페이징 API
지난 포스팅에서는 페이징 API 를 다루어 보았습니다.<br>
이번 포스팅에서는 Query DSL 을 이용한 검색 페이징을 만들어 보겠습니다.<br>

## Point
* 기초 작업
* Controller
* Service

## 기초 작업
Gradle 기준 아래의 코드를 추가해 주세요.
```gradle
plugins {
	id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}

dependencies {
	implementation "com.querydsl:querydsl-jpa"
}


def querydslDir = "$buildDir/generated/querydsl"

querydsl {
	jpa = true
	querydslSourcesDir = querydslDir
}

sourceSets {
	main.java.srcDir querydslDir
}

configurations {
	querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
	options.annotationProcessorPath = configurations.querydsl
}
```
위 코드들은 QueryDSL 을 사용하기 위해 성정해 줘야하는 것들입니다.<br>
IDE 기준으로 gradle task compileQuerydsl 을 실행해주면 설정된 경로에 Q-type 클래스 파일이 생성됩니다.


## Controller
```java
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSearchService memberSearchService;
    
    @GetMapping(value = "/page")
    public Page<MemberDto.Res> getMembers(
            @RequestParam(name = "type") final MemberSearchType type,
            @RequestParam(name = "value", required = false) final String value,
            final PageRequest pageable) {
        return memberSearchService.search(type, value, pageable.of()).map(MemberDto.Res::new);
    }
}

public enum MemberSearchType {
    EMAIL,
    NICKNAME,
    ALL
}
```

* type 은 검색 페이징을 위한 type 을 의미합니다. 본 예제에서는 email, nickname 그리고 전체 등을 사용했습니다.
* value 는 type 에 대한 value 를 의미합니다.
* PageRequest 는 12장을 참조해 주세요.

검색을 위한 type 의 정리는 String 으로 해도 무방하나 enum 으로 관리하는 것이 효율적입니다.<br>
차후의 예외 처리, 추가 및 수정 작업에 있어서 enum 을 사용하는 것이 효율적이기 때문입니다.

## Service
```java
@Service
@Transactional(readOnly = true)
public class MemberSearchService extends QuerydslRepositorySupport {

    public MemberSearchService() {
        super(MemberEntity.class);
    }

    public Page<MemberEntity> search(final MemberSearchType type, final String value, final Pageable pageable) {
        final QMemberEntity member = QMemberEntity.memberEntity;
        final JPQLQuery<MemberEntity> query;

        switch (type) {
            case EMAIL:
                query = from(member)
                        .where(member.email.address.likeIgnoreCase(value + "%"));
                break;
            case NICKNAME:
                query = from(member)
                        .where(member.nickname.likeIgnoreCase(value + "%"));
                break;
            case ALL:
                query = from(member)
                        .fetchAll();
                break;
            default:
                throw new IllegalArgumentException();
        }

        final List<MemberEntity> list = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());
    }
}
```

QuerydslRepositorySupport 를 이용하면 손쉽게 동적 쿼리를 만들 수 있습니다.<br>
객체 기반으로의 쿼리를 만드는 것으로 타입 세이프의 강점을 그대로 사용할 수 있습니다.<br>
QuerydslRepositorySupport 추상 클래스를 상속받고 기본 생성자를 사용하여 조회 대상의 엔티티 클래스를 지정합니다.<br>

<br>

search(...) 메서드는 컨트롤러에서 넘겨받은 type, value, pageable 를 기반으로 동적 쿼리를 만드는 작업을 진행합니다.<br>

QueryDsl 에서 생성한 QMemberEntity 객체를 기반으로 동적 쿼리 작업을 진행합니다.<br>
switch 문을 사용해서 각 타입에 맞는 쿼리문을 작성하고 있습니다.<br>
우리가 작성하는 일반적인 쿼리문과 크게 다르지 않기 때문에 해당 코드를 이해하기 쉽다는 것이 QueryDsl 이 가지는 장점 중 하나입니다.<br>


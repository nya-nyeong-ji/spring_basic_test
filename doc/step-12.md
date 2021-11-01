# Step-12 페이징 API

페이징 처리의 경우 거의 모든 웹의 개발에서 사용됩니다.<br>
실제로 사용하게 될 경우 복잡하고 어려운 구현은 아닙니다.<br>
데이터베이스 마다 페이징 쿼리가 조금씩 달라지는 것도 복잡도를 높이는 이유 중 하나가 됩니다.<br>
이번 포스팅에서는 JPA 를 사용하여 간편한 페이징을 구현해보겠습니다.

## Point
* 기초 작업
* Sample Code
* 개선

## 기초 작업
이전에 작성한 MemberRepository 는 JpaRepository 를 상속하고 있습니다.<br>
그리고 JpaRepository 는 PagingAndSortingRepository 를 상속하고 있습니다.<br>
페이징 작업은 MemberRepository 를 작성한 시점에서 대부분 끝났습니다.

## Sample Code
```java
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
    @GetMapping
    public Page<MemberDto.Res> getMembers(final Pageable pageable) {
        return memberService.findALL(pageable).map(MemberDto.Res::new);
    }
}

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    @Transactional(readOnly = true)
    public Page<MemberEntity> findALL(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }
}
```
컨트롤러에서 Pageable 인터페이스를 받고 repository 메서드 findAll(pageable) 를 넘기기만 하면 됩니다.

Response 에 자세한 정보들이 담겨있습니다.<br>
페이징 하단의 네비게이션을 작성할 때 유용한 정보가 있습니다.<br>
이렇게 Spring Data JPA 를 이용하면 페이징 기능을 간편하게 만들 수 있습니다.

### 요청

### 응답

### 다양한 요청


Pageable 은 다양한 요청을 이용하여 기본적인 기능을 제공합니다.<br>
page 는 실제 페이지를 의미하고 size 는 content 의 size 를 의미합니다.<br>
sort 는 페이징을 처리 시 정렬 값을 의미합니다.<br>

## 개선
위 Pageable 에 개선할 점이 있습니다.<br>
size 에 limit 이 없습니다.<br>

```java
public final class PageRequest {
    private int page;
    private int size;
    private Sort.Direction direction;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int default_size = 10;
        int max_size = 50;
        this.size = size < max_size ? default_size : size;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }
    //getter

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, "createDate");
    }
}
```

Pageable 을 대체하는 PageRequest 클래스를 작성합니다.<br>
* setPage 를 통해 0보다 작은 페이지를 요청했을 경우 1페이지를 시작 페이지로 지정합니다.
* setSize 를 통해 요청 사이즈를 50 이상으로 설정할 경우 지본 사이즈인 10으로 설정 합니다.
* of 를 통해 PageRequest 객체를 응답합니다.

### 컨트롤러
```java
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
    @GetMapping(value = "/page")
    public Page<MemberDto.Res> getMembers(final PageRequest pageable) {
        return memberService.findALL(pageable.of()).map(MemberDto.Res::new);
    }
}

```

### 요청
page 를 기본 사이즈 50이 넘는 500으로 설정했습니다.<br>
정상적으로 작동할 경우 기본 사이즈인 10으로 페이징 됩니다.

## 결론
Spring Data JPA 의 경우 데이터베이스의 종류를 따지지 않고 쉽게 페이징을 구현할 수 있습니다.<br>
거기에 Pageable 을 사용할 수 있습니다.<br>
하지만 PageRequest 같은 객체를 두어 관리하는 것이 더 효과적입니다.
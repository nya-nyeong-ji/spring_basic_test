# Step-14 Query DSL 을 이용한 SQL
지난 포스팅에서는 Query DSL 을 이용한 페이징에 대해서 알아보았습니다.<br>
이번 시간엔 그 이외에도 사용되는 방법에 대해서 알아보도록 하겠습니다.<br>

## Point
* 복잡한 쿼리 사용
* 간단하고 반복적인 쿼리 사용

## 복잡한 쿼리 사용

### 기존 Repository
```java
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    MemberEntity findByEmail(Email email);
}
```
기존의 Repository 의 경우 복잡한 쿼리를 사용할 때 어려움이 존재합니다.<br>
findByEmail 의 경우와 같이 특정 유일한 값을 조회하는 것은 쿼리 메서드로 표현하는 것이 가독성이 좋습니다.<br>

하지만 쿼리가 복잡해 진다면 쿼리 메서드 만으로 표현하기 어려워 집니다.<br>
@Query 어노테이션을 사용하여 JPQL 을 작성하는 방법도 존재합니다.<br>
하지만 @Query 어노테이션의 경우 type safe 하지 않아 유지 보수가 어렵습니다.<br>

이전 포스팅의 방법을 사용하면 해결할 수 있지만 수많은 조회용 DAO 를 생성해야 합니다.
이러한 문제를 상속관계를 이용하여 Q-class 를 객체를 통해 DAO 에 접근할 수 있는 패턴을 알아보겠습니다.

### Class Diagram

MemberCustomRepositoryImpl 에 복잡한 쿼리를 구현한 뒤 MemberRepository 를 이용하여 마치 JpaRepository 를 사용하듯  사용할 수 있습니다.

### Code
```java
public interface MemberRepository extends JpaRepository<MemberEntity, String>, MemberCustomRepository, QuerydslPredicateExecutor<MemberEntity> {

    MemberEntity findByEmail(Email email);
}

public interface MemberCustomRepository {
    List<MemberEntity> findRecentlyRegistered(int limit);
}

@Transactional(readOnly = true)
public class MemberCustomRepositoryImpl extends QuerydslRepositorySupport implements MemberCustomRepository {

    public MemberCustomRepositoryImpl() {
        super(MemberEntity.class);
    }

    @Override
    public List<MemberEntity> findRecentlyRegistered(int limit) {
        final QMemberEntity member = QMemberEntity.memberEntity;
        return from(member)
                .limit(limit)
                .orderBy(member.createDate.desc())
                .fetch();
    }
}

```

MemberCustomRepository 를 생성합니다.<br>
MemberRepository 에 방금 만든 MemberCustomRepository 인터페이스를 상속합니다.<br>
MemberCustomRepositoryImpl 는 실제 Querydsl 를 이용하여 MemberCustomRepository 의 세부 구현을 진행합니다.<br>

### 결론
Repository 가 복잡해지는 것은 유지보수 측면에서 불안전합니다.<br>
또한 @Query 어노테이션은 type safe 가 되지 않습니다.<br>
이러한 문제를 QueryDsl 을 이용하여 세부적이고 복잡한 코드는 따로 구현한 체 Repository 가 이를 사용할 수 있도록 만드는 것이 핵심입니다.

## 간단하고 반복적인 쿼리 사용
기존의 메서드 쿼리의 경우 간단하고 직관적인 구조를 가지고 있으나 필요에 따라 계속해서 추가해 줘야하는 단점이 있었습니다.<br>
이러한 경우 QueryPredicateExecutor 가 효과적입니다.

### QuerydslPredicateExecutor
```java
public interface QuerydslPredicateExecutor<T> {

  Optional<T> findById(Predicate predicate);  

  Iterable<T> findAll(Predicate predicate);   

  long count(Predicate predicate);            

  boolean exists(Predicate predicate);        

  // … more functionality omitted.
}
```
QuerydslPredicateExecutor 의 일부분입니다.<br>
Predicate 를 매게 변수로 잡고 있기 때문에 Predicate 를 통해서 새로운 쿼리를 만들 수 있습니다.

### Repository 에 적용하기
```java
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String>, MemberCustomRepository, QuerydslPredicateExecutor<MemberEntity> {

    MemberEntity findByEmail(Email email);
}
```
추가적으로 상속해 주기만 하면 끝입니다.

### 결론
실제 업무에선 조회용 쿼리를 가장 많이 사용하게 됩니다.<br>
별다른 조취를 취하지 않을 경우 중복코드는 늘어만 갑니다.<br>
그렇게 되면 자연스레 구현체나 의존도가 높아지기 마련입니다.<br>
<br>
이러한 문제를 위와 같이 객체지향적 관점으로 풀어내어 Repository 를 통해 DAO 를 제공하게 되고 세부 구현들을 숨길 수 있는 것이 바람직 하다고 생각합니다.
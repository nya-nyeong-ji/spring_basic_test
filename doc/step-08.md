# Step-08 One-To-One Tip

어떤 객체를 다룰 때 1:1 의 관계를 가진다면 이를 OneToOne 으로 표현할 수 있습니다.<br>
주문과 쿠폰을 객체로 예시를 들어 보겠습니다.<br>

## Point
* 예시
* 외래키의 위치
* 양방향 연관 관계
* 제약 조건

## 예시
```java
public class CouponEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "discount_amount")
    private double discountAmount;

    @Column(name = "use")
    private boolean use;
    
    @OneToOne()
    private OrderEntity order;
}


public class OrderEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "price")
    private double price;

    @OneToOne
    @JoinColumn()
    private CouponEntity coupon;
}

```

## 외래키의 위치
```java
// order 가 연관 관계의 주인일 경우
@OneToOne
@JoinColumn(name = "coupon_id", referencedColumnName = "id")
private CouponEntity coupon;

@OneToOne(mappedBy = "coupon")
private OrderEntity order;

// coupon 이 연관 관계의 주인일 경우
@OneToOne(mappedBy = "order")
private CouponEntity coupon;

@OneToOne
@JoinColumn(name = "order_id", referencedColumnName = "id")
private OrderEntity order;
```
1:N 의 관계일 경우 N 쪽에서 외래키를 관리합니다<br>
JPA 상에서는 외래키를 가지는 쪽이 연관 관계의 주인이 됩니다<br>
연관 관계의 주인만이 데이터 베이스에 매핑되고 외래 키를 관리할 수 있습니다.

SQL 을 실행시킨다는 가정하에<br>
order 테이블에 coupon_id 를 저장하기 때문에 주문 SQL 은 한번만 실행됩니다.<br>
반면에 coupon 이 연관 관계의 주인일 경우 coupon 에 order 의 외래키 저장을 한 번, order 에 insert 한 번<br>
총 두번의 SQL 이 실행됩니다.<br>

### Order 가 주인일 경우 단점: 연관 관계 변경 시 취약
기존 요구사항은 주문 한 개에 쿠폰은 한 개만 적용한다는 전제입니다.<br>
하나의 주문에 여러 개의 쿠폰이 적용되는 기능이 추가될 때 변경하기 어렵다는 단점이 있습니다.<br>
이러할 경우 OneToOne 에서 OneToMany 로 변경해야 하나 이를 실제 서비스 중에 이루어지기 어렵습니다.<br>

## 양방향 연관 관계
```java
// Order가 연관관계의 주인일 경우 예제
class CouponEntity {
    ...
    // 연관관계 편의 메소드
    public void use(final OrderEntity order) {
        this.order = order;
        this.use = true;
    }
}

class OrderEntity {
    private CouponEntity coupon; //(1)
    ...
    // 연관관계 편의 메소드
    public void applyCoupon(final CouponEntity coupon) {
        this.coupon = coupon;
        coupon.use(this);
        price -= coupon.getDiscountAmount();
    }
}

// 주문 생성시 1,000 할인 쿠폰 적용
public OrderEntity order() {
    final OrderEntity order = OrderEntity.builder().price(1_0000).build(); // 10,000 상품주문
    CouponEntity coupon = couponService.findById(1); // 1,000 할인 쿠폰
    order.applyCoupon(coupon);
    return orderRepository.save(order).get();
}
```

연관 관계의 주인이 해당 참조할 객체를 넣어줘야 데이터 칼럼에 외래키가 저장됩니다.<br>
Order 가 연관 관계의 주인이면 (1)번 맴버 필드에 Coupon 을 넣어줘야 데이터 베이스 Order 테이블에 coupon_id 칼럼에 저장됩니다.
양방향 연관 관계일 경우 위처럼 연관 관계 편의 메소드를 작성하는 것이 편리합니다.<br>
단순히 Order 객체에 Coupon 이 있기 때문에 Coupon 에 Order 가 있다고 오해하기 쉽습니다.<br>
때문에 순수한 객체까지 고려한 양방향 관계를 고려하는 것이 바람직합니다.

## 제약 조건
모든 주문에 할인 쿠폰이 적용되게 할려면 nullable 옵션을 사용해 주면 됩니다.<br>
Not Null 제약 조건을 준수해서 안정성이 보장됩니다.<br>
이렇게 될 경우 이너 조인을통해서 SQL 을 만들어주게 되고 이것은 아우터 조인보다 효율적입니다.<br>

# step-05 : Many To One 의 관계 설정
관계 설명을 위하여 배송과 배송 상태를 가지고 있는 배송 로그를 사용하겠습니다.<br>
배송과 배송 로그는 1:N의 관계를 가집니다.

### 관계 설정
```java
public class DeliveryEntity {

    @Id
    @GeneratedValue
    private long id;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DeliveryLogEntity> logs = new ArrayList<>();
    
    ...
}

public class DeliveryLogEntity extends TimeEntity{

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = false)
    private DeliveryStatus status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false, updatable = false)
    private DeliveryEntity delivery;
    
    ...
}
```

* 이전 포스팅에서 설명되었던 @Embedded 와 TimeEntity 를 사용해줍니다.<br>

### 1:N 관계를 다룰 때의 팁
* 1:N 의 관계를 다룰 때 CascadeType.PERSIST 로 설정합니다.<br>
    * @Embedded 를 사용해서 표현된 다른 Entity 까지 생성됩니다.
* 새로운 객체를 만들 때 null 보단 new 객체를 사용합니다.<br>
    * 주로 List 를 사용하게 되는데 이는 새로운 객체를 생성할 때 바로 사용해 주기 위함입니다.<br>
    * null 상태보단 empty 상태가 더 직관적입니다.<br>
    
## 객체의 상태는 언제나 자기 자신이 관리할 것
```java
public class DeliveryLogEntity extends TimeEntity{

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = false)
    private DeliveryStatus status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false, updatable = false)
    private DeliveryEntity delivery;
    
    ...
    
    private void verifyNotYetDelivering() {
        if (isNotYetDelivering()) throw new DeliveryAlreadyDeliveringException();
    }

    private boolean isNotYetDelivering() {
        return status != DeliveryStatus.PENDING;
    }
}
```

객체의 상태는 자기 자신이 관리해야 합니다.<br>
즉 자기 자신이 생성되지 못할 이유도 자기 자신이 관리해야 합니다.<br>
위 설명의 verifyNotYetDelivering 등 처럼 이를 확인하거나 관리하는 로직은 이 클래스 내부에 존재해야 합니다.

## 배송 로그의 저장
```java
public class DeliveryService {

    private DeliveryRepository deliveryRepository;

    public DeliveryEntity create(DeliveryDto.CreationReq dto) {
        final DeliveryEntity delivery = dto.toEntity();
        delivery.addLog(DeliveryStatus.PENDING);
        return deliveryRepository.save(delivery);
    }

    public DeliveryEntity updateStatus(long id, DeliveryDto.UpdateReq dto) {
        final DeliveryEntity delivery = findById(id);
        delivery.addLog(dto.getStatus());
        return delivery;
    }
}
```

저장 당시에는 'pending' 으로 로그를 생성하여 저장한 뒤 수정할 때에는 addLog 를 이용합니다.

## 마무리
도메인을 디자인 할 때면 게시판 - 댓글, 배송 - 배송 상태 등 과 같이 ManyToOne 형태가 자주 보입니다.<br>
그렇기에 한 번 다루는 것이 좋아 보이는 주제였으나 확실히 팁에 가까운 내용이 되었습니다.
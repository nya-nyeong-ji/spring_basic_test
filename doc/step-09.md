# Step-09 One To Many 관계 설정 TIP
일전에 OneToMany 관계에 대한 포스팅을 다룬 적이 있습니다.<br>
그 당시에는 객체가 풍부한 것에 초점을 두었습니다.<br>
이번에는 관계 설정에 대하여 좀 더 살펴보도록 하겠습니다.

## POINT
* Delivery 저장
* orphanRemoval(고아 객체)

## Delivery 저장
1:N 관계에서는 N쪽이 외래 키를 관리하게 됩니다.<br>
JPA 상에서는 외래 키가 갖는 쪽이 연관 관계의 주인이 되고 연관 관계의 주인만이 데이터베이스 연관 관계와 매핑되고 외래 키를 관리할 수 있으므로 DeliveryLogEntity 는 DeliveryEntity 를 관리하게 됩니다.<br>
하지만 DeliveryLogEntity 는 DeliveryEntity 상태를 저장하는 로그의 성격을 띄기 때문에 핵심 비지니스 로직을 DeliveryEntity 에서 관리해야 합니다.<br>

### 편의 메소드
```java
public class DeliveryEntity{
    public void addLog(DeliveryStatus status) {
        this.logs.add(buildLog(status));
    }
}

public class DeliveryLogEntity{
    @Builder
    public DeliveryLogEntity(DeliveryStatus status, DeliveryEntity delivery) {
        this.delivery = delivery;
    }
}

public class DeliveryService {
    public DeliveryEntity create(DeliveryDto.CreationReq dto) {
        final DeliveryEntity delivery = dto.toEntity();
        delivery.addLog(DeliveryStatus.PENDING);
        return deliveryRepository.save(delivery);
    }
}
```

배송이 시작되면 배송 로그는 반드시 보류 중이라는 가정했을 경우 편의 메소드를 이용하여 두 객체에 모두 필요한 값을 바인딩합니다.<br>

### Cascade Persist 설정
Cascade 를 Persist 로 DeliveryEntity 와 DeliveryLogEntity 가 생성은 되지만 insert query 가 실행되지 않습니다.<br>

## orphanRemoval(고아 객체)
JPA 는 부모 엔티티와 연관 관계가 끊어진 자식 엔티티를 자동적으로 제거해 주는 기능이 있습니다.<br>
orphanRemoval(고아 객체) 이라고 합니다.<br>

### DeliveryLogEntity 삭제
```java
public class DeliveryService {
    public DeliveryEntity removeLogs(long id) {
        final DeliveryEntity delivery = findById(id);
        delivery.getLogs().clear();
        return delivery;
    }
}
```
DeliveryEntity 에서 DeliveryLogEntity 를 직관적으로 삭제할 수 있습니다.

### DeliveryEntity 삭제
```java
public class DeliveryService {
    public void remove(long id) {
        deliveryRepository.deleteById(id);
    }
}
```

참조 관계로 인하여 DeliveryEntity 만을 삭제할 수 없습니다.<br>
실제로 DeliveryEntity 삭제 시 hibernate 는 DeliveryLogEntity 를 먼저 삭제하게 됩니다.

### 고아 객체 설정이 없을 경우
DeliveryLogEntity 삭제 같은 경우 실제 객체에서는 clear() 를 사용합니다.<br>
이는 얼핏 삭제된 것처럼 보일 수 있습니다.<br>
하지만 이는 영속성이 있는 데이터를 삭제한 것이 아니기 때문에 DeliveryEntity 조회 시 DeliveryLogEntity 가 그대로 조회됩니다.<br>

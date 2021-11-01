package com.example.spring_basic_test.domain.entity;

import com.example.spring_basic_test.domain.model.Address;
import com.example.spring_basic_test.domain.model.DeliveryStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class DeliveryEntity extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DeliveryLogEntity> logs = new ArrayList<>();

    @Builder
    public DeliveryEntity(Address address) {
        this.address = address;
    }

    public void addLog(DeliveryStatus status) {
        this.logs.add(buildLog(status));
    }

    private DeliveryLogEntity buildLog(DeliveryStatus status) {
        return DeliveryLogEntity.builder()
                .status(status)
                .delivery(this)
                .build();
    }
}

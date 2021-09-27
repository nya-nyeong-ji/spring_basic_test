package com.example.spring_basic_test.domain.entity;

import com.example.spring_basic_test.domain.model.DeliveryStatus;
import com.example.spring_basic_test.exception.DeliveryAlreadyDeliveringException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "delivery_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public DeliveryLogEntity(DeliveryStatus status, DeliveryEntity delivery) {
        this.status = status;
        this.delivery = delivery;
    }

    private void setStatus(final DeliveryStatus status) {
        switch (status) {
            case PENDING:
                pending();
                break;
            case CANCELED:
                cancel();
                break;
            case COMPLETED:
                completed();
                break;
            case DELIVERING:
                delivering();
                break;
        }
    }

    private void pending() {
        this.status = DeliveryStatus.PENDING;
    }

    private void completed() {
        this.status = DeliveryStatus.COMPLETED;
    }

    private void cancel() {
        this.status = DeliveryStatus.CANCELED;
    }

    private void delivering() {
        this.status = DeliveryStatus.DELIVERING;
    }

    private void verifyNotYetDelivering() {
        if (isNotYetDelivering()) throw new DeliveryAlreadyDeliveringException();
    }

    private boolean isNotYetDelivering() {
        return status != DeliveryStatus.PENDING;
    }
}

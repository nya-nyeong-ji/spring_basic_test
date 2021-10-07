package com.example.spring_basic_test.exception;

import com.example.spring_basic_test.domain.model.DeliveryStatus;

public class DeliveryStatusEqualsException extends RuntimeException{

    private DeliveryStatus status;

    public DeliveryStatusEqualsException(DeliveryStatus status) {
        super(status.name() + " It can't be changed the same status.");
        this.status = status;
    }
}

package com.example.spring_basic_test.service;

import com.example.spring_basic_test.domain.entity.DeliveryEntity;
import com.example.spring_basic_test.domain.model.DeliveryStatus;
import com.example.spring_basic_test.domain.repository.DeliveryRepository;
import com.example.spring_basic_test.dto.DeliveryDto;
import com.example.spring_basic_test.exception.DeliveryNoFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
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

    public DeliveryEntity findById(long id) {
        final Optional<DeliveryEntity> delivery = deliveryRepository.findById(id);
        if (delivery == null) throw new DeliveryNoFoundException(id);
        return delivery.get();
    }
}

package com.example.spring_basic_test.domain.repository;

import com.example.spring_basic_test.domain.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
}

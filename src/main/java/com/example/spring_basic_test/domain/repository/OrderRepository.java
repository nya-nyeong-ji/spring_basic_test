package com.example.spring_basic_test.domain.repository;

import com.example.spring_basic_test.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}

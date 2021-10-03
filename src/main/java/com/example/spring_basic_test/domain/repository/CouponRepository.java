package com.example.spring_basic_test.domain.repository;

import com.example.spring_basic_test.domain.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
}

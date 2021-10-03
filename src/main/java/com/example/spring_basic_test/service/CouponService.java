package com.example.spring_basic_test.service;

import com.example.spring_basic_test.domain.entity.CouponEntity;
import com.example.spring_basic_test.domain.repository.CouponRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponEntity findById(long id) {
        return couponRepository.findById(id).get();
    }
}

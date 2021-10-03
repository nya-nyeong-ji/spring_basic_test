package com.example.spring_basic_test.service;

import com.example.spring_basic_test.domain.entity.CouponEntity;
import com.example.spring_basic_test.domain.entity.OrderEntity;
import com.example.spring_basic_test.domain.repository.CouponRepository;
import com.example.spring_basic_test.domain.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponService couponService;

    public OrderEntity order() {
        final OrderEntity order = OrderEntity.builder().price(1_0000).build();
        CouponEntity coupon = couponService.findById(1);
        order.applyCoupon(coupon);
        return orderRepository.save(order);
    }

    public OrderEntity findById(long id) {
        return orderRepository.findById(id).get();
    }
}

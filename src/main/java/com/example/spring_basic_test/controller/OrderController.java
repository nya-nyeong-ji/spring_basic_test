package com.example.spring_basic_test.controller;

import com.example.spring_basic_test.domain.entity.CouponEntity;
import com.example.spring_basic_test.domain.entity.OrderEntity;
import com.example.spring_basic_test.service.CouponService;
import com.example.spring_basic_test.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;
    private CouponService couponService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public OrderEntity getOrders(@PathVariable("id") long id) {
        return orderService.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public OrderEntity getOrders() {
        return orderService.order();
    }

    @RequestMapping(value = "coupons/{id}", method = RequestMethod.GET)
    public CouponEntity getCoupon(@PathVariable("id") long id) {
        return couponService.findById(id);
    }
}

package com.example.spring_basic_test.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "price")
    private double price;

    @OneToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id", nullable = false)
    private CouponEntity coupon;

    @Builder
    public OrderEntity (double price) {
        this.price = price;
    }

    public void applyCoupon(final CouponEntity coupon) {
        this.coupon = coupon;
        coupon.use(this);
        price -= coupon.getDiscountAmount();
    }
}

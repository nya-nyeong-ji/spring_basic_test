package com.example.spring_basic_test.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "discount_amount")
    private double discountAmount;

    @Column(name = "use")
    private boolean use;

    @JsonIgnore
    @OneToOne(mappedBy = "coupon")
    private OrderEntity order;

    @Builder
    public CouponEntity(double discountAmount) {
        this.discountAmount = discountAmount;
        this.use = false;
    }

    public void use(final OrderEntity order) {
        this.order = order;
        this.use = true;
    }
}

package com.example.spring_basic_test.dto;

import com.example.spring_basic_test.domain.entity.DeliveryEntity;
import com.example.spring_basic_test.domain.entity.DeliveryLogEntity;
import com.example.spring_basic_test.domain.model.Address;
import com.example.spring_basic_test.domain.model.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryDto {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class CreationReq {
        @Valid
        private Address address;

        @Builder
        public CreationReq(Address address) {
            this.address = address;
        }

        public DeliveryEntity toEntity() {
            return DeliveryEntity.builder()
                    .address(address)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateReq {
        private DeliveryStatus status;

        @Builder
        public UpdateReq(DeliveryStatus status) {
            this.status = status;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private Address address;
        private List<LogRes> logs;

        public Res(final DeliveryEntity delivery) {
            this.address = delivery.getAddress();
            this.logs = delivery.getLogs()
                    .parallelStream().map(LogRes::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class LogRes {
        private DeliveryStatus status;

        public LogRes(DeliveryLogEntity log){
            this.status = log.getStatus();
        }
    }
}

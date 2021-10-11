package com.example.spring_basic_test.properties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class SamplePropertiesRunner implements ApplicationRunner {

    private final SampleProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final String name = properties.getName();
        final int age = properties.getAge();
        final String email = properties.getEmail();
        final String address = properties.getAddress();
        final double amount = properties.getAmount();

        log.info("=======================================================");
        log.info(name);
        log.info(String.valueOf(age));
        log.info(email);
        log.info(address);
        log.info(String.valueOf(amount));
        log.info("=======================================================");
    }
}

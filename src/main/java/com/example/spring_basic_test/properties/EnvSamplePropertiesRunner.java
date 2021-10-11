package com.example.spring_basic_test.properties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EnvSamplePropertiesRunner implements ApplicationRunner {

    private final Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final String name = env.getProperty("user.name");
        final int age = Integer.valueOf(env.getProperty("user.age"));
        final String email = env.getProperty("user.email");
        final String address = env.getProperty("user.address");
        final int amount = Integer.valueOf(env.getProperty("user.amount"));

        log.info("=======================================================");
        log.info(name);
        log.info(String.valueOf(age));
        log.info(email);
        log.info(address);
        log.info(String.valueOf(amount));
        log.info("=======================================================");
    }
}

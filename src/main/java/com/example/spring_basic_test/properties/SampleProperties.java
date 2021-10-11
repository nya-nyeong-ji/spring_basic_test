package com.example.spring_basic_test.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "user")
@Validated
@Getter
@Setter
public class SampleProperties {
    @NotEmpty
    private String name;
    private int age;
    @Email
    private String email;
    private String address;
    private double amount;
}

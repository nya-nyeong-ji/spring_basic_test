package com.example.spring_basic_test.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    @Column(name = "password", nullable = false)
    private String value;

    @Column(name = "failed_cnt", nullable = false)
    private int failed_cnt;

    @Builder
    public Password (final String value) {
//        this.value = encodingPassword(value);
        this.value = value;
    }

//    public void changePassword(final String newPassword, final String oldPassword) {
//        if (isMatched(oldPassword)) {
//            value = encodePassword(newPassword);
//        }
//    }

    private void updateFailedCount(boolean matches) {
        if (matches) {
            resetFailedCount();
        }
        else {
            increaseFailedCount();
        }
    }

    private void resetFailedCount(){
        this.failed_cnt = 0;
    }

    private void increaseFailedCount() {
        this.failed_cnt++;
    }

//    private boolean isMatches(String rawPassword) {
//        return new BCryptPasswordEncoder().matches(rawPassword, this.value);
//    }

//    private String encodingPassword(final String password) {
//        return BCryptPasswordEncoder().encode(password);
//    }
}

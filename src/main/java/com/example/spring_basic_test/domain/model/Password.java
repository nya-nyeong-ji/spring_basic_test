package com.example.spring_basic_test.domain.model;

import com.example.spring_basic_test.exception.PasswordFailedException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        this.value = encodePassword(value);
    }

    public void changePassword(final String newPassword, final String oldPassword) {
        if (isMatched(oldPassword)) {
            value = encodePassword(newPassword);
        }
    }

    public boolean isMatched(final String rawPassword) {
        if (failed_cnt >= 3) {
            throw new PasswordFailedException();
        }
        final boolean matches = isMatches(rawPassword);
        updateFailedCount(matches);
        return matches;
    }

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

    private boolean isMatches(String rawPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, this.value);
    }

    private String encodePassword(final String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}

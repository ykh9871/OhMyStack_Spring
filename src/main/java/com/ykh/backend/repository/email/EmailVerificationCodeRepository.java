package com.ykh.backend.repository.email;

import com.ykh.backend.entity.email.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, Long> {
    EmailVerificationCode findByEmail(String email);
    void deleteByEmail(String email);
}

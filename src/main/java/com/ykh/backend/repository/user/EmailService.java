package com.ykh.backend.repository.user;


public interface EmailService {
    void sendEmail(String to, String subject, String message) throws Exception;
}


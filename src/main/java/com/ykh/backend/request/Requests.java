package com.ykh.backend.request;

public class Requests {
    public static class DeleteUserRequest {
        public String password;
    }

    public static class FindPasswordRequest {
        public String phoneNumber;
        public String email;
    }

    public static class PasswordChangeRequest {
        public String currentPassword;
        public String newPassword;
    }

    public static class VerifyEmailRequest {

        public String email;
        public String code;
    }
    public static class ValidateTokenRequest{
        public String token;
    }
    public static class PasswordResetRequest{
        public String token;
        public String newPassword;
    }
}

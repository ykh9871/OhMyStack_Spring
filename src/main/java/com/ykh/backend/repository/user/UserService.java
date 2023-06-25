package com.ykh.backend.repository.user;

import com.ykh.backend.dto.user.UserDto;
import com.ykh.backend.dto.user.UserInfoDto;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.service.user.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserService extends UserDetailsService {
    Optional<User> signUpAndSaveNewUserAccount(UserDto accountDto);
    void deleteUser(User user, String password);
    boolean checkPassword(User user, String currentPassword);
    Optional<User> changePassword(User user, String password);
    Optional<User> findUserByEmail(String email);
    void sendEmailVerificationCode(String email) throws Exception; // Exception 키워드 추가
    UserServiceImpl.EmailVerificationStatus verifyEmail(String email, String code);
    String findEmailByPhoneNumber(String phoneNumber);
    void createPasswordResetTokenForUser(User user, String token);
    Optional<User> findUserByPasswordResetToken(String token);
    void sendPasswordResetEmail(String phoneNumber, String email) throws Exception; // Exception 키워드 추가
    void changeNewPassword(User user, String newPassword);
    User getUserFromAuthentication(Authentication authentication);
    void updateUser(User user);
    Optional<User> getByCredentials(final String email, final String password , final PasswordEncoder encoder);
    Optional<User> changeUserInfo(User user, UserInfoDto userInfoDto);
    ResponseEntity<?> getAllUserInfo(User user);
}


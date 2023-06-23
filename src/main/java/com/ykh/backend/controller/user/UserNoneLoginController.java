package com.ykh.backend.controller.user;

import com.ykh.backend.dto.user.UserDto;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.repository.user.UserService;
import com.ykh.backend.request.Requests;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserNoneLoginController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> SignUpUser(@RequestBody UserDto userDto) {
        Optional<User> newUser = userService.signUpAndSaveNewUserAccount(userDto);
        if (newUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    // 이메일 인증
    @PostMapping("/verify-email")
    public ResponseEntity<?> sendEmailVerificationCode(@RequestBody Map<String, String> emailMap) {
        String email = emailMap.get("email");
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isPresent()) {
            try {
                userService.sendEmailVerificationCode(user.get());
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                e.printStackTrace(); // 예외 메시지를 콘솔에 출력
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else return ResponseEntity.notFound().build();
    }
    // 이메일 인증 확인
    @PostMapping("/verify-email-code")
    public ResponseEntity<?> verifyEmail(@RequestBody Requests.VerifyEmailRequest request) {
        Optional<User> user = userService.findUserByEmail(request.email);
        if (user.isPresent()) {
            boolean isVerified = userService.verifyEmail(user.get(), request.code);
            if (isVerified) return ResponseEntity.ok().build();
            else return ResponseEntity.badRequest().build();
        } else return ResponseEntity.notFound().build();
    }
    // 이메일 찾기
    @GetMapping("/find-Email")
    public ResponseEntity<String> findEmailByPhoneNumber(@RequestBody Requests.PhoneNumberRequest request) {
        String email = userService.findEmailByPhoneNumber(request.phoneNumber);
        if (email != null) {
            return ResponseEntity.ok(email);
        } else return ResponseEntity.notFound().build();
    }
    // 비밀번호 찾기
    @PostMapping("/find-password")
    public ResponseEntity<?> sendPasswordResetEmail(@RequestBody Requests.FindPasswordRequest request) {
        try {
            userService.sendPasswordResetEmail(request.phoneNumber, request.email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 토큰 검증 요청
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        try {
            userService.findUserByPasswordResetToken(token);
            // 토큰이 유효하면 OK 상태 코드를 반환합니다.
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 비밀번호 변경 요청
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Requests.PasswordResetRequest request) {
        try {
            User user = userService.findUserByPasswordResetToken(request.token)
                    .orElseThrow(() -> new UsernameNotFoundException("토큰에 해당하는 사용자를 찾을 수 없습니다: " + request.token));
            userService.changeNewPassword(user, request.newPassword);
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

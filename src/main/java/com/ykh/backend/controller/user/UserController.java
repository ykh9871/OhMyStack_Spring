package com.ykh.backend.controller.user;

import com.ykh.backend.dto.user.UserInfoDto;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.repository.user.UserService;
import com.ykh.backend.request.Requests;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    // 비밀번호 변경
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Requests.PasswordChangeRequest request, Authentication authentication) {
        // 인증 객체에서 이메일을 가져옵니다.
        String email = (String) authentication.getPrincipal();
        // 이메일로 사용자를 찾습니다.
        Optional<User> userOptional = userService.findUserByEmail(email);
        // 사용자가 존재하지 않는 경우, 404 Not Found 상태를 반환합니다.
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User user = userOptional.get();
        // 현재 비밀번호가 맞는지 확인
        // 현재 비밀번호가 틀렸다면, 401 Unauthorized 상태를 반환
        if (!userService.checkPassword(user, request.currentPassword))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Optional<User> updatedUser = userService.changePassword(user, request.newPassword);
        // 비밀번호 변경에 실패했다면, 500 Internal Server Error 상태를 반환
        if (updatedUser.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        // 비밀번호 변경이 성공적으로 이루어졌다면, 200 OK 상태를 반환
        return ResponseEntity.ok().build();
    }

    // 회원 정보 변경
    @PutMapping("/userinfo")
    public ResponseEntity<?> changeUserInfo(@RequestBody UserInfoDto userInfoDto, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        Optional<User> updatedUser = userService.changeUserInfo(user, userInfoDto);
        if (updatedUser.isPresent()) return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // 회원 정보 조회
    @GetMapping("/userinfo")
    public ResponseEntity<?> getAllUserInfo(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        ResponseEntity<?> userInfoDto = userService.getAllUserInfo(user);
        return ResponseEntity.ok(userInfoDto);
    }

    // 회원 탈퇴
    @DeleteMapping("withdrawal")
    public ResponseEntity<?> deleteUser(@RequestBody Requests.DeleteUserRequest request, Authentication authentication) {
        // 인증 객체에서 이메일을 가져옵니다.
        String email = (String) authentication.getPrincipal();
        // 이메일로 사용자를 찾기.
        Optional<User> userOptional = userService.findUserByEmail(email);
        // 사용자가 존재하지 않는 경우, 404 Not Found 상태를 반환.
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User user = userOptional.get();
        userService.deleteUser(user, request.password);
        return ResponseEntity.ok().build();
    }
}
package com.ykh.backend.controller.user;

import com.ykh.backend.dto.user.UserIntroduceDto;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.repository.user.UserService;
import com.ykh.backend.service.user.UserIntroduceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserIntroduceController {
    private final UserIntroduceService userIntroduceService;
    private final UserService userService;

    // 자기소개서 리스트 보기
    @GetMapping("/all-introduces")
    public ResponseEntity<List<UserIntroduceDto>> getUserIntroducesByUserId(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        // 이메일로 사용자를 찾습니다.
        Optional<User> userOptional = userService.findUserByEmail(email);

        // 사용자가 존재하지 않는 경우, 404 Not Found 상태를 반환합니다.
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOptional.get();
        List<UserIntroduceDto> userIntroduces = userIntroduceService.getUserIntroducesByUserId(user.getId());
        return ResponseEntity.ok(userIntroduces);
    }

    // 자기소개서 작성
    @PostMapping("/introduce-save")
    public ResponseEntity<UserIntroduceDto> saveUserIntroduce(@RequestBody UserIntroduceDto userIntroduceDto, Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        // 이메일로 사용자를 찾습니다.
        Optional<User> userOptional = userService.findUserByEmail(email);

        // 사용자가 존재하지 않는 경우, 404 Not Found 상태를 반환합니다.
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOptional.get();
        userIntroduceDto.setUserId(user.getId());
        UserIntroduceDto userIntroduce = userIntroduceService.saveUserIntroduce(userIntroduceDto);
        return ResponseEntity.ok(userIntroduce);
    }

    // 자기소개서 조회
    @GetMapping("/introduce/{id}")
    public ResponseEntity<UserIntroduceDto> getUserIntroduce(@PathVariable("id") Long id) {
        UserIntroduceDto userIntroduceDto = userIntroduceService.getUserIntroduce(id);
        return ResponseEntity.ok(userIntroduceDto);
    }

    // 자기소개서 수정
    @PutMapping("/introduce/{id}")
    public ResponseEntity<UserIntroduceDto> updateUserIntroduce(@PathVariable Long id, @RequestBody UserIntroduceDto userIntroduceDto, Principal principal) {
        String email = principal.getName();
        UserIntroduceDto userIntroduce = userIntroduceService.updateUserIntroduce(id, userIntroduceDto, email);
        return ResponseEntity.ok(userIntroduce);
    }

    // 자기소개서 삭제
    @DeleteMapping("/introduce/{id}")
    public ResponseEntity<?> deleteUserIntroduce(@PathVariable Long id) {
        userIntroduceService.deleteUserIntroduce(id);
        return ResponseEntity.ok().build();
    }

    // 자기소개서 대표 설정
    @PutMapping("/represent/{id}")
    public ResponseEntity<UserIntroduceDto> setRepresentTrue(@PathVariable Long id, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        UserIntroduceDto userIntroduceDto = userIntroduceService.setRepresentTrue(user.getId(), id);
        return ResponseEntity.ok(userIntroduceDto);
    }
}
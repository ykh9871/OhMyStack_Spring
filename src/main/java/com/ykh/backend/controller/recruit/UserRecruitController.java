package com.ykh.backend.controller.recruit;

import com.ykh.backend.dto.recruit.RecruitDto;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.repository.user.UserService;
import com.ykh.backend.service.recruit.UserRecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserRecruitController {
    private final UserRecruitService userRecruitService;
    private final UserService userService;

    // 사용자가 선택한 채용공고 업데이트
    @PostMapping("/recruit")
    public ResponseEntity<Void> updateUserRecruits(@RequestBody List<Long> recruitIds, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        user.setUserRecruits(recruitIds);
        user.setUpdatedAt(LocalDateTime.now());
        userService.updateUser(user);

        return ResponseEntity.ok().build();
    }

    // 사용자가 선택한 채용공고 조회
    @GetMapping("/recruit")
    public List<RecruitDto> getUserRecruits(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        return userRecruitService.getUserRecruits(user.getUserRecruits());
    }

    // 사용자가 선택한 채용공고 제거
    @DeleteMapping("/recruit")
    public ResponseEntity<Void> removeRecruitsFromUser(@RequestBody List<Long> recruitIdsToRemove, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        List<Long> userRecruits = user.getUserRecruits();
        userRecruits.removeAll(recruitIdsToRemove);

        if (userRecruits.isEmpty()) {
            user.setUserRecruits(null);
        }

        user.setUpdatedAt(LocalDateTime.now());
        userService.updateUser(user);

        return ResponseEntity.ok().build();
    }
}
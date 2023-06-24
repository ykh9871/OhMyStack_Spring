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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserRecruitController {
    private final UserRecruitService userRecruitService;
    private final UserService userService;

    // 사용자가 선택한 채용공고 업데이트
    @PostMapping("/recruit")
    public ResponseEntity<Void> addUserRecruits(@RequestBody List<Long> recruitIdsToAdd, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        List<Long> userRecruits = user.getUserRecruits();

        // Set으로 변경하여 중복 제거
        Set<Long> recruitIdsToAddSet = new HashSet<>(recruitIdsToAdd);

        // 이미 존재하는 ID 제거
        userRecruits.forEach(recruitIdsToAddSet::remove);

        // 추가
        userRecruits.addAll(recruitIdsToAddSet);

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
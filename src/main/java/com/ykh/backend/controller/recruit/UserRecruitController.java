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
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserRecruitController {
    private final UserRecruitService userRecruitService;
    private final UserService userService;

    // 사용자가 선택한 채용공고 좋아요기능
    @PostMapping("/recruit")
    public ResponseEntity<Void> toggleUserRecruits(@RequestBody List<Long> recruitIdsToToggle, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        List<Long> userRecruits = user.getUserRecruits();

        // userRecurits가 null인 경우 빈 ArrayList로 초기화합니다
        if (userRecruits == null) {
            userRecruits = new ArrayList<>();
            user.setUserRecruits(userRecruits); // 그냥 업데이트
        }

        for (Long recruitId : recruitIdsToToggle) {
            if (userRecruits.contains(recruitId)) {
                // userRecruits에 recruitId가 이미 포함되어 있으면 제거합니다
                userRecruits.remove(recruitId);
                if (userRecruits.isEmpty()) {
                    user.setUserRecruits(null);
                }
            } else {
                // 그렇지 않으면 목록에 추가합니다
                userRecruits.add(recruitId);
            }
        }

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
package com.ykh.backend.controller.stack;

import com.ykh.backend.entity.stack.TotalStack;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.repository.user.UserService;
import com.ykh.backend.service.stack.UserStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserStackController {
    private final UserStackService userStackService;
    private final UserService userService;

    // 사용자의 기술 스택 업데이트
    @PostMapping("/update-stacks")
    public ResponseEntity<Void> updateUserStacks(@RequestBody List<Long> stackIds, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        user.setUserStacks(stackIds);
        user.setUpdatedAt(LocalDateTime.now());
        userService.updateUser(user);

        return ResponseEntity.ok().build();
    }

    // 사용자의 기술 스택 조회
    @GetMapping("/stacks")
    public List<TotalStack> getUserStacks(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        return userStackService.getUserStacks(user.getUserStacks());
    }

    // 사용자의 기술 스택 제거
    @DeleteMapping("/stacks")
    public ResponseEntity<Void> removeStacksFromUser(@RequestBody List<Long> stackIdsToRemove, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        List<Long> userStacks = user.getUserStacks();
        userStacks.removeAll(stackIdsToRemove);

        if (userStacks.isEmpty()) {
            user.setUserStacks(null);
        }

        user.setUpdatedAt(LocalDateTime.now());
        userService.updateUser(user);

        return ResponseEntity.ok().build();
    }

    // 사용자의 기술 스택 추가
    @PostMapping("/stacks")
    public ResponseEntity<Void> addStacksToUser(@RequestBody List<Long> stackIdsToAdd, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        List<Long> userStacks = user.getUserStacks();
        userStacks.addAll(stackIdsToAdd);
        user.setUpdatedAt(LocalDateTime.now());
        userService.updateUser(user);

        return ResponseEntity.ok().build();
    }
}
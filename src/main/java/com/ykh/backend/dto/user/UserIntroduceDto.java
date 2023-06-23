package com.ykh.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIntroduceDto {
    private Long id;
    private Long userId;
    private String title;
    private String introduce;
    private boolean represent;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}

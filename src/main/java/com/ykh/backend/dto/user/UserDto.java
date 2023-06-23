package com.ykh.backend.dto.user;

import com.ykh.backend.entity.user.AcademicAbility;
import com.ykh.backend.entity.user.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String token;
    private Long id;
    private String phoneNumber;
    private String email;
    private String password;
    private String nickName;
    private String authority;
    private String userAddress;
    private AcademicAbility academicAbility;
    private Department department;
    private Double credit;
}
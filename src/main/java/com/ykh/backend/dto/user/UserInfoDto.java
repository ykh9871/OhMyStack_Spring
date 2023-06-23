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
public class UserInfoDto {
    private String nickName;
    private String phoneNumber;
    private AcademicAbility academicAbility;
    private Department department;
    private Double credit;
    private String userAddress;
}

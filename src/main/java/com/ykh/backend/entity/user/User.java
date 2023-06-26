package com.ykh.backend.entity.user;

import com.ykh.backend.converter.LongArrayConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "휴대전화 번호는 꼭 입력해주세요")
    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @NotEmpty(message = "닉네임은 꼭 입력해주세요")
    @Column(name = "nick_name", unique = true, nullable = false)
    private String nickName;

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @NotEmpty(message = "이메일은 꼭 입력해주세요.")
    @Column(unique = true, nullable = false)
    private String email;

    @NotEmpty(message = "비밀번호는 꼭 입력해주세요")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "delete_user")
    private boolean deleteUser;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_stacks", columnDefinition = "json")
    @Convert(converter = LongArrayConverter.class)
    private List<Long> userStacks;

    @Column(name = "user_recruits", columnDefinition = "json")
    @Convert(converter = LongArrayConverter.class)
    private List<Long> userRecruits;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<UserIntroduce> userIntroduces;

    @ManyToOne
    @JoinColumn(name="academic_ability_id")
    private AcademicAbility academicAbility;

    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

    @Column(name = "credit")
    private Double credit;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT NULL")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME DEFAULT NULL")
    private LocalDateTime deletedAt;
}

package com.ykh.backend.service.user;

import com.ykh.backend.dto.user.UserDto;
import com.ykh.backend.dto.user.UserInfoDto;
import com.ykh.backend.entity.email.EmailVerificationCode;
import com.ykh.backend.entity.user.AcademicAbility;
import com.ykh.backend.entity.user.Authority;
import com.ykh.backend.entity.user.Department;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.repository.email.EmailVerificationCodeRepository;
import com.ykh.backend.repository.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AcademicAbilityRepository academicAbilityRepository;
    private final DepartmentRepository departmentRepository;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;

    @Override
    public User getUserFromAuthentication(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자 Email :" + email));
    }
    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> signUpAndSaveNewUserAccount(UserDto accountDto) {
        // 이메일로 사용자를 찾습니다.
        Optional<User> userOptional = userRepository.findByEmail(accountDto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 사용자가 탈퇴한 경우, 사용자 정보를 삭제하거나 별도로 보관합니다.
            if (user.isDeleteUser()) {
                // user와 관련된 UserIntroduce 엔티티도 삭제합니다.
                userRepository.delete(user);
            } else {
                // 탈퇴하지 않은 사용자의 경우, 이미 사용 중인 이메일이라는 예외를 발생시킵니다.
                throw new RuntimeException("이미 사용 중인 이메일입니다.");
            }
        }
        AcademicAbility academicAbility = academicAbilityRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("AcademicAbility with id 1 not found"));
        Department department = departmentRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Department with id 1 not found"));

        User newUser = User.builder()
                .email(accountDto.getEmail())
                .phoneNumber(accountDto.getPhoneNumber())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .nickName(accountDto.getNickName())
                .authority(Authority.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .academicAbility(academicAbility)
                .department(department)
                .build();

        userRepository.save(newUser);

        return Optional.of(newUser);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserDetailsImpl::build)
                .orElseThrow(() -> new UsernameNotFoundException("이메일을 사용하는 사용자를 찾을 수 없습니다: " + email));
    }

    @Override
    public void deleteUser(User user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setDeleteUser(true);
            user.setDeletedAt(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
    }
    @Override
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public Optional<User> changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> changeUserInfo(User user, UserInfoDto userInfoDto) {
        user.setNickName(userInfoDto.getNickName());
        user.setPhoneNumber(userInfoDto.getPhoneNumber());
        user.setAcademicAbility(userInfoDto.getAcademicAbility());
        user.setDepartment(userInfoDto.getDepartment());
        user.setCredit(userInfoDto.getCredit());
        user.setUserAddress(userInfoDto.getUserAddress());
        userRepository.save(user);
        return Optional.of(user);
    }
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public void sendEmailVerificationCode(String email) throws Exception {
        String code = generateVerificationCode();

        // 전자 메일 확인 코드 만들기 또는 업데이트
        EmailVerificationCode emailVerificationCode = emailVerificationCodeRepository.findByEmail(email);
        if (emailVerificationCode == null) {
            emailVerificationCode = new EmailVerificationCode();
            emailVerificationCode.setEmail(email);
        }
        emailVerificationCode.setVerificationCode(code);
        emailVerificationCodeRepository.save(emailVerificationCode);

        String subject = "이메일 인증";
        String message = "인증 코드: " + code + " 입니다.";
        emailService.sendEmail(email, subject, message);
    }

    public enum EmailVerificationStatus {
        VERIFIED, INVALID_CODE, EMAIL_NOT_FOUND
    }
    @Override
    @Transactional
    public EmailVerificationStatus verifyEmail(String email, String code) {
        EmailVerificationCode emailVerificationCode = emailVerificationCodeRepository.findByEmail(email);
        if (emailVerificationCode == null) {
            return EmailVerificationStatus.EMAIL_NOT_FOUND;
        } else if (emailVerificationCode.getVerificationCode().equals(code)) {
            emailVerificationCodeRepository.deleteByEmail(email);
            return EmailVerificationStatus.VERIFIED;
        } else {
            return EmailVerificationStatus.INVALID_CODE;
        }
    }


    @Override
    public String findEmailByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new UsernameNotFoundException("전화번호에 해당하는 사용자를 찾을 수 없습니다: " + phoneNumber));
        return user.getEmail();
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        user.setPasswordResetToken(token);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByPasswordResetToken(String token) {
        return userRepository.findByPasswordResetToken(token);
    }


    @Override
    public void sendPasswordResetEmail(String phoneNumber, String email) throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("전화번호에 해당하는 사용자를 찾을 수 없습니다: " + phoneNumber));

        if (user.getEmail().equals(email)) {
            String verificationCode = generateVerificationCode();
            createPasswordResetTokenForUser(user, verificationCode);

            String subject = "비밀번호 재설정 안내";
            String message = "비밀번호를 재설정하려면 다음 코드를 입력하세요: " + verificationCode;

            emailService.sendEmail(user.getEmail(), subject, message);
        } else {
            throw new IllegalArgumentException("전화번호에 연결된 이메일과 일치하지 않는 이메일이 제공되었습니다");
        }
    }

    @Override
    public void changeNewPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);  // 토큰 사용 완료 후 null로 설정
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    private String generateVerificationCode() {
        return UUID.randomUUID().toString();
    }
    @Override
    public Optional<User> getByCredentials(final String email, final String password , final PasswordEncoder encoder) {
        final Optional<User> originalUser = userRepository.findByEmail(email);

        // matches 메서드를 이용해 패스워드가 같은지 확인하고 deleteUser 필드가 false인지 확인
        if (originalUser.isPresent() && encoder.matches(password, originalUser.get().getPassword()) && !originalUser.get().isDeleteUser()) {
            return originalUser;
        }
        return Optional.empty();
    }
    @Override
    public ResponseEntity<?> getAllUserInfo(User user) {
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .nickName(user.getNickName())
                .phoneNumber(user.getPhoneNumber())
                .academicAbility(user.getAcademicAbility())
                .department(user.getDepartment())
                .credit(user.getCredit())
                .userAddress(user.getUserAddress())
                .build();

        return ResponseEntity.ok(userInfoDto);
    }

}
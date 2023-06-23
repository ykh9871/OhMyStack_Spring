package com.ykh.backend.service.user;

import com.ykh.backend.dto.user.UserIntroduceDto;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.entity.user.UserIntroduce;
import com.ykh.backend.repository.user.UserIntroduceRepository;
import com.ykh.backend.repository.user.UserRepository;
import com.ykh.backend.repository.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserIntroduceService {
    private final UserRepository userRepository;
    private final UserIntroduceRepository userIntroduceRepository;
    private final UserService userService;

    private UserIntroduceDto convertToDto(UserIntroduce userIntroduce) {
        UserIntroduceDto userIntroduceDto = new UserIntroduceDto();
        userIntroduceDto.setId(userIntroduce.getId());
        userIntroduceDto.setTitle(userIntroduce.getTitle());
        userIntroduceDto.setIntroduce(userIntroduce.getIntroduce());
        userIntroduceDto.setRepresent(userIntroduce.isRepresent());
        userIntroduceDto.setCreated_at(userIntroduce.getCreatedAt());
        userIntroduceDto.setUpdated_at(userIntroduce.getUpdatedAt());

        return userIntroduceDto;
    }

    public List<UserIntroduceDto> getUserIntroducesByUserId(Long userId) {
        List<UserIntroduce> userIntroduces = userIntroduceRepository.findByUser_IdAndInvisibleFalse(userId);
        return userIntroduces.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserIntroduceDto saveUserIntroduce(UserIntroduceDto dto) {
        // 사용자 ID로 사용자를 조회합니다.
        Optional<User> user = userRepository.findById(dto.getUserId());
        if (user.isPresent()) {
            // 조회된 사용자가 있다면 새로운 UserIntroduce 객체를 생성하고 정보를 설정합니다.
            UserIntroduce userIntroduce = new UserIntroduce();
            userIntroduce.setUser(user.get());
            userIntroduce.setTitle(dto.getTitle());
            userIntroduce.setIntroduce(dto.getIntroduce());
            userIntroduce.setCreatedAt(LocalDateTime.now());

            // 생성된 UserIntroduce 객체를 저장합니다.
            UserIntroduce savedUserIntroduce = userIntroduceRepository.save(userIntroduce);

            // 저장된 UserIntroduce 객체를 UserIntroduceDto로 변환하여 반환합니다.
            return convertToDto(savedUserIntroduce);
        }

        // 사용자를 조회할 수 없다면 예외를 발생시킵니다.
        throw new IllegalArgumentException("해당 ID의 사용자가 없습니다.: " + dto.getUserId());
    }



    @Transactional
    public UserIntroduceDto getUserIntroduce(Long id) {
        UserIntroduce userIntroduce = userIntroduceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 자기소개서가 없습니다: " + id));

        return convertToDto(userIntroduce);
    }

    // Service
    @Transactional
    public UserIntroduceDto updateUserIntroduce(Long id, UserIntroduceDto userIntroduceDto, String email) {
        UserIntroduce userIntroduce = userIntroduceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 자기소개서가 없습니다: " + id));

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 없습니다.: " + email));

        if (!user.getId().equals(userIntroduce.getUser().getId())) {
            throw new IllegalArgumentException("요청한 사용자와 현재 로그인된 사용자가 일치하지 않습니다.");
        }

        userIntroduce.setTitle(userIntroduceDto.getTitle());
        userIntroduce.setIntroduce(userIntroduceDto.getIntroduce());
        userIntroduce.setUpdatedAt(LocalDateTime.now());
        UserIntroduce updatedUserIntroduce = userIntroduceRepository.save(userIntroduce);

        return convertToDto(updatedUserIntroduce);
    }



    @Transactional
    public void deleteUserIntroduce(Long id) {
        UserIntroduce userIntroduce = userIntroduceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 자기소개서가 없습니다: " + id));
        userIntroduce.setDeletedAt(LocalDateTime.now());
        userIntroduce.setInvisible(true);
        userIntroduceRepository.save(userIntroduce);
    }

    @Transactional
    public UserIntroduceDto setRepresentTrue(Long userId, Long introduceId) {
        List<UserIntroduce> userIntroduces = userIntroduceRepository.findByUser_Id(userId);

        // 해당 사용자의 모든 자기소개서의 represent 필드를 false로 설정합니다.
        userIntroduces.forEach(ui -> {
            ui.setRepresent(false);
            userIntroduceRepository.save(ui);
        });

        // 선택된 자기소개서의 represent 필드만 true로 설정합니다.
        UserIntroduce userIntroduce = userIntroduceRepository.findById(introduceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 자기소개서가 없습니다: " + introduceId));
        userIntroduce.setRepresent(true);
        UserIntroduce representedUserIntroduce = userIntroduceRepository.save(userIntroduce);

        // 저장된 UserIntroduce 객체를 UserIntroduceDto로 변환하여 반환합니다.
        return convertToDto(representedUserIntroduce);
    }

}

package com.ykh.backend.controller.login;

import com.ykh.backend.dto.user.ResponseDto;
import com.ykh.backend.dto.user.UserDto;
import com.ykh.backend.entity.user.User;
import com.ykh.backend.jwt.TokenProvider;
import com.ykh.backend.repository.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserDto userDto){
        // 사용자 인증
        Optional<User> user = userService.getByCredentials(
                userDto.getEmail(),
                userDto.getPassword(),
                passwordEncoder
        );
        if(user.isPresent()){
            // 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDto responseUserDTO = UserDto.builder()
                    .email(user.get().getEmail())
                    .id(user.get().getId())
                    .token(token)
                    .nickName(user.get().getNickName())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }else {
            // 로그인 실패 응답
            ResponseDto<Object> responseDto = ResponseDto.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDto);
        }
    }
    // 로그아웃은 클라이언트 측에서 JWT 토큰을 삭제하는 것으로 구현한다.
    // 따라서 서버 측에서 별도의 로그아웃 처리는 필요하지 않다.
}
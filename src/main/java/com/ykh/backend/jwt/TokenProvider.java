package com.ykh.backend.jwt;

import com.ykh.backend.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;


@Slf4j
@Service
// JWT 토큰의 생성과 검증을 담당하는 클래스.
public class TokenProvider {

    private final Key key;

    // 생성자에서는 jwt.secret 값으로부터 키를 생성
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // User 객체로부터 JWT 토큰을 생성
    // 생성된 토큰은 사용자의 이메일을 주체로 가지며, 유효 기간은 현재로부터 24시간
    public String create(Optional<User> user){
        //기한은 지금부터 24시간으로 설정
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));
        //JWT 토큰 생성
        return Jwts.builder()
                //header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(key, SignatureAlgorithm.HS512)
                //payload에 들어갈 내용
                .setSubject(user.get().getEmail())//sub
                .setIssuer("ohMyStack")//iss
                .setIssuedAt(new Date())//iat
                .setExpiration(expiryDate)//exp
                .compact();
    }

    // 주어진 JWT 토큰을 검증하고, 토큰에 포함된 이메일을 반환
    // 토큰이 위조되었거나 만료되었다면 예외를 발생
    public String validateAndGetEmail(String token){
        /* parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
         * 헤더와 페이로드를 setStringKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
         * 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날림
         * 그중 우리는 Email이 필요하므로 getBody 를 부른다.*/
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
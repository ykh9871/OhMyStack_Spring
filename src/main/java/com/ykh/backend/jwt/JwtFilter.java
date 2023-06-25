package com.ykh.backend.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
// 이 필터는 HTTP 요청에 대해 한 번 실행되며, JWT 토큰을 추출, 검증하고, 사용자 식별 정보를 가져와 저장한다.
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    // 실제 필터링 로직이 이 메서드에 구현되어 있다.
    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰을 검증하고 인증 정보를 현재 스레드의 SecurityContext에 저장한다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            //요청에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("Filter is running ...");
            //토큰 검사하기. JWT이므로 인가 서버에 요청하지 않고도 검증 가능
            if(token != null && !token.equalsIgnoreCase("null")){
                //userID 가져오기. 위조된 경우 예외 처리된다.
                String email = tokenProvider.validateAndGetEmail(token);
                log.info("Authenticated email : " + email);
                //인증 완료. SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email, //Email이 AuthenticationPrincipal(또는 principal)로 불려오는 값이다.
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        }catch (Exception ex){
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request,response);
    }

     // HTTP 요청에서 Bearer 토큰을 추출하는 함수.
    private String parseBearerToken(HttpServletRequest request) {
        //Authorization 헤더를 파싱하여 Bearer 토큰을 반환한다.
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
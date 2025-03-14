package org.myteam.server.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.myteam.server.auth.entity.Refresh;
import org.myteam.server.auth.repository.RefreshJpaRepository;
import org.myteam.server.global.security.dto.CustomUserDetails;
import org.myteam.server.global.security.jwt.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static org.myteam.server.global.security.jwt.JwtProvider.*;
import static org.myteam.server.member.domain.MemberStatus.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshJpaRepository refreshJpaRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, RefreshJpaRepository refreshJpaRepository) {
        setFilterProcessesUrl("/login");
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshJpaRepository = refreshJpaRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON 요청 본문에서 username과 password 추출
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);

            String username = credentials.get("username");
            String password = credentials.get("password");

            log.info("로그인 요청 - username: {}, password: {}", username, password);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            log.error("로그인 요청 JSON 파싱 오류", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        try {
            // UserDetails
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            String username = customUserDetails.getUsername();
            UUID publicId = customUserDetails.getPublicId();
            String status = customUserDetails.getStatus();

            log.info("successfulAuthentication > username : {}", username);
            log.info("successfulAuthentication > publicId : {}", publicId);
            log.info("successfulAuthentication > status : {}", status);

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();

            if (status.equals(PENDING.name())) {
                log.warn("PENDING 상태인 경우 로그인이 불가능합니다");
                sendErrorResponse(response, HttpStatus.LOCKED, "PENDING 상태인 경우 로그인이 불가능합니다");
                return;
            } else if (status.equals(INACTIVE.name())) {
                log.warn("INACTIVE 상태인 경우 로그인이 불가능합니다");
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "INACTIVE 상태인 경우 로그인이 불가능합니다");
                return;
            } else if (!status.equals(ACTIVE.name())) {
                log.warn("알 수 없는 유저 상태 코드 : " + status);
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "알 수 없는 유저 상태 코드 : " + status);
                return;
            }

            // 권한 획득
            String role = auth.getAuthority();

            // Authorization
            String accessToken = jwtProvider.generateToken(TOKEN_CATEGORY_ACCESS, Duration.ofDays(1), publicId, role, status);

            log.debug("print accessToken: {}", accessToken);
            log.debug("print role: {}", role);

            response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + accessToken);
            response.setStatus(HttpStatus.OK.value());

            log.info("자체 서비스 로그인에 성공하였습니다.");
        } catch (InternalAuthenticationServiceException e) {
            System.out.println("successfulAuthentication 메서드 에러 발생 : " + e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String message = failed.getMessage();
        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
        log.debug("message : {}", message);
        System.out.println("fail authentication");
    }

    public void addRefreshEntity(UUID publicId, String refresh, Duration duration) {
        log.info("addRefreshEntity > publicId: {}, refresh: {}, expiredMs: {}", publicId, refresh, duration.toMillis());
        Date date = new Date(System.currentTimeMillis() + duration.toMillis());

        Refresh refreshEntity = Refresh.builder()
                .publicId(publicId)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshJpaRepository.save(refreshEntity);
    }

    /**
     * 공통 에러 응답 처리 메서드
     *
     * @param response   HttpServletResponse
     * @param httpStatus HTTP 상태 오브젝트
     * @param message    메시지
     * @throws IOException
     */
    private void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus, String message) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"message\":\"%s\",\"status\":\"%s\"}", message, httpStatus.name()));
    }
}

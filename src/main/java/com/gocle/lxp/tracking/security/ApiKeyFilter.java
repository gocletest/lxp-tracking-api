package com.gocle.lxp.tracking.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gocle.lxp.tracking.common.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyAuthService apiKeyAuthService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Tracking API 이벤트 수신 경로만 필터 적용
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/lxp/events");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 1. API Key 추출
            String apiKey = extractApiKey(request);

            // 2. 인증 + 정책 검증
            apiKeyAuthService.authenticate(apiKey, request);

            // 3. 통과
            filterChain.doFilter(request, response);

        } catch (ApiKeyException e) {

            // ❗ Filter 레벨에서 직접 응답 생성
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ApiResponse<?> body = ApiResponse.error(403, e.getMessage());
            response.getWriter().write(
                objectMapper.writeValueAsString(body)
            );
        }
    }

    /**
     * API Key 추출
     * 1. Header (권장)
     * 2. Query Param (Beacon fallback)
     */
    private String extractApiKey(HttpServletRequest request) {

        // Header 우선
        String headerKey = request.getHeader("X-LXP-KEY");
        if (headerKey != null && !headerKey.isBlank()) {
            return headerKey;
        }

        // Beacon fallback
        return request.getParameter("apiKey");
    }
}

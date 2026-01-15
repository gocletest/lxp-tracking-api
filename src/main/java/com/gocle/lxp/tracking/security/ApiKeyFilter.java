package com.gocle.lxp.tracking.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gocle.lxp.tracking.common.ApiResponse;
import com.gocle.lxp.tracking.domain.LxpApiKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyAuthService apiKeyAuthService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        // CORS Preflight 무조건 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

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
            LxpApiKey key = apiKeyAuthService.authenticate(apiKey, request);

            // 3. Controller에서 쓰도록 attribute 주입
            request.setAttribute("apiKeyId", key.getApiKeyId());
            request.setAttribute("clientId", key.getClientId());

            filterChain.doFilter(request, response);

        } catch (ApiKeyException e) {

            response.setStatus(e.getStatusCode());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ApiResponse<?> body =
                    ApiResponse.error(e.getStatusCode(), e.getMessage());

            response.getWriter().write(
                objectMapper.writeValueAsString(body)
            );
        }
    }

    /**
     * API Key 추출
     * 1. Header
     * 2. Query Param (Beacon fallback)
     */
    private String extractApiKey(HttpServletRequest request) {

        String headerKey = request.getHeader("X-LXP-KEY");
        if (headerKey != null && !headerKey.isBlank()) {
            return headerKey;
        }

        return request.getParameter("apiKey");
    }
}

